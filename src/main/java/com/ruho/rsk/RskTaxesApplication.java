package com.ruho.rsk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruho.rsk.domain.RskDto;
import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.RskDepositReport;
import com.ruho.rsk.filters.reports.RskWithdrawReport;
import com.ruho.rsk.filters.reports.UnknownTransactionReport;
import com.ruho.rsk.generators.CTCReportGenerator;
import com.ruho.rsk.http.TransactionsFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class RskTaxesApplication implements CommandLineRunner {

	@Autowired
	private TransactionsFetcherService transactionsFetcherService;

	@Autowired
	private TransactionsParser transactionsParser;

	public static void main(String[] args) {
		new SpringApplicationBuilder(RskTaxesApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	List<AnyReport> printReport(String walletAddress, List<AnyReport> entries, RskInternalTransaction[] internalTransactions){
		CTCReportGenerator reportGenerator = new CTCReportGenerator();
		try {
			List<AnyReport> reports = combineReports(walletAddress, entries, internalTransactions);
			reportGenerator.generatorReport(reports);
			return reports;
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
		return new ArrayList<>();
	}

	private List<AnyReport> combineReports(String walletAddress, List<AnyReport> entries, RskInternalTransaction[] internalTransactions) {
		Map<String, AnyReport> existingTransactions = entries.stream()
				.collect(Collectors.toMap(s -> s.getTransactionHash().toLowerCase(), Function.identity()));
		List<AnyReport> allReports = Arrays.stream(internalTransactions)
				.filter(rskInternalTransaction -> {
					String key = rskInternalTransaction.getTransaction().toLowerCase();
					return !existingTransactions.containsKey(key) ||
							existingTransactions.get(key) instanceof UnknownTransactionReport;
				})
				.filter(rskInternalTransaction -> rskInternalTransaction.getStatus().equalsIgnoreCase("SUCCESSFUL"))
				.filter(rskInternalTransaction -> rskInternalTransaction.getValueAmount().doubleValue() > 0)
				.flatMap(rskInternalTransaction -> {
					if(rskInternalTransaction.getTo().equalsIgnoreCase(walletAddress)) {
						return Stream.of(mapToDepositReport(rskInternalTransaction, existingTransactions));
					} else if(rskInternalTransaction.getFrom().equalsIgnoreCase(walletAddress)) {
						return Stream.of(mapToWithdrawReport(rskInternalTransaction, existingTransactions));
					}
					return Stream.empty();
				})
				.collect(Collectors.toList());
		entries.addAll(allReports);
		return entries;
	}

	private AnyReport mapToDepositReport(RskInternalTransaction rskInternalTransaction, Map<String, AnyReport> allReports) {
		Instant instant = Instant.ofEpochSecond(rskInternalTransaction.getTimestamp());
		AnyReport report = allReports.get(rskInternalTransaction.getTransaction());
		return new RskDepositReport()
				.setTransactionHash(rskInternalTransaction.getTransaction())
				.setTime(LocalDateTime.ofInstant(instant, ZoneOffset.UTC))
				.setToken(rskInternalTransaction.getValueSymbol())
				.setFees(report == null ? BigDecimal.ZERO : report.getFees())
				.setAmount(rskInternalTransaction.getValueAmount());
	}

	private AnyReport mapToWithdrawReport(RskInternalTransaction rskInternalTransaction, Map<String, AnyReport> allReports) {
		Instant instant = Instant.ofEpochSecond(rskInternalTransaction.getTimestamp());
		AnyReport report = allReports.get(rskInternalTransaction.getTransaction());
		if(report == null) {
			throw new IllegalStateException("withdraw transaction should be included in covalent. Check transaction " + rskInternalTransaction.getTransaction());
		}
		return new RskWithdrawReport()
				.setTransactionHash(rskInternalTransaction.getTransaction())
				.setTime(LocalDateTime.ofInstant(instant, ZoneOffset.UTC))
				.setToken(rskInternalTransaction.getValueSymbol())
				.setFees(report.getFees())
				.setAmount(rskInternalTransaction.getValueAmount());
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length != 2) {
			System.out.println("please run it with \"<wallet_addr> --classpath\" or with \"<wallet_addr> <apiKey>\"");
			System.exit(1);
		}
		System.out.println("starting");
		RskDto rskDto;
		int pageNumber = 0;
		String walletAddress = args[0];
		String apiKey = args[1];
		List<AnyReport> allEntries = new ArrayList<>();
		RskInternalTransaction[] internalTransactions = internalTransactions(walletAddress);
		do {
			if (apiKey.equals("--classpath")) {
				rskDto = fromClasspath(walletAddress, pageNumber);
			} else {
				rskDto = this.transactionsFetcherService.fetchTransactions(walletAddress, apiKey, pageNumber);
			}
			List<AnyReport> entries = transactionsParser.parse(walletAddress, rskDto, internalTransactions);
			allEntries.addAll(entries);
			pageNumber++;
		} while(rskDto.getData().getPagination().getHasMore());
		printReport(walletAddress, allEntries, internalTransactions).forEach(report -> {
			System.out.println(report);
			System.out.println("------------------");
		});
		System.out.println("completed!");
		System.exit(0);
	}

	private RskDto fromClasspath(String walletAddr, int pageNumber) throws IOException {
		String filePath = Objects.requireNonNull(
				TransactionsParser.class.getClassLoader().getResource(String.format("transactions/%s-%s.json", walletAddr, pageNumber))
		).getFile();
		return this.transactionsFetcherService.fetchTransactions(filePath);
	}

	public RskInternalTransaction[] internalTransactions(String walletAddr) throws IOException {
		URL resource = TransactionsParser.class.getClassLoader().getResource(String.format("transactions/%s-internal.json", walletAddr));
		if (resource == null) {
			return new RskInternalTransaction[0];
		}
		ObjectMapper mapper = new ObjectMapper();
		try (Reader readerInternal = new FileReader(resource.getFile())) {
			return mapper.readValue(readerInternal, RskInternalTransaction[].class);
		}
	}
}
