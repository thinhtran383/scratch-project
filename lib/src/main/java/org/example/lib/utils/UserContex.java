package org.example.lib.utils;

public class UserContex {
    private static UserContex instance; // mot the hien duy nhat cua class

    private String username;
    private String password;

    private UserContex() {
    }

    public static UserContex getInstance() {
        if (instance == null) {
            instance = new UserContex();
        }
        return instance;
    }

    public void clearContext() {
        this.username = null;
        this.password = null;
    }

    public static void setInstance(UserContex instance) {
        UserContex.instance = instance;
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
}
