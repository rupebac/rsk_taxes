package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;
import com.ruho.rsk.utils.TokenContractSpecs;

import java.math.BigDecimal;
import java.util.StringJoiner;

public class RskWithdrawReport extends AbstractReport<RskWithdrawReport>{
    private TokenContractSpecs token;
    private BigDecimal amount;
    private String toAddress;

    public TokenContractSpecs getToken() {
        return token;
    }

    public RskWithdrawReport setToken(TokenContractSpecs token) {
        this.token = token;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public RskWithdrawReport setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public String getToAddress() {
        return toAddress;
    }

    public RskWithdrawReport setToAddress(String toAddress) {
        this.toAddress = toAddress;
        return this;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.WITHDRAW;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RskWithdrawReport.class.getSimpleName() + "[", "]")
                .add("symbol='" + token + "'")
                .add("toAddress='" + toAddress + "'")
                .add("amount=" + amount) + ", " + super.toString();
    }
}
