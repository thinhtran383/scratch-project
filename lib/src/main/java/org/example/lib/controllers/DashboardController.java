package org.example.lib.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import org.example.lib.App;
import org.example.lib.models.Borrow;
import org.example.lib.services.BorrowService;
import org.example.lib.utils.UserContex;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private AnchorPane pane;
    @FXML
    private ListView<String> lstMenu;

    public DashboardController() {
        BorrowService borrowService = new BorrowService();

        borrowService.updateIfLate();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lstMenu.getItems().addAll("Quản lý sách", "Quản lý độc giả", "Quản lý mượn trả");
        lstMenu.getSelectionModel().selectFirst();

        loadFXML("BookManagementFrm.fxml");

        lstMenu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "Quản lý sách":
                    loadFXML("BookManagementFrm.fxml");
                    break;
                case "Quản lý độc giả":
                    loadFXML("ReaderManagementFrm.fxml");
                    break;
                case "Quản lý mượn trả":
                    loadFXML("BorrowManagementFrm.fxml");
                    break;
            }
        });
    }

    private void loadFXML(String fxmlFile) {
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/org/example/lib/" + fxmlFile));
            pane.getChildren().setAll(newPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onClickChangePassword(ActionEvent actionEvent) throws IOException {
        App.setRootPop("ChangePassFrm", "Thay đổi mật khẩu", false, 600, 400);
    }

    public void onClickLogout(ActionEvent actionEvent) throws IOException {
        UserContex.getInstance().clearContext();
        App.setRoot("LoginFrm", "ĐĂNG NHẬP");
    }
}
