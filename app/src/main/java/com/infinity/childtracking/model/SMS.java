package com.infinity.childtracking.model;

import androidx.annotation.NonNull;

public class SMS {
    String id, address, body, date, type;
    boolean read;
    String username;

    public SMS(String id, String address, String body, String date, String type, boolean read) {
        this.id = id;
        this.address = address;
        this.body = body;
        this.date = date;
        this.type = type;
        this.read = read;
    }

    public SMS(String id, String address, String body, String date, String type, boolean read,String username) {
        this.id = id;
        this.address = address;
        this.body = body;
        this.date = date;
        this.type = type;
        this.read = read;
        this.username = username;
    }

    @NonNull
    @Override
    public String toString() {
        return "Id: "+id
                + "\naddress: "+address
                + "\nbody: "+body
                + "\ndate: "+date
                + "\ntype: "+type
                + "\nread: "+ read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
