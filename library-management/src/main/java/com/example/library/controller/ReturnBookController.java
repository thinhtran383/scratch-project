package com.example.library.controller;

import com.example.library.model.Borrow;
import com.example.library.service.IBorrowService;
import com.example.library.service.impl.BorrowServiceImpl;
import com.example.library.util.Alert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ReturnBookController {
    @FXML
    private TextField txtBookId;
    @FXML
    private TextField txtReaderId;

    private final IBorrowService borrowService;

    public ReturnBookController() {
        this.borrowService = new BorrowServiceImpl();
    }

    public void onClickOk(ActionEvent actionEvent) {
        String bookId = txtBookId.getText();
        String readerId = txtReaderId.getText();

        if (bookId.isEmpty() || readerId.isEmpty()) {
            Alert.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", null, "Please fill all fields");
            return;
        }

        Borrow borrow = Borrow.builder()
                .bookId(bookId)
                .readerId(readerId)
                .build();

        try {
            borrowService.returnBook(borrow);
        } catch (Exception e) {
            Alert.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", null, e.getMessage());
            return;
        }

        Alert.showAlert(javafx.scene.control.Alert.AlertType.INFORMATION, "Information", null, "Return book successfully");


    }
}
