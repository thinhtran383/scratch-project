package org.example.lib.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.lib.models.Borrow;
import org.example.lib.services.BorrowService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryBorrowController implements Initializable {
    @FXML
    private TableColumn<Borrow, String> colId;
    @FXML
    private TableView<Borrow> tbBorrows;
    @FXML
    private TableColumn<Borrow, String> colBookId;
    @FXML
    private TableColumn<Borrow, String> colReaderId;
    @FXML
    private TableColumn<Borrow, String> colReaderName;
    @FXML
    private TableColumn<Borrow, String> colBookName;
    @FXML
    private TableColumn<Borrow, String> colBorrowDate;
    @FXML
    private TableColumn<Borrow, String> colDueDate;
    @FXML
    private TableColumn<Borrow, String> colReturnDate;
    @FXML
    private TableColumn<Borrow, String> colStatus;

    private static String readerId;

    private final BorrowService borrowService;

    public static void setReaderId(String readerId) {
        HistoryBorrowController.readerId = readerId;
    }

    public HistoryBorrowController() {
        borrowService = new BorrowService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataToTable(borrowService.findBorrowedReaders(readerId));

    }

    private void loadDataToTable(List<Borrow> borrows) {
        ObservableList<Borrow> data = FXCollections.observableList(borrows);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colReaderId.setCellValueFactory(new PropertyValueFactory<>("readerId"));
        colReaderName.setCellValueFactory(new PropertyValueFactory<>("readerName"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tbBorrows.setItems(data);

    }

    public void onClickReturn(ActionEvent actionEvent) {
        Borrow borrow = tbBorrows.getSelectionModel().getSelectedItem();

        if (borrow == null) {
            return;
        }

        borrowService.returnBook(borrow);

        loadDataToTable(borrowService.findBorrowedReaders(readerId));
    }
}
