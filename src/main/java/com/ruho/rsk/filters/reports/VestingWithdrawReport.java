package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;
import com.ruho.rsk.utils.TokenContractSpecs;

import java.math.BigDecimal;

public class VestingWithdrawReport extends AbstractReport<VestingWithdrawReport> {

    private TokenContractSpecs token;            // usdt ?
    private BigDecimal amount;


    public TokenContractSpecs getToken() {
        return token;
    }

    public VestingWithdrawReport setToken(TokenContractSpecs symbol) {
        this.token = symbol;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public VestingWithdrawReport setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.SPOT_SWAP;
    }

    @Override
    public String toString() {
        return "VestingWithdrawReport{" +
                "symbol='" + token + '\'' +
                ", amount=" + amount +
                "} " + super.toString();
    }
}
