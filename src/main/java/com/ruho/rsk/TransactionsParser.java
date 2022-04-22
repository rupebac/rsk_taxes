package com.ruho.rsk;

import com.ruho.rsk.domain.RskDto;
import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.filters.AnyFilter;
import com.ruho.rsk.filters.UnknownTransactionFilter;
import com.ruho.rsk.filters.reports.AnyReport;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TransactionsParser {
    private final UnknownTransactionFilter unknownTransactionFilter;
    private final List<AnyFilter> allFilters;

    public TransactionsParser(UnknownTransactionFilter unknownTransactionFilter,
                              List<AnyFilter> allFilters) {
        this.unknownTransactionFilter = unknownTransactionFilter;
        this.allFilters = allFilters;
    }

    public List<AnyReport> parse(String ownWallet, RskDto dto, RskInternalTransaction[] internalTransactions) {
        Map<String, List<RskInternalTransaction>> internalTransactionsMap = Arrays.stream(internalTransactions)
                .collect(Collectors.groupingBy(RskInternalTransaction::getTransaction));

        return dto.getData().getItems().stream()
                .filter(RskItem::getSuccessful)
                .map(transaction -> {
                    List<RskInternalTransaction> internalTransactionsForHash = internalTransactionsMap.get(transaction.getTransactionHash());
                    return allFilters.stream()
                        .filter(anyFilter -> !(anyFilter instanceof UnknownTransactionFilter))
                        .filter(anyFilter -> anyFilter.isTransactionInteresting(ownWallet, transaction, internalTransactionsForHash))
                        .map(anyFilter -> anyFilter.generateReport(ownWallet, transaction, internalTransactionsForHash))
                        .findFirst()
                        .orElseGet(() ->
                            unknownTransactionFilter.generateReport(ownWallet, transaction, internalTransactionsForHash)
                        );
                })
                .collect(Collectors.toList());
    }

}
