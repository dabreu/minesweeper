package com.minesweeper.security;

public class LoginInfo {

    private String token;

    public LoginInfo() {
    }

    public LoginInfo(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
