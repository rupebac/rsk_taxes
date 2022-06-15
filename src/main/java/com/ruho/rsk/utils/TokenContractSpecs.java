package com.ruho.rsk.utils;

import java.util.Arrays;
import java.util.Objects;

/**
 * Stores all tokens contract
 */
public enum TokenContractSpecs {
    SOV("0xefc78fc7d48b64958315949279ba181c2114abbd", "SOV", "SOV", "SOV"),
    MYNT("0x2e6b1d146064613e8f521eb3c6e65070af964ebb", "MYNT", "MYNT", "MYNT"),
    RBTC("0x542fda317318ebf1d3deaf76e0b632741a7e677d", "BTC", "RBTC", "BTC"),
    USD("0xb5999795be0ebb5bab23144aa5fd6a02d080299f", "USD", "XUSD", "USDC"),
    FISH("0x055a902303746382fbb7d18f6ae0df56efdc5213", "FISH", "FISH", "FISH"),
    BNB("0x6d9659bdf5b1a1da217f7bbaf7dbaf8190e2e71b", "BNB", "BNB", "BNB"),
    ETH("0x1d931bf8656d795e50ef6d639562c5bd8ac2b78f", "ETH", "ETH", "ETH"),
    RUSDT("0xef213441a85df4d7acbdae0cf78004e1e486bb96", "rUSDT", "rUSDT", "USDT"),
    ;

    private final String address;

    // typically the same as rskSymbol, but we may not call it RBTC but BTC (or USD instead of XUSD), so that oracles and tax software can index the price.
    private final String printSymbol;

    // real symbol name inside RSK
    private final String rskSymbol;

    // symbol outside RSK
    private final String outsideSymbol;

    TokenContractSpecs(String address, String printSymbol, String rskSymbol, String outsideSymbol) {
        this.address = address;
        this.printSymbol = printSymbol;
        this.rskSymbol = rskSymbol;
        this.outsideSymbol = outsideSymbol;
    }


    public String getAddress() {
        return address;
    }

    public String getPrintSymbol() {
        return printSymbol;
    }

    public String getOutsideSymbol() {
        return outsideSymbol;
    }

    public String getRskSymbol() {
        return rskSymbol;
    }


    public static TokenContractSpecs fromRskSymbol(String possibleSymbol) {
        Objects.requireNonNull(possibleSymbol, "possibleSymbol can't be null here");
        return Arrays.stream(TokenContractSpecs.values())
                .filter(tokenContractSpecs ->
                                tokenContractSpecs.getRskSymbol().equalsIgnoreCase(possibleSymbol)
                )
                .findFirst()
                .orElseThrow(()-> new IllegalStateException("can't find tokenContract for: " + possibleSymbol));
    }

    public static TokenContractSpecs fromTokenAddress(String address) {
        Objects.requireNonNull(address, "address can't be null here");
        return Arrays.stream(values())
                .filter(tokenContractSpecs -> tokenContractSpecs.getAddress().equalsIgnoreCase(address.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find TokenContract for address: " + address));
    }
}
