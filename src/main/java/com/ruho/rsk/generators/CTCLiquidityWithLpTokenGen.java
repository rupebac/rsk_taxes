package com.ruho.rsk.generators;

import com.ruho.rsk.filters.reports.AddLiquidityReport;
import com.ruho.rsk.filters.reports.RemoveLiquidityReport;
import com.ruho.rsk.utils.TokenContractSpecs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CTCLiquidityWithLpTokenGen {

    public List<String[]> addLiquidity(AddLiquidityReport report) {
        AddLiquidityReport addLiquidityReport = report;
        List<String[]> parts = new ArrayList<>();
        String description = String.format("add liquidity: %s %s and %s %s",
                                           addLiquidityReport.getBaseAmount(),
                                           addLiquidityReport.getBaseToken().getRskSymbol(),
                                           addLiquidityReport.getQuotedAmount(),
                                           addLiquidityReport.getQuotedToken());
        parts.add(sellEntry(addLiquidityReport, addLiquidityReport.getBaseToken(), addLiquidityReport.getBaseAmount()));
        parts.add(sellEntry(addLiquidityReport, addLiquidityReport.getQuotedToken(), addLiquidityReport.getQuotedAmount()));
        parts.add(
                CTCPrinter.printToArray(
                        addLiquidityReport.getTime(),
                        "buy",
                        addLiquidityReport.getPoolTokenSymbol(),
                        addLiquidityReport.getPoolTokenAmount(),
                        "",
                        null,
                        addLiquidityReport.getFees(),
                        "", "", addLiquidityReport.getTransactionHash(),
                        description)
        );
        return parts;
    }

    public List<String[]> removeLiquidity(RemoveLiquidityReport report) {
        RemoveLiquidityReport removeLiquidityReport = report;
        List<String[]> parts = new ArrayList<>();
        String description = String.format("removed liquidity: %s %s and %s %s",
                                           removeLiquidityReport.getBaseAmount(),
                                           removeLiquidityReport.getBaseToken(),
                                           removeLiquidityReport.getQuotedAmount(),
                                           removeLiquidityReport.getQuotedToken());
        parts.add(buyEntry(removeLiquidityReport, removeLiquidityReport.getBaseToken(), removeLiquidityReport.getBaseAmount()));
        parts.add(buyEntry(removeLiquidityReport, removeLiquidityReport.getQuotedToken(), removeLiquidityReport.getQuotedAmount()));
        parts.add(
                CTCPrinter.printToArray(
                        removeLiquidityReport.getTime(),
                        "sell",
                        removeLiquidityReport.getPoolTokenSymbol(),
                        removeLiquidityReport.getPoolTokenAmount(),
                        "",
                        null,
                        removeLiquidityReport.getFees(),
                        "", "", removeLiquidityReport.getTransactionHash(),
                        description)
        );
        return parts;
    }


    private String[] sellEntry(AddLiquidityReport report, TokenContractSpecs symbol, BigDecimal amount) {
        return CTCPrinter.printToArray(
                report.getTime(),
                "sell",
                symbol.getPrintSymbol(),
                amount,
                "",
                null,
                report.getFees(),
                "", "", report.getTransactionHash(),
                "AddLiquidityReport - Sell");
    }

    private String[] buyEntry(RemoveLiquidityReport report, TokenContractSpecs symbol, BigDecimal amount) {
        return CTCPrinter.printToArray(
                report.getTime(),
                "buy",
                symbol.getPrintSymbol(),
                amount,
                "",
                null,
                report.getFees(),
                "", "", report.getTransactionHash(),
                "RemoveLiquidityReport - Buy");
    }
}
