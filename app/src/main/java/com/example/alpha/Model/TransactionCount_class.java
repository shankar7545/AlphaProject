package com.example.alpha.Model;

public class TransactionCount_class {
    public String bronzeCount, silverCount, goldCount, diamondCount, level5, level6, level7, level8;

    public TransactionCount_class() {
    }


    public TransactionCount_class(String bronzeCount, String silverCount, String goldCount, String diamondCount) {
        this.bronzeCount = bronzeCount;
        this.silverCount = silverCount;
        this.goldCount = goldCount;
        this.diamondCount = diamondCount;


    }

    public String getBronzeCount() {
        return bronzeCount;
    }

    public void setBronzeCount(String bronzeCount) {
        this.bronzeCount = bronzeCount;
    }

    public String getSilverCount() {
        return silverCount;
    }

    public void setSilverCount(String silverCount) {
        this.silverCount = silverCount;
    }

    public String getGoldCount() {
        return goldCount;
    }

    public void setGoldCount(String goldCount) {
        this.goldCount = goldCount;
    }

    public String getDiamondCount() {
        return diamondCount;
    }

    public void setDiamondCount(String diamondCount) {
        this.diamondCount = diamondCount;
    }

    public String getLevel5() {
        return level5;
    }

    public void setLevel5(String level5) {
        this.level5 = level5;
    }

    public String getLevel6() {
        return level6;
    }

    public void setLevel6(String level6) {
        this.level6 = level6;
    }

    public String getLevel7() {
        return level7;
    }

    public void setLevel7(String level7) {
        this.level7 = level7;
    }

    public String getLevel8() {
        return level8;
    }

    public void setLevel8(String level8) {
        this.level8 = level8;
    }
}
