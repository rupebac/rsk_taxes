package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;

import java.util.StringJoiner;

public class UnknownTransactionReport extends AbstractReport<UnknownTransactionReport>{
    private Integer numberEvents;

    public Integer getNumberEvents() {
        return numberEvents;
    }

    public UnknownTransactionReport setNumberEvents(Integer numberEvents) {
        this.numberEvents = numberEvents;
        return this;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.UNKNOWN;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UnknownTransactionReport.class.getSimpleName() + "[", "]")
                .add("numberEvents=" + numberEvents) + "  " + super.toString();
    }
}
