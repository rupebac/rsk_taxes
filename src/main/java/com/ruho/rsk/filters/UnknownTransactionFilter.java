package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.filters.reports.UnknownTransactionReport;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class UnknownTransactionFilter implements AnyFilter {
    @Override
    public UnknownTransactionReport generateReport(String ownWallet, RskItem transaction,
                                             List<RskInternalTransaction> internalTransactions) {
        UnknownTransactionReport unknownTransactionReport = new UnknownTransactionReport();
        unknownTransactionReport.setMethodIds(methodsIdCalled(internalTransactions));
        unknownTransactionReport.setNumberEvents(transaction.getLogEvents().size());
        unknownTransactionReport.setFees(transaction.getTotalFees());
        unknownTransactionReport.setTransactionHash(transaction.getTransactionHash());
        unknownTransactionReport.setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC));
        return unknownTransactionReport;
    }


    @Override
    public boolean isTransactionInteresting(String ownWallet, RskItem transaction,
                                            List<RskInternalTransaction> internalTransactions) {
        return true;
    }

}
