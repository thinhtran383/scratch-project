package com.example.quanlykho.util;

import javafx.scene.control.Alert;

public class Notification {
    public static void alter(String message, Alert.AlertType type, String title) { // warning, infomation, error
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static boolean confirm(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        return alert.getResult().getButtonData().isDefaultButton();
    }
}
