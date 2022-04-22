package com.ruho.rsk.filters;

import com.ruho.rsk.domain.RskInternalTransaction;
import com.ruho.rsk.domain.RskItem;
import com.ruho.rsk.domain.RskLogEvent;
import com.ruho.rsk.filters.reports.ClaimRewardsReport;
import com.ruho.rsk.steps.StepsFilter;
import com.ruho.rsk.utils.MethodIds;
import com.ruho.rsk.utils.TokenContractSpecs;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static com.ruho.rsk.steps.LogEventNames.*;
import static com.ruho.rsk.steps.StepsFilter.findAmountParam;

@Component
public class ClaimRewardsFilter implements AnyFilter {
    @Override
    public ClaimRewardsReport generateReport(String ownWallet, RskItem transaction,
                                             List<RskInternalTransaction> internalTransactions) {
        RskLogEvent firstTransfer = StepsFilter.findTransferEvents(transaction).stream().findFirst().orElseThrow();
        BigDecimal rewardsClaimedAmount;
        List<String> methodIds = methodsIdCalled(internalTransactions);
        if(methodIds.contains(MethodIds.CREATE_VESTING_AND_STAKE.getMethodId())) {
            rewardsClaimedAmount = findAmountParam(firstTransfer);
        } else {
            rewardsClaimedAmount = findAmountParam(StepsFilter.findRewardsClaimedEvent(transaction), "amount");
        }
        return new ClaimRewardsReport()
                .setMethodIds(methodIds)
                .setTransactionHash(transaction.getTransactionHash())
                .setTime(LocalDateTime.ofInstant(transaction.getBlockSignedAt().toInstant(), ZoneOffset.UTC))
                .setFees(transaction.getTotalFees())
                .setClaimedAmount(rewardsClaimedAmount)
                .setClaimedToken(TokenContractSpecs.fromTokenAddress(firstTransfer.getSenderAddress()))
        ;
    }


    @Override
    public boolean isTransactionInteresting(String ownWallet, RskItem transaction,
                                            List<RskInternalTransaction> internalTransactions) {
        List<String> methodIds = methodsIdCalled(internalTransactions);
        return methodIds.contains(MethodIds.CLAIM_REWARD_FROM_ALL_POOLS.getMethodId()) ||
                methodIds.contains(MethodIds.CREATE_VESTING_AND_STAKE.getMethodId());
    }

}
