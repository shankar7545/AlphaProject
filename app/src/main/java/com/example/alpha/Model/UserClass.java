package com.example.alpha.Model;

public class UserClass {
    public String email,name,password,payment,username;

    public UserClass() {
    }

    public UserClass(String email, String name, String password, String payment, String username) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.payment = payment;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
