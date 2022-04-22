package com.ruho.rsk.filters.reports;

import java.math.BigDecimal;

import com.ruho.rsk.filters.TransactionType;

public class SpotSwapReport extends AbstractReport<SpotSwapReport> {


    private String sourceSymbol;            // usdt ?
    private BigDecimal sourceAmount;

    private String targetSymbol;              // btc ?
    private BigDecimal targetAmount;


    public String getSourceSymbol() {
        return sourceSymbol;
    }

    public SpotSwapReport setSourceSymbol(final String sourceSymbol) {
        this.sourceSymbol = sourceSymbol;
        return this;
    }

    public BigDecimal getSourceAmount() {
        return sourceAmount;
    }

    public SpotSwapReport setSourceAmount(final BigDecimal sourceAmount) {
        this.sourceAmount = sourceAmount;
        return this;
    }

    public String getTargetSymbol() {
        return targetSymbol;
    }

    public SpotSwapReport setTargetSymbol(final String targetSymbol) {
        this.targetSymbol = targetSymbol;
        return this;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public SpotSwapReport setTargetAmount(final BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
        return this;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.SPOT_SWAP;
    }

    @Override
    public String toString() {
        return "SpotSwapReport{" +
                "sourceSymbol='" + sourceSymbol + '\'' +
                ", sourceAmount=" + sourceAmount +
                ", targetSymbol='" + targetSymbol + '\'' +
                ", targetAmount=" + targetAmount +
                "} " + super.toString();
    }
}
