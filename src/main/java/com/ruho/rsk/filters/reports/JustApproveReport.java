package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;
import com.ruho.rsk.utils.TokenContractSpecs;

import java.util.StringJoiner;

public class JustApproveReport extends AbstractReport<JustApproveReport> {
    private String tokenApproved;

    public String getTokenApproved() {
        return tokenApproved;
    }

    public JustApproveReport setTokenApproved(String tokenApproved) {
        this.tokenApproved = tokenApproved;
        return this;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.JUST_APPROVE;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JustApproveReport.class.getSimpleName() + "[", "]")
                .add("tokenApproved=" + tokenApproved) + "  " + super.toString();
    }
}
