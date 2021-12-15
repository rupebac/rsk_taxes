package com.ruho.rsk.domain;

import com.google.gson.annotations.SerializedName;
import com.ruho.rsk.utils.NumberParser;

import java.beans.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RskItem  implements Serializable {

    private static final long serialVersionUID = -6524986028707016895L;

    @SerializedName("block_signed_at")
    private Date blockSignedAt;

    @SerializedName("block_height")
    private Long blockHeight;

    @SerializedName("tx_hash")
    private String transactionHash;

    @SerializedName("tx_offset")
    private Long transactionOffset;

    private Boolean successful;

    @SerializedName("from_address")
    private String fromAddress;

    @SerializedName("from_address_label")
    private String fromAddressLabel;

    @SerializedName("to_address")
    private String toAddress;

    @SerializedName("to_address_label")
    private String toAddressLabel;

    private String value;

    //BigDecimal?
    @SerializedName("value_quote")
    private String valueQuote;

    // BigDecimal
    @SerializedName("gas_offered")
    private String gasOffered;

    // BigDecimal
    @SerializedName("gas_spent")
    private String gasSpent;

    // BigDecimal
    @SerializedName("gas_price")
    private String gasPrice;

    // BigDecimal
    @SerializedName("gas_quote")
    private String gasQuote;

    // BigDecimal
    @SerializedName("gas_quote_rate")
    private String gasQuoteRate;

    @SerializedName("log_events")
    private List<RskLogEvent> logEvents;

    public List<RskLogEvent> getLogEvents() {
        return logEvents;
    }

    public void setLogEvents(final List<RskLogEvent> logEvents) {
        this.logEvents = logEvents;
    }

    public Date getBlockSignedAt() {
        return blockSignedAt;
    }

    public void setBlockSignedAt(final Date blockSignedAt) {
        this.blockSignedAt = blockSignedAt;
    }

    public Long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(final Long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(final String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public Long getTransactionOffset() {
        return transactionOffset;
    }

    public void setTransactionOffset(final Long transactionOffset) {
        this.transactionOffset = transactionOffset;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(final Boolean successful) {
        this.successful = successful;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(final String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getFromAddressLabel() {
        return fromAddressLabel;
    }

    public void setFromAddressLabel(final String fromAddressLabel) {
        this.fromAddressLabel = fromAddressLabel;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(final String toAddress) {
        this.toAddress = toAddress;
    }

    public String getToAddressLabel() {
        return toAddressLabel;
    }

    public void setToAddressLabel(final String toAddressLabel) {
        this.toAddressLabel = toAddressLabel;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public String getValueQuote() {
        return valueQuote;
    }

    public void setValueQuote(final String valueQuote) {
        this.valueQuote = valueQuote;
    }

    public String getGasOffered() {
        return gasOffered;
    }

    public void setGasOffered(final String gasOffered) {
        this.gasOffered = gasOffered;
    }

    public String getGasSpent() {
        return gasSpent;
    }

    public void setGasSpent(final String gasSpent) {
        this.gasSpent = gasSpent;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(final String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getGasQuote() {
        return gasQuote;
    }

    public void setGasQuote(final String gasQuote) {
        this.gasQuote = gasQuote;
    }

    public String getGasQuoteRate() {
        return gasQuoteRate;
    }

    public void setGasQuoteRate(final String gasQuoteRate) {
        this.gasQuoteRate = gasQuoteRate;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RskItem rskItem = (RskItem) o;
        return Objects.equals(blockSignedAt, rskItem.blockSignedAt)
                && Objects.equals(blockHeight, rskItem.blockHeight)
                && Objects.equals(transactionHash, rskItem.transactionHash)
                && Objects.equals(transactionOffset, rskItem.transactionOffset)
                && Objects.equals(successful, rskItem.successful)
                && Objects.equals(fromAddress, rskItem.fromAddress)
                && Objects.equals(fromAddressLabel, rskItem.fromAddressLabel)
                && Objects.equals(toAddress, rskItem.toAddress)
                && Objects.equals(toAddressLabel, rskItem.toAddressLabel)
                && Objects.equals(value, rskItem.value)
                && Objects.equals(valueQuote, rskItem.valueQuote)
                && Objects.equals(gasOffered, rskItem.gasOffered)
                && Objects.equals(gasSpent, rskItem.gasSpent)
                && Objects.equals(gasPrice, rskItem.gasPrice)
                && Objects.equals(gasQuote, rskItem.gasQuote)
                && Objects.equals(gasQuoteRate, rskItem.gasQuoteRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockSignedAt, blockHeight, transactionHash, transactionOffset, successful, fromAddress, fromAddressLabel,
                            toAddress, toAddressLabel, value, valueQuote, gasOffered, gasSpent, gasPrice, gasQuote, gasQuoteRate);
    }


    @Transient
    public BigDecimal getTotalFees() {
        return NumberParser.numberFrom(this.getGasSpent()).multiply(NumberParser.numberFrom(this.getGasPrice()));
    }
}
