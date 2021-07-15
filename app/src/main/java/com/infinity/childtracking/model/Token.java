package com.infinity.childtracking.model;

public class Token {
    String token,username;

    public Token(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public Token(String token) {
        this.token = token;
    }

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
