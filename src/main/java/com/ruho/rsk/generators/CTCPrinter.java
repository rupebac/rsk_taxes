package com.ruho.rsk.generators;

import com.ruho.rsk.utils.TokenContractSpecs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CTCPrinter {
    public static final String[] HEADERS = new String[] {
            "Timestamp (UTC)", "Type", "Base Currency", "Base Amount", "Quote Currency (Optional)",
            "Quote Amount (Optional)", "Fee Currency (Optional)", "Fee Amount (Optional)",
            "From (Optional)", "To (Optional)", "ID (Optional)", "Description (Optional)"
    };

    public static String[] printToArray(LocalDateTime timestamp,
                                        String type,
                                        String baseCurrency,
                                        BigDecimal baseAmount,
                                        String quoteCurrency,
                                        BigDecimal quoteAmount,
                                        BigDecimal feeAmount,
                                        String from,
                                        String to,
                                        String id,
                                        String description) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        List<String> parts = new ArrayList<>();
        parts.add(formatter.format(timestamp));
        parts.add(type);
        parts.add(baseCurrency);
        parts.add(baseAmount.toPlainString());
        parts.add(quoteCurrency);
        parts.add(quoteAmount == null ? null : quoteAmount.toPlainString());
        parts.add(TokenContractSpecs.RBTC.getPrintSymbol());
        parts.add(feeAmount == null ? null : feeAmount.toPlainString());
        parts.add(from);
        parts.add(to);
        parts.add(id);
        parts.add(description);
        return parts.toArray(new String[0]);
    }
}
