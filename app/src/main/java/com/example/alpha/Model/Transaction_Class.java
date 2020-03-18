package com.example.alpha.Model;

public class Transaction_Class {
    public String transactionType;
    public String transactionDate;
    public String transactionTime;
    public String transferredFrom;
    public String transferredTo;
    public String transactionId;
    public String transactionAmount;
    public String transactionLevel;
    public long position;

    public Transaction_Class(String transactionType, String transactionDate, String transactionTime, String transferredFrom,
                             String transferredTo, String transactionId, String transactionAmount, long position,
                             String transactionLevel) {
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.transferredFrom = transferredFrom;
        this.transferredTo = transferredTo;
        this.transactionId = transactionId;
        this.transactionAmount = transactionAmount;
        this.position = position;
        this.transactionLevel = transactionLevel;


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

    public void setTransactionLevel(String transactionLevel) {
        this.transactionLevel = transactionLevel;
    }
}
