package com.example.library.controller;

import com.example.library.model.Account;
import com.example.library.model.Reader;
import com.example.library.service.impl.AccountServiceImpl;
import com.example.library.service.IAccountService;
import com.example.library.util.Alert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.UUID;

import static com.example.library.common.Regex.isValid;

public class RegisterController {
    @FXML
    private TextField txtFullname;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private TextField txtAddress;
    @FXML
    private DatePicker dpDob;
    @FXML
    private TextField txtTaiKhoan;
    @FXML
    private PasswordField pwMatKhau;
    @FXML
    private PasswordField pwReMatKhau;

    private final IAccountService accountService;

    public RegisterController() {
        accountService = new AccountServiceImpl();
    }

    public void onClickRegister(ActionEvent actionEvent) {
        String username = txtTaiKhoan.getText();
        String password = pwMatKhau.getText();
        String rePassword = pwReMatKhau.getText();

        String fullname = txtFullname.getText();
        String email = txtEmail.getText();
        String phoneNumber = txtPhoneNumber.getText();
        String address = txtAddress.getText();
        dpDob.getValue();
        String dob = dpDob.getValue().toString();


        if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty() ||
                fullname.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || dob.isEmpty()) {
            Alert.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", null, "All fields are required!");
            return;
        }

        if (!password.equals(rePassword)) {
            Alert.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", null, "Password and re-password are not the same!");
            return;
        }

        if (!isValid("EMAIL", email)) {
            Alert.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", null, "Email is invalid!");
            return;
        }

        if (!isValid("PHONE_NUMBER", phoneNumber)) {
            Alert.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", null, "Phone number is invalid!");
            return;
        }

        Account account = Account.builder()
                .username(username)
                .password(password)
                .role("reader")
                .build();

        Reader reader = Reader.builder()
                .readerName(fullname)
                .readerId("R" + UUID.randomUUID().toString().substring(0, 5).toUpperCase())
                .readerAddress(address)
                .readerDOB(LocalDate.parse(dob))
                .readerEmail(email)
                .readerPhone(phoneNumber)
                .build();
        try {
            if (accountService.registerAccount(account, reader)) {
                Alert.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Information", null, "Signup successfully");
            } else {
                Alert.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", null, "Signup failed, username is already taken!");
            }
        } catch (Exception e) {
            Alert.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", null, e.getMessage());
        }
    }
}
