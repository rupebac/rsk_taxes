package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.StakeReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.MethodIds;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import static com.ruho.rsk.steps.LogEventNames.TOKENS_STAKED;

@Component
public class StakeFilter implements AnyFilter {
    @Override
    public StakeReport generateReport(String ownWallet, RskItem transaction,
                                      List<RskInternalTransaction> internalTransactions) {
        RskLogEvent firstStakeEvent = this.findTokensStakedByMe(transaction).get(0);
        RskLogEvent firstTransferEvent = StepsFilter.findTransferEvents(transaction).get(0);
        return new StakeReport()
                .setMethodIds(methodsIdCalled(internalTransactions))
                .setTransactionHash(transaction.getTransactionHash())
                .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                .setFees(transaction.getTotalFees())
                .setStakedAmount(StepsFilter.findAmountParam(firstStakeEvent, "amount"))
                .setStakedToken(TokenContractSpecs.fromTokenAddress(firstTransferEvent.getSenderAddress()))
        ;
    }

    private List<RskLogEvent> findTokensStakedByMe(RskItem transaction) {
        List<RskLogEvent> tokensStakedEvents = StepsFilter.findAllEvents(transaction, TOKENS_STAKED);
        return tokensStakedEvents.stream()
                .filter(rskLogEvent ->
                    StepsFilter.findFirstParam(rskLogEvent, "staker")
                            .map(param -> transaction.getFromAddress().equalsIgnoreCase(String.valueOf(param.getValue())))
                            .orElse(false)
                )
                .collect(Collectors.toList());
    }

    @Override
    public boolean isTransactionInteresting(String ownWallet, RskItem transaction,
                                            List<RskInternalTransaction> internalTransactions) {
        List<String> methodIds = methodsIdCalled(internalTransactions);
        return methodIds.contains(MethodIds.STAKE_REPORT.getMethodId());
    }

}
