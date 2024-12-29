package org.example.lib.utils;

import javafx.scene.control.Alert;

public class AlterUtil {
    public static void alter(String message, Alert.AlertType type, String title, String header) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // yes no alert

    public static boolean alterYesNo(String message, String title, String header) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
        return alert.getResult().getButtonData().isDefaultButton();
    }
}
