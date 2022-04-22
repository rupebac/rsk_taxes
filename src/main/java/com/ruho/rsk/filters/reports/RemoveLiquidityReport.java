package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;
import com.ruho.rsk.utils.TokenContractSpecs;

import java.math.BigDecimal;

public class RemoveLiquidityReport extends AbstractReport<RemoveLiquidityReport> {

    private BigDecimal sovsRewards;

    private TokenContractSpecs quotedToken;            // usdt ?
    private BigDecimal quotedAmount;

    private TokenContractSpecs baseToken;              // btc ?
    private BigDecimal baseAmount;

    private BigDecimal poolTokenAmount;


    public BigDecimal getPoolTokenAmount() {
        return poolTokenAmount;
    }

    public RemoveLiquidityReport setPoolTokenAmount(BigDecimal poolTokenAmount) {
        this.poolTokenAmount = poolTokenAmount;
        return this;
    }

    public BigDecimal getSovsRewards() {
        return sovsRewards;
    }

    public RemoveLiquidityReport setSovsRewards(BigDecimal sovsRewards) {
        this.sovsRewards = sovsRewards;
        return this;
    }

    public TokenContractSpecs getQuotedToken() {
        return quotedToken;
    }

    public RemoveLiquidityReport setQuotedToken(TokenContractSpecs quotedToken) {
        this.quotedToken = quotedToken;
        return this;
    }

    public BigDecimal getQuotedAmount() {
        return quotedAmount;
    }

    public RemoveLiquidityReport setQuotedAmount(BigDecimal quotedAmount) {
        this.quotedAmount = quotedAmount;
        return this;
    }

    public TokenContractSpecs getBaseToken() {
        return baseToken;
    }

    public RemoveLiquidityReport setBaseToken(TokenContractSpecs baseToken) {
        this.baseToken = baseToken;
        return this;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public RemoveLiquidityReport setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
        return this;
    }

    public String getPoolTokenSymbol() {
        return this.getBaseToken().getRskSymbol() + "-" + this.getQuotedToken().getRskSymbol() + "-LP";
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.LIQUIDITY_REMOVE;
    }

    @Override
    public String toString() {
        return "RemoveLiquidityReport{" +
                "sovsRewards=" + sovsRewards +
                ", quotedSymbol='" + quotedToken + '\'' +
                ", quotedAmount=" + quotedAmount +
                ", baseSymbol='" + baseToken + '\'' +
                ", baseAmount=" + baseAmount +
                "} " + super.toString();
    }
}
