package com.ruho.rsk.utils;

import java.util.Arrays;

public enum MethodIds {
    CREATE_VESTING_AND_STAKE("0x4558269f"),     // current contract for claiming rewards
    CLAIM_REWARD_FROM_ALL_POOLS("0xd6a49f0e"),   // deprecated contract for claiming rewards,
    ADD_LIQUIDITY_METHOD("0x60dc20cd"),
    SPOT_CONVERT_BY_PATH_2("0xb77d239b"),
    SPOT_CONVERT_BY_PATH("0xb37a4831"),
    SPOT_EXTERNAL_SWAP("0xe321b540"),
    VESTING_WITHDRAW("0x49df728c"),
    REMOVE_LIQUIDITY("0xe94190ed"),
    STAKE_REPORT("0x25629ec0"),
    APPROVE("0x095ea7b3"),
    NEW_BITCOIN_TRANSFER("0xa327aaab")
    ;

    private final String methodId;

    MethodIds(String methodId) {
        this.methodId = methodId;
    }

    public String getMethodId() {
        return methodId;
    }

    public static MethodIds fromMethodId(String methodId){
        return Arrays.stream(MethodIds.values())
                .filter(methodIds ->
                                methodIds.getMethodId().equalsIgnoreCase(methodId)
                )
                .findFirst()
                .orElseThrow(() -> {
                    throw new IllegalStateException("can't find methodId " + methodId);
                });
    }
}
