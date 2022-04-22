package com.ruho.rsk.filters.reports;

import com.ruho.rsk.filters.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface AnyReport {
    TransactionType getTransactionType();

    String getTransactionHash();

    BigDecimal getFees();

    List<String> getMethodIds();
}
