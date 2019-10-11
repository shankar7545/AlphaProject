package com.example.alpha.Model;

public class TreeClass {
    public String referid,uid,username,currentuser;

    public TreeClass() {
    }

    public String getReferid() {
        return referid;
    }

    public void setReferid(String referid) {
        this.referid = referid;
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

    public String getCurrentuser() {
        return currentuser;
    }

    public void setCurrentuser(String currentuser) {
        this.currentuser = currentuser;
    }

    public TreeClass(String referid, String uid, String username, String currentuser) {
        this.referid = referid;
        this.uid = uid;
        this.username = username;
        this.currentuser = currentuser;
    }
}
