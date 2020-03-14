package com.example.alpha.Model;

public class Wallet_Class {
    public String balance, transaction, paid, username, amountwon, kill, withdrawable;

    public Wallet_Class() {
    }

    public Wallet_Class(String balance, String transaction, String paid, String username, String amountwon, String kill, String withdrawable) {
        this.balance = balance;
        this.transaction = transaction;
        this.paid = paid;
        this.username = username;
        this.amountwon = amountwon;
        this.kill = kill;
        this.withdrawable = withdrawable;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAmountwon() {
        return amountwon;
    }

    public void setAmountwon(String amountwon) {
        this.amountwon = amountwon;
    }

    public String getKill() {
        return kill;
    }

    public void setKill(String kill) {
        this.kill = kill;
    }

    public String getwithdrawable() {
        return withdrawable;
    }

    public void setwithdrawable(String withdrawable) {
        this.withdrawable = withdrawable;
    }


}
