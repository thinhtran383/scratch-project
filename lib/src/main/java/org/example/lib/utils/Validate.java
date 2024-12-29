package org.example.lib.utils;

import javafx.scene.control.Alert;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;

public class Validate {

    public static void showValidationError(ValidationResult validationResult) {
        StringBuilder errorMessage = new StringBuilder();
        validationResult.getMessages().forEach(message -> errorMessage.append(message.getText()).append("\n"));

        AlterUtil.alter(errorMessage.toString(), Alert.AlertType.ERROR, "Lỗi", "Vui lòng thực hiện đầy đủ yêu cầu sau:");
    }

    public static boolean showError(ValidationSupport validationSupport) {
        ValidationResult validationResult = validationSupport.getValidationResult();

        if (!validationResult.getMessages().isEmpty()) {
            showValidationError(validationResult);
            return true;
        }
        return false;
    }

}
