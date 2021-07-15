package com.infinity.childtracking.model;

import androidx.annotation.NonNull;

public class CALL {
    private String number,name,duration;
    int type;
    String datetime;
    String username;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public CALL(String number, String name, String duration, int type, String datetime) {
        this.number = number;
        this.name = name;
        this.duration = duration;
        this.type = type;
        this.datetime = datetime;
    }

    public CALL(String number, String name, String duration, int type, String datetime,String username) {
        this.number = number;
        this.name = name;
        this.duration = duration;
        this.type = type;
        this.datetime = datetime;
        this.username = username;
    }

    public CALL() {
    }

    @NonNull
    @Override
    public String toString() {
        return "Name:"+getName() +"\n"
                +"Number:"+getNumber() + "\n"
                + "Duration:"+getDuration()+ "\n"
                + "Type:"+getType() + "\n"
                + "DateTime:"+getDatetime();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
