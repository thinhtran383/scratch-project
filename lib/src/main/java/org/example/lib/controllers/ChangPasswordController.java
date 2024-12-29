package org.example.lib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.example.lib.services.AccountService;
import org.example.lib.utils.AlterUtil;
import org.example.lib.utils.UserContex;
import org.example.lib.utils.Validate;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangPasswordController implements Initializable {
    public PasswordField pwCur;
    public PasswordField pwNew;
    public PasswordField pwReEnter;

    private final AccountService accountService;
    private final ValidationSupport validationSupport;

    public ChangPasswordController() {
        accountService = new AccountService();
        validationSupport = new ValidationSupport();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        validationSupport.registerValidator(pwCur, true, Validator.createEmptyValidator("Mật khẩu hiện tại không được để trống!"));
        validationSupport.registerValidator(pwNew, true, Validator.createEmptyValidator("Mật khẩu mới không được để trống!"));
        validationSupport.registerValidator(pwReEnter, true, Validator.createEmptyValidator("Nhập lại mật khẩu không được để trống!"));
    }

    public void onClickOk(ActionEvent actionEvent) {
        String curPassword = pwCur.getText();
        String newPassword = pwNew.getText();
        String reEnterPassword = pwReEnter.getText();

        if (Validate.showError(validationSupport)) {
            return;
        }


        if (!curPassword.equals(UserContex.getInstance().getPassword())) {
            AlterUtil.alter("Mật khẩu hiện tại không đúng!", Alert.AlertType.WARNING, "Lỗi", null);
            return;
        }

        if (!newPassword.equals(reEnterPassword)) {
            AlterUtil.alter("Mật khẩu nhập lại không khớp!", Alert.AlertType.WARNING, "Lỗi", null);
            return;
        }

        accountService.changePassword(newPassword);
        AlterUtil.alter("Đổi mật khẩu thành công!", Alert.AlertType.INFORMATION, "Thông báo", null);

    }

}
