package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.VestingWithdrawReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.MethodIds;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.ruho.rsk.steps.StepsFilter.findAmountParam;

@Component
public class VestingWithdrawFilter implements AnyFilter {
    @Override
    public VestingWithdrawReport generateReport(String ownWallet,
                                                RskItem transaction,
                                                List<RskInternalTransaction> internalTransactions) {
        List<RskLogEvent> transferEvents = StepsFilter.findTransferEventsToMe(transaction, ownWallet);
        if(transferEvents.isEmpty()) {
            throw new IllegalStateException("can't find any transfer event in vestingWithdrawFilter " + transaction.getTransactionHash());
        }

        TokenContractSpecs token = TokenContractSpecs.fromTokenAddress(transferEvents.get(0).getSenderAddress());

        return new VestingWithdrawReport()
                .setMethodIds(methodsIdCalled(internalTransactions))
                .setTransactionHash(transaction.getTransactionHash())
                .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                .setFees(transaction.getTotalFees())
                .setToken(token)
                .setAmount(sumTransferAmounts(transferEvents))
        ;
    }

    private BigDecimal sumTransferAmounts(List<RskLogEvent> transferEvents) {
        return transferEvents.stream()
                .map(StepsFilter::findAmountParam)
                .reduce(BigDecimal::add)
                .orElseThrow();
    }

    @Override
    public boolean isTransactionInteresting(String ownWallet,
                                            RskItem transaction,
                                            List<RskInternalTransaction> internalTransactions) {
        List<String> methodIds = methodsIdCalled(internalTransactions);
        return methodIds.contains(MethodIds.VESTING_WITHDRAW.getMethodId());
    }

}
