package com.studentmanagement.services;

import com.studentmanagement.models.Account;
import com.studentmanagement.utils.DbConnect;
import com.studentmanagement.utils.PasswordUtil;

import java.sql.ResultSet;

public class AccountService {
    private final DbConnect dbConnect;
    public AccountService() {
        dbConnect = DbConnect.getInstance();
    }


    public boolean isExist(String username) { // Check if username is already exist
        String sql = "SELECT * FROM users WHERE username = ?"; // SQL query
        try(ResultSet rs = dbConnect.executeQuery(sql, username)) {
            return rs.next(); // Return true if username is already exist
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean login(Account account) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        String hashedPassword = PasswordUtil.hashPassword(account.getPassword()); // Hash password

        try(ResultSet rs = dbConnect.executeQuery(sql,account.getUsername(), hashedPassword)) {
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean register(Account account) {
        if(isExist(account.getUsername())) {
            return false;
        }

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        String hashedPassword = PasswordUtil.hashPassword(account.getPassword());
        try {
            return dbConnect.executeUpdate(sql, account.getUsername(), hashedPassword) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePassword(Account account, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ? AND password = ?";
        String hashedPassword = PasswordUtil.hashPassword(account.getPassword());
        String hashedNewPassword = PasswordUtil.hashPassword(newPassword);
        try {
            return dbConnect.executeUpdate(sql, hashedNewPassword, account.getUsername(), hashedPassword) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
