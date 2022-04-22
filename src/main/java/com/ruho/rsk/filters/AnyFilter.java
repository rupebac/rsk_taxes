package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.filters.reports.AnyReport;

import java.util.List;
import java.util.stream.Collectors;

public interface AnyFilter {
    AnyReport generateReport(String ownWallet,
                             RskItem transaction,
                             List<RskInternalTransaction> internalTransactions);

    boolean isTransactionInteresting(String ownWallet,
                                     RskItem item,
                                     List<RskInternalTransaction> internalTransactions);

    default List<String> methodsIdCalled(List<RskInternalTransaction> internalTransactions) {
        if(internalTransactions == null) {
            return List.of();
        }
        return internalTransactions.stream()
                .filter(rskInternalTransaction -> "call".equals(rskInternalTransaction.getCallType()))
                .filter(rskInternalTransaction -> "SUCCESSFUL".equals(rskInternalTransaction.getStatus()))
                .map(RskInternalTransaction::getInput)
                .filter(input -> input.length() >= 10)
                .map(input -> input.substring(0, 10))
                .collect(Collectors.toList());
    }
}
