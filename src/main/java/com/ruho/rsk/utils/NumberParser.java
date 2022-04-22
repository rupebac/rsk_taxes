package com.ruho.rsk.utils;

import java.math.BigDecimal;

public class NumberParser {


    public static BigDecimal numberFrom(String stringNumber, int decimals) {
        BigDecimal bigDecimal = new BigDecimal(stringNumber);
        return bigDecimal.divide(BigDecimal.valueOf(10L).pow(decimals));
    }

    public static BigDecimal numberFrom(Object number, Integer decimals) {
        if(number instanceof String) {
            return numberFrom((String) number, (decimals == null || decimals == 0) ? 18 : decimals);
        }
        throw new IllegalArgumentException("wrong type " + number.getClass());
    }

    public static BigDecimal numberFrom(String stringNumber) {
        return numberFrom(stringNumber.replaceAll(",", "").replaceAll("\\.", ""), 18);
    }
}
