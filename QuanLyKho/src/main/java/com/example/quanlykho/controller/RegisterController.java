package com.example.quanlykho.controller;

import com.example.quanlykho.dao.UserDao;
import com.example.quanlykho.entity.User;
import com.example.quanlykho.util.Notification;
import com.example.quanlykho.util.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtFullName;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtRePassword;

    private final UserDao userDao;

    public RegisterController() {
        userDao = new UserDao();
    }

    public void onClickRegister(ActionEvent actionEvent) {
        String fullName = txtFullName.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String rePassword = txtRePassword.getText();

        if (Validator.isNull(fullName, username, password, rePassword)) {
            Notification.alter("Vui lòng nhập đầy đủ thông tin", Alert.AlertType.WARNING, "THÔNG BÁO");
            return;
        }


        if (!password.equals(rePassword)) {
            Notification.alter("Mật khẩu không trùng khớp", Alert.AlertType.WARNING, "THÔNG BÁO");
            return;
        }

        User user = new User(username, password, fullName);

        if (userDao.register(user)) {
            Notification.alter("Đăng ký thành công, bạn có thể đăng nhập", Alert.AlertType.INFORMATION, "THÔNG BÁO");
        } else {
            Notification.alter("Tài khoản đã tồn tại", Alert.AlertType.WARNING, "THÔNG BÁO");
        }
    }
}
