package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskDecodedData;
import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.RemoveLiquidityReport;
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
public class RemoveLiquidityFilter implements AnyFilter {
    @Override
    public RemoveLiquidityReport generateReport(String ownWallet, RskItem transaction,
                                                List<RskInternalTransaction> internalTransactions) {
        String contractAddress = findContractAddress(transaction);
        RskLogEvent liquidityAddedEvent = findLiquidityRemovedEvent(transaction);
        BigDecimal poolTokensAmount = findAmountParam(liquidityAddedEvent, "_poolTokenAmount");
        return PoolContractSpecs.findSpecsFromContract(contractAddress)
                .map(poolContractSpecs -> {
                    BigDecimal baseAmount = getTransferAmount(transaction, poolContractSpecs.getBaseToken());
                    BigDecimal quoteAmount = getTransferAmount(transaction, poolContractSpecs.getQuoteToken());
                    return new RemoveLiquidityReport()
                            .setMethodIds(methodsIdCalled(internalTransactions))
                            .setTransactionHash(transaction.getTransactionHash())
                            .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                            .setFees(transaction.getTotalFees())
                            .setSovsRewards(findRewardsAmount(transaction))
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
        RskLogEvent transferEvent = findTransferEventByToken(transaction, token);
        return findAmountParam(transferEvent);
    }

    private RskLogEvent findTransferEventByToken(RskItem transaction, TokenContractSpecs token) {
        return findTransferEvents(transaction, token).stream()
                .filter(logEvent -> logEvent.getSenderAddress().contains(token.getAddress()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("can't find transferEvent by " + token + " for " + transaction.getTransactionHash()));
    }

    private String findContractAddress(RskItem transaction) {
        RskLogEvent withdrawEvent = findWithdrawEvent(transaction);
        return String.valueOf(findFirstParam(withdrawEvent, "recipient")
                .map(RskDecodedData.Param::getValue)
                .orElseThrow(() -> new IllegalStateException("recipient param not found for " + withdrawEvent.getTransactionHash() + "_" + withdrawEvent.getLogOffset())));
    }

    private BigDecimal findRewardsAmount(RskItem transaction) {
        List<RskLogEvent> transferEvents = findTransferEvents(transaction, TokenContractSpecs.SOV);
        Collections.reverse(transferEvents);
        RskLogEvent rewardsEvent = transferEvents.stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("can't find Transfer rewards event"));
        return findFirstParam(rewardsEvent, "value")
                .map(RskDecodedData.Param::getValue)
                .map(number -> NumberParser.numberFrom(number, rewardsEvent.getSenderContractDecimals()))
                .orElseThrow(() -> new IllegalStateException("value param not found for hash: " + rewardsEvent.getTransactionHash() + " offset: " + rewardsEvent.getLogOffset()));
    }


    @Override
    public boolean isTransactionInteresting(String ownWallet, RskItem transaction,
                                            List<RskInternalTransaction> internalTransactions) {
        List<String> methodIds = methodsIdCalled(internalTransactions);
        return methodIds.contains(MethodIds.REMOVE_LIQUIDITY.getMethodId());
    }


}
