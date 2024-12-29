package org.example.lib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.example.lib.App;
import org.example.lib.services.AccountService;
import org.example.lib.utils.AlterUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField pwPassword;

    private final ValidationSupport validationSupport;
    private final AccountService accountService;

    public LoginController() {
        validationSupport = new ValidationSupport();
        accountService = new AccountService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        validationSupport.registerValidator(txtUsername, Validator.createEmptyValidator("Username is required"));
        validationSupport.registerValidator(pwPassword, Validator.createEmptyValidator("Password is required"));

        txtUsername.setText("admin");
        pwPassword.setText("admin");
    }

    public void onClickLogin(ActionEvent actionEvent) throws IOException {
        if (validationSupport.isInvalid()) {
            AlterUtil.alter("vui lòng nhập đầy đủ thông tin!", Alert.AlertType.WARNING, "Cảnh báo", null);
            return;
        }

        boolean isSuccess = accountService.login(txtUsername.getText(), pwPassword.getText());

        if (isSuccess) {
            AlterUtil.alter("Đăng nhập thành công!", Alert.AlertType.INFORMATION, "Đăng nhập", null);
            App.setRoot("DashboardFrm", "DASHBOARD");
        } else {
            AlterUtil.alter("Đăng nhập không thành công! Vui lòng kiểm tra lại.", Alert.AlertType.ERROR, "Đăng nhập",null);
        }
    }
}
