package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.JustApproveReport;
import com.ruho.rsk.steps.LogEventNames;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.MethodIds;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class JustApproveFilter implements AnyFilter{
    @Override
    public AnyReport generateReport(String ownWallet,
                                    RskItem transaction,
                                    List<RskInternalTransaction> internalTransactions) {
        RskLogEvent approval = StepsFilter.requireFirstEvent(transaction, LogEventNames.APPROVAL);
        return new JustApproveReport()
                .setMethodIds(methodsIdCalled(internalTransactions))
                .setTransactionHash(transaction.getTransactionHash())
                .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                .setFees(transaction.getTotalFees())
                .setTokenApproved(approval.getSenderContract_ticker_symbol());
    }

    @Override
    public boolean isTransactionInteresting(String ownWallet,
                                            RskItem transaction,
                                            List<RskInternalTransaction> internalTransactions) {
        List<String> methodIds = methodsIdCalled(internalTransactions);
        return transaction.getLogEvents().size() == 1 && methodIds.contains(MethodIds.APPROVE.getMethodId());
    }
}
