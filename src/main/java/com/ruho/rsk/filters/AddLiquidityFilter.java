package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.AddLiquidityReport;
import com.ruho.rsk.utils.MethodIds;
import com.ruho.rsk.utils.PoolContractSpecs;
import com.ruho.rsk.utils.NumberParser;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static com.ruho.rsk.steps.StepsFilter.*;

@Component
public class AddLiquidityFilter implements AnyFilter {
    public AddLiquidityReport generateReport(String ownWallet, RskItem transaction,
                                             List<RskInternalTransaction> internalTransactions) {
        String contractAddress = findContractAddress(transaction);
        RskLogEvent liquidityAddedEvent = findLiquidityAddedEvent(transaction);
        BigDecimal poolTokensAmount = findAmountParam(liquidityAddedEvent, "_poolTokenAmount");
        return PoolContractSpecs.findSpecsFromContract(contractAddress)
                .map(poolContractSpecs -> {
                    BigDecimal baseAmount = getTransferAmount(transaction, poolContractSpecs.getBaseToken());
                    BigDecimal quoteAmount = getTransferAmount(transaction, poolContractSpecs.getQuoteToken());
                    return new AddLiquidityReport()
                            .setMethodIds(methodsIdCalled(internalTransactions))
                            .setTransactionHash(transaction.getTransactionHash())
                            .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                            .setFees(transaction.getTotalFees())
                            .setBaseToken(poolContractSpecs.getBaseToken())
                            .setBaseAmount(baseAmount)
                            .setQuotedToken(poolContractSpecs.getQuoteToken())
                            .setQuotedAmount(quoteAmount)
                            .setPoolTokenAmount(poolTokensAmount);
                }).orElseThrow(() ->
                    new IllegalStateException("contractSpecs not found for: " + contractAddress)
                );
    }

    private BigDecimal getTransferAmount(RskItem transaction, TokenContractSpecs token) {
        RskLogEvent transferEvent = findTransferEventBySymbol(transaction, token);
        return findFirstParam(transferEvent, "value")
                .map(params -> NumberParser.numberFrom(params.getValue(), transferEvent.getSenderContractDecimals()))
                .orElseThrow(() -> new IllegalStateException("can't find value param in baseTransfer for " + transaction.getTransactionHash()));
    }

    private RskLogEvent findTransferEventBySymbol(RskItem transaction, TokenContractSpecs token) {
        List<RskLogEvent> transferEvents = findTransferEvents(transaction, token);
        Collections.reverse(transferEvents);
        return transferEvents.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("can't find transferEvent by " + token + " for " + transaction.getTransactionHash()));
    }

    private String findContractAddress(RskItem transaction) {
        RskLogEvent issuanceEvent = findIssuanceEvent(transaction);
        return issuanceEvent.getSenderAddress();
    }

    @Override
    public boolean isTransactionInteresting(String ownWallet, RskItem transaction,
                                            List<RskInternalTransaction> internalTransactions) {
        return methodsIdCalled(internalTransactions).contains(MethodIds.ADD_LIQUIDITY_METHOD.getMethodId());
    }


}
