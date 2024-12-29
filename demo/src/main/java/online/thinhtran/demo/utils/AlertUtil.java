package online.thinhtran.demo.utils;

import javafx.scene.control.Alert;

public class AlertUtil {

    public static void showAlert(Alert.AlertType type, String title, String content){
        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
