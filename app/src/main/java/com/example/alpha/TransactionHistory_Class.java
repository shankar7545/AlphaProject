package com.example.alpha;

public class TransactionHistory_Class {
    public String transactionAmount,transactionTime,transactionDate,transactionid,transactionStatus,transactionName;


    public String getTransactionamount() {
        return transactionAmount;
    }

    public void setTransactionamount(String transactionamount) {
        this.transactionAmount = transactionamount;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public TransactionHistory_Class(String transactionAmount, String transactionTime, String transactionDate, String transactionid, String transactionStatus, String transactionName) {
        this.transactionAmount = transactionAmount;
        this.transactionTime = transactionTime;
        this.transactionDate = transactionDate;
        this.transactionid = transactionid;
        this.transactionStatus = transactionStatus;
        this.transactionName = transactionName;
    }

    public TransactionHistory_Class() {
    }
}
