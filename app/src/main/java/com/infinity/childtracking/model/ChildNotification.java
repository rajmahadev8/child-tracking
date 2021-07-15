package com.infinity.childtracking.model;

import androidx.annotation.NonNull;

public class ChildNotification {
    String pkg,title,text,username;

    public ChildNotification(String pkg, String title, String text) {
        this.pkg = pkg;
        this.title = title;
        this.text = text;
    }

    public ChildNotification(String pkg, String title, String text,String username) {
        this.pkg = pkg;
        this.title = title;
        this.text = text;
        this.username = username;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return "Pkg :"+ pkg
                + "title: "+title
                + "text :"+text;
    }
}
