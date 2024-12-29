package com.studentmanagement.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {
    public static String hashPassword(String password) {
        try {
            // Tạo instance của MessageDigest với thuật toán MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Chuyển đổi mật khẩu thành mảng byte và tính hash
            byte[] hashedBytes = md.digest(password.getBytes());

            // Chuyển đổi mảng byte thành dạng hex
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        String password = "1";
        String hashedPassword = hashPassword(password);
        System.out.println("Original Password: " + password);
        System.out.println("Hashed Password (MD5): " + hashedPassword);
    }

}
