package online.thinhtran.demo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import online.thinhtran.demo.App;
import online.thinhtran.demo.models.Account;
import online.thinhtran.demo.services.AccountService;
import online.thinhtran.demo.utils.AlertUtil;

import java.io.IOException;

public class LoginController {
    @FXML // binding du lieu
    private TextField txtUsername;
    @FXML
    private PasswordField pwPassword;


    private final AccountService accountService;

    public LoginController() {
        this.accountService = new AccountService();
    }

    public void onClickLogin(ActionEvent actionEvent) throws IOException {
        String username = txtUsername.getText();
        String password = pwPassword.getText();

        // valid data
        if(username.isEmpty() || password.isEmpty()){
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Canh bao", "Tai khoan hoac mat khau khong chinh xac");
            return;
        }


        Account loginAccount = new Account(username, password);

        if(accountService.login(loginAccount)){
            System.out.println("success");

            App.setRootPopup("DashboardView", "dang nhap thanh cong", 400, 400);

        } else {
            System.out.println("false");

            AlertUtil.showAlert(Alert.AlertType.ERROR, "Loi", "Tai khoan hoac mat khau khong chinh xac");

        }
    }
}
