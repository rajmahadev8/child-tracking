package com.infinity.childtracking.model;

import androidx.annotation.NonNull;

public class USAGE {
    String pkg;
    String lasttime;
    String totaltime;
    String username;

    public USAGE(String pkg, String lasttime, String totaltime, String username) {
        this.pkg = pkg;
        this.lasttime = lasttime;
        this.totaltime = totaltime;
        this.username = username;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getLasttime() {
        return lasttime;
    }

    public void setLasttime(String lasttime) {
        this.lasttime = lasttime;
    }

    public String getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(String totaltime) {
        this.totaltime = totaltime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NonNull
    @Override
    public String toString() {
        return "Pkg "+ pkg
                + "last time "+ lasttime
                + "first time "+totaltime;
    }
}
