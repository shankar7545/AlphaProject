package com.example.alpha.Model;

public class ReferClass {
    public String uid, username, childCount, enabled;

    public ReferClass() {
    }


    public ReferClass(String uid, String username, String childCount, String enabled) {
        this.uid = uid;
        this.username = username;
        this.childCount = childCount;
        this.enabled = enabled;


    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChildCount() {
        return childCount;
    }

    public void setChildCount(String childCount) {
        this.childCount = childCount;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
}
