package com.example.alpha.Model;

public class Transaction_Class {
    public String transactionType,transactionDate ,transactionTime ,transferredFrom ,transferredTo ,transactionId ,transactionAmount;

    public Transaction_Class(String transactionType , String transactionDate , String transactionTime ,String transferredFrom ,String transferredTo ,String transactionId ,String transactionAmount ) {
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionTime = transactionTime;
        this.transferredFrom = transferredFrom;
        this.transferredTo = transferredTo;
        this.transactionId = transactionId;
        this.transactionAmount = transactionAmount;


    }

    public String getTransactionType() {
        return transactionType;
    }
    public String getTransactionDate(){
        return transactionDate;
    }
    public String getTransactionTime(){
        return transactionTime;
    }
    public String getTransferredFrom(){
        return transferredFrom;
    }
    public String getTransferredTo(){
        return transferredTo;
    }
    public String getTransactionId(){
        return transactionId;
    }
    public String getTransactionAmount(){
        return transactionAmount;
    }


    public Transaction_Class() {
    }
}
