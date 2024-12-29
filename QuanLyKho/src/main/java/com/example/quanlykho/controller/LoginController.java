package com.example.quanlykho.controller;

import com.example.quanlykho.App;
import com.example.quanlykho.dao.UserDao;
import com.example.quanlykho.entity.User;
import com.example.quanlykho.util.Notification;
import com.example.quanlykho.util.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    private final UserDao userDao;

    public LoginController() {
        userDao = new UserDao();
    }

    public void onClickLogin(ActionEvent actionEvent) throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (Validator.isNull(username, password)) {
            Notification.alter("Vui lòng nhập đầy đủ thông tin", Alert.AlertType.WARNING, "THÔNG BÁO");
            return;
        }

        if (userDao.login(new User(username, password))) {
            App.setRoot("MainView", "TRANG CHỦ");
        } else {
            Notification.alter("Tài khoản hoặc mật khẩu không đúng", Alert.AlertType.WARNING, "THÔNG BÁO");
        }
    }

    public void onClickCreateAccount(ActionEvent actionEvent) throws IOException {
        App.setRootPop("RegisterView", "ĐĂNG KÝ", false, 600, 400);

    }
}
