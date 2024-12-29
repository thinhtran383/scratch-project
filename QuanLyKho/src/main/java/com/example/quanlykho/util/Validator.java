package com.example.quanlykho.util;

public class Validator {

    public static boolean isNull(Object... o) {
        for (Object obj : o) {
            if (obj == null || obj.toString().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumber(String s) {
        return s.matches("\\d+");
    }
}
