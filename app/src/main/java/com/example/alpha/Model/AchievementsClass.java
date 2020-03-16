package com.example.alpha.Model;

public class AchievementsClass {
    public String beginner, bronze, silver, gold, diamond;

    public AchievementsClass() {
    }


    public AchievementsClass(String beginner, String bronze, String silver, String gold, String diamond) {
        this.beginner = beginner;
        this.bronze = bronze;
        this.silver = silver;
        this.gold = gold;
        this.diamond = diamond;


    }

    public String getBeginner() {
        return beginner;
    }

    public void setBeginner(String beginner) {
        this.beginner = beginner;
    }

    public String getBronze() {
        return bronze;
    }

    public void setBronze(String bronze) {
        this.bronze = bronze;
    }

    public String getSilver() {
        return silver;
    }

    public void setSilver(String silver) {
        this.silver = silver;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public String getDiamond() {
        return diamond;
    }

    public void setDiamond(String diamond) {
        this.diamond = diamond;
    }
}
