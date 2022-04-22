package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;
import com.ruho.rsk.utils.TokenContractSpecs;

import java.math.BigDecimal;

public class AddLiquidityReport extends AbstractReport<AddLiquidityReport> {
    private TokenContractSpecs quotedToken;            // usdt ?
    private BigDecimal quotedAmount;

    private TokenContractSpecs baseToken;              // btc ?
    private BigDecimal baseAmount;

    private BigDecimal poolTokenAmount;


    public BigDecimal getPoolTokenAmount() {
        return poolTokenAmount;
    }

    public AddLiquidityReport setPoolTokenAmount(BigDecimal poolTokenAmount) {
        this.poolTokenAmount = poolTokenAmount;
        return this;
    }

    public TokenContractSpecs getQuotedToken() {
        return quotedToken;
    }

    public AddLiquidityReport setQuotedToken(TokenContractSpecs quotedToken) {
        this.quotedToken = quotedToken;
        return this;
    }

    public BigDecimal getQuotedAmount() {
        return quotedAmount;
    }

    public AddLiquidityReport setQuotedAmount(BigDecimal quotedAmount) {
        this.quotedAmount = quotedAmount;
        return this;
    }

    public TokenContractSpecs getBaseToken() {
        return baseToken;
    }

    public AddLiquidityReport setBaseToken(TokenContractSpecs baseToken) {
        this.baseToken = baseToken;
        return this;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public AddLiquidityReport setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    public String getPoolTokenSymbol() {
        return this.getBaseToken().getRskSymbol() + "-" + this.getQuotedToken().getRskSymbol() + "-LP";
    }

    @Override
    public String toString() {
        return "AddLiquidityReport{" +
                "quotedSymbol='" + quotedToken + '\'' +
                ", quotedAmount=" + quotedAmount +
                ", baseSymbol='" + baseToken + '\'' +
                ", baseAmount=" + baseAmount +
                "} " + super.toString();
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.LIQUIDITY_ADD;
    }

}
