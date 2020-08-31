package com.minesweeper.model;

import org.springframework.data.annotation.Id;

public class User {

    @Id
    private String username;

    private String password;

    private String activeToken;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.activeToken = null;
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

    public String getActiveToken() {
        return activeToken;
    }

    public void setActiveToken(String activeToken) {
        this.activeToken = activeToken;
    }

}
