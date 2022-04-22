package com.ruho.rsk.filters.reports;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.StringJoiner;

public abstract class AbstractReport<SELF extends AbstractReport<? extends AnyReport>> implements AnyReport {
    private String transactionHash;
    private LocalDateTime time;
    private BigDecimal fees;
    private List<String> methodIds;

    @Override
    public String getTransactionHash() {
        return transactionHash;
    }

    public SELF setTransactionHash(final String transactionHash) {
        this.transactionHash = transactionHash;
        return self();
    }

    public List<String> getMethodIds() {
        return methodIds;
    }

    public SELF setMethodIds(List<String> methodIds) {
        this.methodIds = methodIds;
        return self();
    }

    @Override
    public BigDecimal getFees() {
        return fees;
    }

    public SELF setFees(BigDecimal fees) {
        this.fees = fees;
        return self();
    }

    public LocalDateTime getTime() {
        return time;
    }

    public SELF setTime(final LocalDateTime time) {
        this.time = time;
        return self();
    }

    private SELF self() {
        return (SELF) this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AbstractReport.class.getSimpleName() + "[", "]")
                .add("methodIds=" + methodIds)
                .add("transactionHash='" + transactionHash + "'")
                .add("time=" + time)
                .add("fees=" + fees)
                .toString();
    }
}
