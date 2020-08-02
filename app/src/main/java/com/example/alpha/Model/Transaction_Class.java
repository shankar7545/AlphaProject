package com.example.alpha.Model;

public class Transaction_Class {
    private String transactionType;
    private String transactionDate;
    private String transactionTime;
    private String transferredFrom;
    private String transferredTo;
    private String transactionId;
    private String transactionAmount;
    private String transactionLevel;
    private String paymentMode;
    private long position;


    public Transaction_Class(String transactionType, String transactionDate, String transactionTime, String transferredFrom,
                             String transferredTo, String transactionId, String transactionAmount, long position,
                             String transactionLevel, String paymentMode) {
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.transferredFrom = transferredFrom;
        this.transferredTo = transferredTo;
        this.transactionId = transactionId;
        this.transactionAmount = transactionAmount;
        this.position = position;
        this.transactionLevel = transactionLevel;
        this.paymentMode = paymentMode;


    }

    public Transaction_Class() {
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public String getTransferredFrom() {
        return transferredFrom;
    }

    public String getTransferredTo() {
        return transferredTo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionLevel() {
        return transactionLevel;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setTransactionLevel(String transactionLevel) {
        this.transactionLevel = transactionLevel;
    }
}
