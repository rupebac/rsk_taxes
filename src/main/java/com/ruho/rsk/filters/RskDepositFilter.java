package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.RskDepositReport;
import com.ruho.rsk.filters.reports.UnknownTransactionReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class RskDepositFilter implements AnyFilter {


    @Override
    public AnyReport generateReport(String ownWallet, final RskItem transaction,
                                    List<RskInternalTransaction> internalTransactions) {
        try {
            RskLogEvent transferEvent = StepsFilter.findTransferEvents(transaction).iterator().next();
            TokenContractSpecs tokenSpecs = TokenContractSpecs.fromTokenAddress(transferEvent.getSenderAddress());
            BigDecimal amount = StepsFilter.findAmountParam(transferEvent);
            return new RskDepositReport()
                    .setMethodIds(methodsIdCalled(internalTransactions))
                    .setTransactionHash(transaction.getTransactionHash())
                    .setToken(tokenSpecs)
                    .setFees(transaction.getTotalFees())
                    .setAmount(amount)
                    .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC));
        } catch(Exception e) {
            e.printStackTrace(System.err);
            return new UnknownTransactionReport()
                    .setTransactionHash(transaction.getTransactionHash())
                    .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC));
        }

    }

    @Override
    public boolean isTransactionInteresting(String ownWallet, final RskItem transaction,
                                            List<RskInternalTransaction> internalTransactions) {
        List<String> methodIds = methodsIdCalled(internalTransactions);
        return methodIds.isEmpty() && StepsFilter.findTransferEventsToMe(transaction, ownWallet).size() > 0;
    }
}
