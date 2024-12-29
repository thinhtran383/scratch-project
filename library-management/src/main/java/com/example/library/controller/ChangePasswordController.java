package com.example.library.controller;

import com.example.library.model.Account;
import com.example.library.service.impl.AccountServiceImpl;
import com.example.library.service.IAccountService;
import com.example.library.util.Alert;
import com.example.library.util.UserContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class ChangePasswordController {
    @FXML
    private PasswordField pwCur;
    @FXML
    private PasswordField pwNew;
    @FXML
    private PasswordField pwReEnter;

    private final IAccountService accountService;

    public ChangePasswordController() {
        this.accountService = new AccountServiceImpl();
    }

    public void onClickOk(ActionEvent actionEvent) {
        String oldPassword = pwCur.getText();
        String newPassword = pwNew.getText();
        String reEnterPassword = pwReEnter.getText();

        if (!newPassword.equals(reEnterPassword)) {
            Alert.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", null, "Re-type password not match!");
            return;
        }

        Account account = Account.builder()
                .role(UserContext.getInstance().getRole())
                .password(oldPassword)
                .username(UserContext.getInstance().getUsername())
                .build();

        try {
            accountService.changePassword(account, newPassword);
            Alert.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Information", null, "Change password successfully!");

        } catch (Exception e) {
            Alert.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", null, e.getMessage());

        }
    }
}
