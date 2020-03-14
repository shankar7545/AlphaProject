package com.example.alpha.Model;

public class UserClass {
    public String email, name, password, paymentStatus, username, level, parentStatus, date, childCount, p5, p6, p7, p8;

    public UserClass() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserClass(String email, String name, String password, String paymentStatus, String username, String level, String parentStatus
            , String date, String childCount) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.paymentStatus = paymentStatus;
        this.username = username;
        this.level = level;
        this.parentStatus = parentStatus;
        this.date = date;
        this.childCount = childCount;
    }

    public String getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getParentStatus() {
        return parentStatus;
    }

    public void setParentStatus(String parentStatus) {
        this.parentStatus = parentStatus;
    }

    public String getChildCount() {
        return childCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setChildCount(String childCount) {
        this.childCount = childCount;
    }


}
