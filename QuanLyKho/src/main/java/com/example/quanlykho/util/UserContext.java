package com.example.quanlykho.util;

public class UserContext {
    private String username;
    private String fullName;
    private Integer userId;

    private static UserContext instance;

    private UserContext() {

    }

    public static UserContext getInstance() {
        if (instance == null) {
            instance = new UserContext();
        }
        return instance;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void clearContext() {
        this.username = null;
        this.fullName = null;
        this.userId = null;
    }
}
