package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.AnyReport;
import com.ruho.rsk.filters.reports.SpotSwapReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.MethodIds;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class ConversionSwapFilter implements AnyFilter {

    @Override
    public AnyReport generateReport(String ownWallet, final RskItem transaction,
                                    List<RskInternalTransaction> internalTransactions) {
        List<RskLogEvent> transferEvents = StepsFilter.findTransferEvents(transaction);
        RskLogEvent targetTransfer = transferEvents.stream().iterator().next();
        RskLogEvent sourceTransfer = transferEvents.get(transferEvents.size() - 1);

        BigDecimal fromAmount = StepsFilter.findAmountParam(sourceTransfer);
        BigDecimal toAmount = StepsFilter.findAmountParam(targetTransfer);

        return new SpotSwapReport()
                .setMethodIds(methodsIdCalled(internalTransactions))
                .setTransactionHash(transaction.getTransactionHash())
                .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                .setFees(transaction.getTotalFees())
                .setSourceSymbol(TokenContractSpecs.fromTokenAddress(sourceTransfer.getSenderAddress()).getPrintSymbol())
                .setSourceAmount(fromAmount)
                .setTargetSymbol(TokenContractSpecs.fromTokenAddress(targetTransfer.getSenderAddress()).getPrintSymbol())
                .setTargetAmount(toAmount);
    }

    @Override
    public boolean isTransactionInteresting(String ownWallet, final RskItem transaction,
                                            List<RskInternalTransaction> internalTransactions) {
        List<String> methodIds = methodsIdCalled(internalTransactions);
        return methodIds.contains(MethodIds.SPOT_CONVERT_BY_PATH_2.getMethodId()) ||
                methodIds.contains(MethodIds.SPOT_CONVERT_BY_PATH.getMethodId()) ||
                methodIds.contains(MethodIds.SPOT_EXTERNAL_SWAP.getMethodId());
    }
}
