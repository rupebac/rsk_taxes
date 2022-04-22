package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;
import com.ruho.rsk.utils.TokenContractSpecs;

import java.math.BigDecimal;
import java.util.StringJoiner;

public class StakeReport extends AbstractReport<StakeReport> {

    private TokenContractSpecs stakedToken;
    private BigDecimal stakedAmount;


    public TokenContractSpecs getStakedToken() {
        return stakedToken;
    }

    public StakeReport setStakedToken(TokenContractSpecs stakedToken) {
        this.stakedToken = stakedToken;
        return this;
    }

    public BigDecimal getStakedAmount() {
        return stakedAmount;
    }

    public StakeReport setStakedAmount(BigDecimal stakedAmount) {
        this.stakedAmount = stakedAmount;
        return this;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.STAKE;
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", StakeReport.class.getSimpleName() + "[", "]")
                .add("stakedToken=" + stakedToken)
                .add("stakedAmount=" + stakedAmount) + "  " + super.toString();
    }
}
