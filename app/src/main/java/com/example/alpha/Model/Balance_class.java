package com.example.alpha.Model;

public class Balance_class {
    private String bronzeBalance, silverBalance, goldBalance, diamondBalance, mainBalance, withdrawable;

    public Balance_class() {
    }

    public Balance_class(String bronzeBalance, String silverBalance, String goldBalance, String diamondBalance, String mainBalance, String withdrawable) {
        this.bronzeBalance = bronzeBalance;
        this.silverBalance = silverBalance;
        this.goldBalance = goldBalance;
        this.diamondBalance = diamondBalance;
        this.mainBalance = mainBalance;
        this.withdrawable = withdrawable;

    }


    public String getBronzeBalance() {
        return bronzeBalance;
    }

    public void setBronzeBalance(String bronzeBalance) {
        this.bronzeBalance = bronzeBalance;
    }

    public String getSilverBalance() {
        return silverBalance;
    }

    public void setSilverBalance(String silverBalance) {
        this.silverBalance = silverBalance;
    }

    public String getGoldBalance() {
        return goldBalance;
    }

    public void setGoldBalance(String goldBalance) {
        this.goldBalance = goldBalance;
    }

    public String getDiamondBalance() {
        return diamondBalance;
    }

    public void setDiamondBalance(String diamondBalance) {
        this.diamondBalance = diamondBalance;
    }

    public String getMainBalance() {
        return mainBalance;
    }

    public void setMainBalance(String mainBalance) {
        this.mainBalance = mainBalance;
    }

    public String getWithdrawable() {
        return withdrawable;
    }

    public void setWithdrawable(String withdrawable) {
        this.withdrawable = withdrawable;
    }
}
