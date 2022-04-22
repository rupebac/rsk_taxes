package com.ruho.rsk.generators;

import com.ruho.rsk.filters.reports.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CTCReportGenerator {
    private List<String[]> generate(AnyReport report) {
        if(report instanceof SpotSwapReport) {
            return spotSwap((SpotSwapReport) report);
        } else if( report instanceof VestingWithdrawReport) {
            return withdrawVesting((VestingWithdrawReport) report);
        } else if(report instanceof AddLiquidityReport) {
            return new CTCLiquidityWithLpTokenGen().addLiquidity((AddLiquidityReport) report);
        } else if(report instanceof RemoveLiquidityReport) {
            return new CTCLiquidityWithLpTokenGen().removeLiquidity((RemoveLiquidityReport) report);
        } else if(report instanceof RskDepositReport) {
            return deposit((RskDepositReport) report);
        } else if(report instanceof RskWithdrawReport) {
            return withdraw((RskWithdrawReport) report);
        } else if(report instanceof StakeReport) {
            return stake((StakeReport) report);
        }
        return new ArrayList<>();
    }

    private List<String[]> spotSwap(SpotSwapReport report) {
        return List.<String[]>of(
            CTCPrinter.printToArray(
                    report.getTime(),
                    "buy",
                    report.getTargetSymbol(),
                    report.getTargetAmount(),
                    report.getSourceSymbol(),
                    report.getSourceAmount(),
                    report.getFees(),
                    "", "", report.getTransactionHash(),
                    "swap")
        );
    }

    private List<String[]> deposit(RskDepositReport report) {
        return List.<String[]>of(
                CTCPrinter.printToArray(
                        report.getTime(),
                        "buy",
                        report.getToken().getPrintSymbol(),
                        report.getAmount(),
                        "",
                        null,
                        report.getFees(),
                        "",
                        "Sovryn",
                        report.getTransactionHash(),
                        "bridge in to sovryn"
                )
        );
    }

    private List<String[]> stake(StakeReport report) {
        return List.<String[]>of(
                CTCPrinter.printToArray(
                        report.getTime(),
                        "transfer-out",
                        report.getStakedToken().getPrintSymbol(),
                        report.getStakedAmount(),
                        "",
                        null,
                        report.getFees(),
                        "",
                        "SovrynStake",
                        report.getTransactionHash(),
                        "stake to sovryn"
                )
        );
    }

    private List<String[]> withdraw(RskWithdrawReport report) {
        return List.<String[]>of(
                CTCPrinter.printToArray(
                        report.getTime(),
                        "sell",
                        report.getToken().getPrintSymbol(),
                        report.getAmount(),
                        "",
                        null,
                        report.getFees(),
                        "Sovryn", "",
                        report.getTransactionHash(),
                        "bridge out from sovryn")
        );
    }

    private List<String[]> withdrawVesting(VestingWithdrawReport report) {
        return List.<String[]>of(
                CTCPrinter.printToArray(
                        report.getTime(),
                        "staking",
                        report.getToken().getPrintSymbol(),
                        report.getAmount(),
                        "",
                        null,
                        report.getFees(),
                        "", "", report.getTransactionHash(),
                        "VestingWithdrawReport")
        );
    }

    public void generatorReport(List<AnyReport> reports) throws IOException {

        FileWriter out = new FileWriter("ctc_report.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(CTCPrinter.HEADERS))) {
            for (AnyReport report : reports) {
                for (String[] entry : generate(report)) {
                    printer.printRecord(entry);
                }
            }
        }
    }
}
