package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;
import com.ruho.rsk.utils.TokenContractSpecs;

import java.math.BigDecimal;
import java.util.StringJoiner;

public class ClaimRewardsReport extends AbstractReport<ClaimRewardsReport> {

    private TokenContractSpecs claimedToken;
    private BigDecimal claimedAmount;


    public TokenContractSpecs getClaimedToken() {
        return claimedToken;
    }

    public ClaimRewardsReport setClaimedToken(TokenContractSpecs claimedToken) {
        this.claimedToken = claimedToken;
        return this;
    }

    public BigDecimal getClaimedAmount() {
        return claimedAmount;
    }

    public ClaimRewardsReport setClaimedAmount(BigDecimal claimedAmount) {
        this.claimedAmount = claimedAmount;
        return this;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.CLAIM_REWARDS;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ClaimRewardsReport.class.getSimpleName() + "[", "]")
                .add("claimedToken=" + claimedToken)
                .add("claimedAmount=" + claimedAmount)
                .toString() + "  " + super.toString();
    }
}
