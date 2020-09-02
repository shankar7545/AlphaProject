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
    private String strDate;


    public Transaction_Class(String transactionType, String transactionDate, String transactionTime, String transferredFrom,
                             String transferredTo, String transactionId, String transactionAmount, String strDate,
                             String transactionLevel, String paymentMode) {
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.transferredFrom = transferredFrom;
        this.transferredTo = transferredTo;
        this.transactionId = transactionId;
        this.transactionAmount = transactionAmount;
        this.strDate = strDate;
        this.transactionLevel = transactionLevel;
        this.paymentMode = paymentMode;


    }

    public Transaction_Class() {
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransferredFrom() {
        return transferredFrom;
    }

    public void setTransferredFrom(String transferredFrom) {
        this.transferredFrom = transferredFrom;
    }

    public String getTransferredTo() {
        return transferredTo;
    }

    public void setTransferredTo(String transferredTo) {
        this.transferredTo = transferredTo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionLevel() {
        return transactionLevel;
    }

    public void setTransactionLevel(String transactionLevel) {
        this.transactionLevel = transactionLevel;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }
}
