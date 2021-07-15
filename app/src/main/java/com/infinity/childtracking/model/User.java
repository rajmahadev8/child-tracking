package com.infinity.childtracking.model;

import androidx.annotation.NonNull;

public class User {
    private String username;
    private String password;
    private String email_mobile;
    private String usertype;
    private String child_parent;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String usertype, String child_parent) {
        this.username = username;
        this.usertype = usertype;
        this.child_parent = child_parent;
    }

    public User(String username, String password, String email_mobile, String usertype, String child_parent) {
        this.username = username;
        this.password = password;
        this.usertype = usertype;
        this.email_mobile = email_mobile;
        this.child_parent = child_parent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsertype() {
        return usertype;
    }

    public String getEmail_mobile() {
        return email_mobile;
    }

    public void setEmail_mobile(String email_mobile) {
        this.email_mobile = email_mobile;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getChild_parent() {
        return child_parent;
    }

    public void setChild_parent(String child_parent) {
        this.child_parent = child_parent;
    }

    @NonNull
    @Override
    public String toString() {
        return "Username :"+username
                + "Usertype :"+usertype
                + "childUser :"+ child_parent
                + "Email_mobile :"+ email_mobile;
    }
}
