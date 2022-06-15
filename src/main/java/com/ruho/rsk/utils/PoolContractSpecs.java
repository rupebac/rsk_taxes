package com.ruho.rsk.utils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Pool contracts
 */
public enum PoolContractSpecs {
    BTC_XUSD_POOL("0x6f96096687952349dd5944e0eb1be327dcdeb705", TokenContractSpecs.RBTC, TokenContractSpecs.USD),
    BTC_rETH_POOL("0xf41ed702df2b84ace02772c6a0d8ae46465aa5f4", TokenContractSpecs.RBTC, TokenContractSpecs.ETH),
    BTC_BNB_POOL("0x8f3d24ab3510294f1466aa105f78901b90d79d4d", TokenContractSpecs.RBTC, TokenContractSpecs.BNB),
    BTC_SOV_POOL("0x09c5faf7723b13434abdf1a65ab1b667bc02a902", TokenContractSpecs.RBTC, TokenContractSpecs.SOV),
    BTC_MYNT_POOL("0x36263ac99ecdcf1ab20513d580b7d8d32d3c439d", TokenContractSpecs.RBTC, TokenContractSpecs.MYNT),
    BTC_FISH_POOL("0x35a74a38fd7728f1c6bc39ae3b18c974b7979ddd", TokenContractSpecs.RBTC, TokenContractSpecs.FISH),
    BTC_RUSDT_POOL("0x40580e31cc14dbf7a0859f38ab36a84262df821d", TokenContractSpecs.RBTC, TokenContractSpecs.RUSDT),
    BTC_RUSDT_POOL_2("0x9c4017d1c04cfa0f97fdc9505e33a0d8ac84817f", TokenContractSpecs.RBTC, TokenContractSpecs.RUSDT);


    private final TokenContractSpecs quoteSymbol;
    private final String contractAddress;
    private final TokenContractSpecs baseSymbol;

    PoolContractSpecs(String contractAddress, TokenContractSpecs baseSymbol, TokenContractSpecs quoteSymbol) {
        this.contractAddress = contractAddress;
        this.baseSymbol = baseSymbol;
        this.quoteSymbol = quoteSymbol;
    }

    public TokenContractSpecs getQuoteToken() {
        return quoteSymbol;
    }

    public String getQuoteSymbol() {
        return quoteSymbol.getPrintSymbol();
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public TokenContractSpecs getBaseToken() {
        return baseSymbol;
    }

    public String getBaseSymbol() {
        return baseSymbol.getPrintSymbol();
    }

    public static Optional<PoolContractSpecs> findSpecsFromContract(String contractAddress) {
        Objects.requireNonNull(contractAddress, "contractAddress can't be null");
        return Arrays.stream(values())
                .filter(poolContractSpecs -> poolContractSpecs.getContractAddress().equalsIgnoreCase(contractAddress.trim()))
                .findFirst();
    }
}
