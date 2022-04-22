package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskDecodedData;
import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.RskWithdrawReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.MethodIds;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class RskWithdrawFilter implements AnyFilter {


    @Override
    public AnyReport generateReport(String ownWallet, final RskItem transaction,
                                    List<RskInternalTransaction> internalTransactions) {
        List<String> methodIds = methodsIdCalled(internalTransactions);
        TokenContractSpecs tokenSpecs;
        BigDecimal amount;
        String sentToAddress;
        if(methodIds.contains(MethodIds.NEW_BITCOIN_TRANSFER.getMethodId())) {
            tokenSpecs = internalTransactions.get(0).getValueSymbol();
            amount = internalTransactions.get(0).getValueAmount();
            sentToAddress = transaction.getToAddress();
        } else {
            RskLogEvent transferEvent = StepsFilter.findTransferEvents(transaction).iterator().next();
            tokenSpecs = TokenContractSpecs.fromTokenAddress(transferEvent.getSenderAddress());
            amount = StepsFilter.findAmountParam(transferEvent);
            RskLogEvent sentEvent = StepsFilter.findSentEvent(transaction);
            RskDecodedData.Param toParam = StepsFilter.findFirstParam(sentEvent, "to").orElseThrow(() -> new IllegalStateException("can't find to param on sentEvent for transaction " + transaction.getTransactionHash()));
            sentToAddress = String.valueOf(toParam.getValue());
        }

        return new RskWithdrawReport()
                .setMethodIds(methodIds)
                .setTransactionHash(transaction.getTransactionHash())
                .setToken(tokenSpecs)
                .setAmount(amount)
                .setFees(transaction.getTotalFees())
                .setToAddress(sentToAddress)
                .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC));
    }

    @Override
    public boolean isTransactionInteresting(String ownWallet, final RskItem transaction,
                                            List<RskInternalTransaction> internalTransactions) {
        List<String> methodIds = methodsIdCalled(internalTransactions);
        return methodIds.contains(MethodIds.NEW_BITCOIN_TRANSFER.getMethodId()) ||
                (transaction.getLogEvents().stream().anyMatch(StepsFilter::isBurned) &&
                transaction.getLogEvents().stream().anyMatch(StepsFilter::isTransfer) &&
                transaction.getLogEvents().stream().anyMatch(StepsFilter::isSent))
        ;
    }
}
