package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;
import com.ruho.rsk.utils.TokenContractSpecs;

import java.math.BigDecimal;
import java.util.StringJoiner;

public class RskDepositReport extends AbstractReport<RskDepositReport>{
    private TokenContractSpecs token;
    private BigDecimal amount;

    public TokenContractSpecs getToken() {
        return token;
    }

    public RskDepositReport setToken(TokenContractSpecs token) {
        this.token = token;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public RskDepositReport setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.DEPOSIT;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RskDepositReport.class.getSimpleName() + "[", "]")
                .add("symbol='" + token + "'")
                .add("amount=" + amount) + ", " + super.toString();
    }
}
