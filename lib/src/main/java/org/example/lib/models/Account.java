package org.example.lib.models;

public class Account {
    private String username;
    private String password;

    public Account() {
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return "Account{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static Account mapToAccount(String[] fields) {
        Account account = new Account();
        account.setUsername(fields[0]);
        account.setPassword(fields[1]);
        return account;
    }

    public String mapToString() {
        return username + "," + password;
    }

    public static void main(String[] args) {
        Account account = new Account();
        account.setUsername("admin");
        account.setPassword("admin");


        System.out.println(account);
    }
}
