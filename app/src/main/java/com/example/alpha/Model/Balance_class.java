package com.example.alpha.Model;

public class Balance_class {
    private String bronze, silver, gold, diamond, mainBalance, withdrawable;

    public Balance_class() {
    }

    public Balance_class(String bronze, String silver, String gold, String diamond, String mainBalance, String withdrawable) {
        this.bronze = bronze;
        this.silver = silver;
        this.gold = gold;
        this.diamond = diamond;
        this.mainBalance = mainBalance;
        this.withdrawable = withdrawable;

    }

    public String getBronze() {
        return bronze;
    }

    public String getSilver() {
        return silver;
    }

    public String getGold() {
        return gold;
    }

    public String getDiamond() {
        return diamond;
    }

    public void setBronze(String bronze) {
        this.bronze = bronze;
    }

    public void setSilver(String silver) {
        this.silver = silver;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }


    public String getWithdrawable() {
        return withdrawable;
    }

    public void setWithdrawable(String withdrawable) {
        this.withdrawable = withdrawable;
    }

    public String getMainBalance() {
        return mainBalance;
    }

    public void setMainBalance(String mainBalance) {
        this.mainBalance = mainBalance;
    }
}
