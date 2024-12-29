package org.example.lib.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator;
import org.example.lib.models.Borrow;
import org.example.lib.services.BookService;
import org.example.lib.services.BorrowService;
import org.example.lib.services.ReaderService;
import org.example.lib.utils.AlterUtil;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class BorrowController implements Initializable {

    public TableView<Borrow> tbLoanRecord;
    public TableColumn<Borrow, LocalDate> colBorrowDate;
    public TableColumn<Borrow, LocalDate> colDueDate;
    public TableColumn<Borrow, LocalDate> colReturnDate;
    public TextField txtKeyword;
    public ComboBox<String> cbReaderId;
    public ComboBox<String> cbBookId;
    public TableColumn<Borrow, String> colReaderId;
    public TableColumn<Borrow, String> colReaderName;
    public TableColumn<Borrow, String> colBookId;
    public TableColumn<Borrow, String> colBookName;
    public TableColumn<Borrow, String> colStatus;
    public TableColumn<Borrow, String> colId;

    private final BorrowService borrowService;
    private final BookService bookService;
    private final ReaderService readerService;
    public ComboBox<String> cbFilter;

    public BorrowController() {
        bookService = new BookService();
        readerService = new ReaderService();
        borrowService = new BorrowService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLoanRecord(borrowService.getAllBorrows());

        cbReaderId.getItems().addAll(readerService.getReaderId());
        cbBookId.getItems().addAll(bookService.getBookId());

        cbFilter.getItems().addAll("Tất cả", "Chưa trả", "Đã trả", "Quá hạn");
        cbFilter.setValue("Tất cả");
    }

    private void loadLoanRecord(List<Borrow> data) {
        ObservableList<Borrow> observableList = FXCollections.observableList(data);

        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colReaderId.setCellValueFactory(new PropertyValueFactory<>("readerId"));
        colReaderName.setCellValueFactory(new PropertyValueFactory<>("readerName"));
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        tbLoanRecord.setItems(observableList);
    }


    public void onTableSelected(MouseEvent mouseEvent) {
        Borrow borrowId = tbLoanRecord.getSelectionModel().getSelectedItem();

        if (borrowId == null) {
            return;
        }

        String reader = borrowId.getReaderId() + " - " + borrowId.getReaderName();
        String book = borrowId.getBookId() + " - " + borrowId.getBookName();

        cbReaderId.setValue(reader);
        cbBookId.setValue(book);
    }

    private void clearForm() {
        cbReaderId.setValue(null);
        cbBookId.setValue(null);


        tbLoanRecord.getSelectionModel().clearSelection();
    }

    private Borrow getBorrowFromForm() {
        Borrow borrow = new Borrow();

        borrow.setBookId(cbBookId.getValue().split(" - ")[0]);
        borrow.setReaderId(cbReaderId.getValue().split(" - ")[0]);
        borrow.setBorrowDate(LocalDate.now());
        borrow.setDueDate(LocalDate.now().plusDays(7));
        borrow.setReturnDate(null);
        borrow.setBookName(cbBookId.getValue().split(" - ")[1]);
        borrow.setReaderName(cbReaderId.getValue().split(" - ")[1]);
        borrow.setStatus("Chưa trả");

        return borrow;
    }

    public void onClickAdd(MouseEvent mouseEvent) {
        Borrow borrow = getBorrowFromForm();

        borrow.setId(borrowService.generateBorrowId());

        try {
            borrowService.borrowBook(borrow);
        } catch (Exception e) {
            AlterUtil.alter(e.getMessage(), Alert.AlertType.ERROR, "Lỗi", null);
        }
        loadLoanRecord(borrowService.getAllBorrows());
        clearForm();

        AlterUtil.alter("Mượn sách thành công", Alert.AlertType.INFORMATION, "Thành công", null);
    }

    public void onClickUpdate(MouseEvent mouseEvent) {
        Borrow selectedBorrow = tbLoanRecord.getSelectionModel().getSelectedItem();

        borrowService.reNewBorrow(selectedBorrow);

        loadLoanRecord(borrowService.getAllBorrows());
        clearForm();
        AlterUtil.alter("Gia hạn thành công", Alert.AlertType.INFORMATION, "Thành công", null);
    }

    public void onClickReturn(MouseEvent mouseEvent) {
        Borrow selectedBorrow = tbLoanRecord.getSelectionModel().getSelectedItem();

        if (selectedBorrow == null) {
            return;
        }

        borrowService.returnBook(selectedBorrow);

        loadLoanRecord(borrowService.getAllBorrows());
        clearForm();
        AlterUtil.alter("Trả sách thành công", Alert.AlertType.INFORMATION, "Thành công", null);
    }

    public void onClickRefresh(MouseEvent mouseEvent) {
        clearForm();
    }

    public void pressKey(KeyEvent keyEvent) {
        String keyword = txtKeyword.getText().trim().toLowerCase();
        List<Borrow> borrows = borrowService.search(keyword);

        loadLoanRecord(borrows);
    }

    public void onChoose(ActionEvent actionEvent) {
        tbLoanRecord.setItems(FXCollections.observableList(borrowService.filterByStatus(cbFilter.getValue())));
    }
}
