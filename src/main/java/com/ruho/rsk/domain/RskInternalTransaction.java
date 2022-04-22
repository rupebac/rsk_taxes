package com.ruho.rsk.domain;

import com.google.common.base.Strings;
import com.ruho.rsk.utils.NumberParser;
import com.ruho.rsk.utils.TokenContractSpecs;

import java.beans.Transient;
import java.math.BigDecimal;

public class RskInternalTransaction {
    private String from;
    private String to;
    private String value;
    private String status;
    private String transaction;
    private String date;
    private String type;
    private String callType;
    private String input;
    private String blockHash;
    private String blockNumber;
    private String gas;
    private String gasUsed;
    private String output;
    private String error;
    private String init;
    private String code;
    private Long timestamp;

    public Long getTimestamp() {
        return timestamp;
    }

    public RskInternalTransaction setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getType() {
        return type;
    }

    public RskInternalTransaction setType(String type) {
        this.type = type;
        return this;
    }

    public String getCallType() {
        return callType;
    }

    public RskInternalTransaction setCallType(String callType) {
        this.callType = callType;
        return this;
    }

    public String getInput() {
        return input;
    }

    public RskInternalTransaction setInput(String input) {
        this.input = input;
        return this;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public RskInternalTransaction setBlockHash(String blockHash) {
        this.blockHash = blockHash;
        return this;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public RskInternalTransaction setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }

    public String getGas() {
        return gas;
    }

    public RskInternalTransaction setGas(String gas) {
        this.gas = gas;
        return this;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public RskInternalTransaction setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
        return this;
    }

    public String getOutput() {
        return output;
    }

    public RskInternalTransaction setOutput(String output) {
        this.output = output;
        return this;
    }

    public String getError() {
        return error;
    }

    public RskInternalTransaction setError(String error) {
        this.error = error;
        return this;
    }

    public String getInit() {
        return init;
    }

    public RskInternalTransaction setInit(String init) {
        this.init = init;
        return this;
    }

    public String getCode() {
        return code;
    }

    public RskInternalTransaction setCode(String code) {
        this.code = code;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public RskInternalTransaction setFrom(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public RskInternalTransaction setTo(String to) {
        this.to = to;
        return this;
    }

    public String getValue() {
        return value;
    }

    public BigDecimal getValueAmount() {
        if(Strings.isNullOrEmpty(this.value)) {
            return BigDecimal.ZERO;
        }
        String[] parts = this.value.split(" ");
        if(parts.length != 2) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(parts[0]);
    }

    public TokenContractSpecs getValueSymbol() {
        if(Strings.isNullOrEmpty(this.value)) {
            return null;
        }
        String[] parts = this.value.split(" ");
        if(parts.length != 2) {
            return null;
        }
        String possibleSymbol = parts[1];
        return TokenContractSpecs.fromRskSymbol(possibleSymbol);
    }

    public RskInternalTransaction setValue(String value) {
        this.value = value;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public RskInternalTransaction setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getTransaction() {
        return transaction;
    }

    public RskInternalTransaction setTransaction(String transaction) {
        this.transaction = transaction;
        return this;
    }

    public String getDate() {
        return date;
    }

    public RskInternalTransaction setDate(String date) {
        this.date = date;
        return this;
    }
}
