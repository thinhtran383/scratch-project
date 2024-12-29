package org.example.lib.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.example.lib.models.Book;
import org.example.lib.services.BookService;
import org.example.lib.utils.AlterUtil;
import org.example.lib.utils.ExportToExcel;
import org.example.lib.utils.Validate;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class BookController implements Initializable {
    @FXML
    private TextField txtKeyword;
    @FXML
    private TableView<Book> tbBooks;
    @FXML
    private TableColumn<Book, String> colBookId;
    @FXML
    private TableColumn<Book, String> colTitle;
    @FXML
    private TableColumn<Book, String> colAuthor;
    @FXML
    private TableColumn<Book, String> colPublisher;
    @FXML
    private TableColumn<Book, String> colCategory;
    @FXML
    private TableColumn<Book, LocalDate> colPublishDate;
    @FXML
    private TableColumn<Book, Integer> colQuantity;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtPublisher;
    @FXML
    private DatePicker dpPublishDate;
    @FXML
    private TextField txtCategory;
    @FXML
    private TextField txtQuantity;

    private final BookService bookService;
    private final ValidationSupport validationSupport;

    public BookController() {
        bookService = new BookService();
        validationSupport = new ValidationSupport();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTableData(bookService.getAllBook());

        txtId.setText(bookService.generateBookId());

        validationSupport.registerValidator(txtTitle, true, Validator.createEmptyValidator("Tên sách đang bị để trống"));
        validationSupport.registerValidator(txtAuthor, true, Validator.createEmptyValidator("Tác giả đang bị để trống"));
        validationSupport.registerValidator(txtPublisher, true, Validator.createEmptyValidator("Nhà xuất bản đang bị để trống"));
        validationSupport.registerValidator(txtCategory, true, Validator.createEmptyValidator("Thể loại đang bị để trống"));
        validationSupport.registerValidator(dpPublishDate, true, Validator.createEmptyValidator("Ngày xuất bản đang bị để trống"));
        validationSupport.registerValidator(txtQuantity, true, Validator.createEmptyValidator("Số lượng đang bị để trống"));

        validationSupport.registerValidator(txtQuantity, true, Validator.createRegexValidator("Số lượng phải là một số nguyên dương", "\\d+", Severity.ERROR));

        validationSupport.validationResultProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue.getMessages().isEmpty()) {
                txtTitle.setStyle("-fx-border-color: red");
                txtAuthor.setStyle("-fx-border-color: red");
                txtPublisher.setStyle("-fx-border-color: red");
                txtCategory.setStyle("-fx-border-color: red");
                dpPublishDate.setStyle("-fx-border-color: red");
                txtQuantity.setStyle("-fx-border-color: red");
            } else {
                txtTitle.setStyle("-fx-border-color: black");
                txtAuthor.setStyle("-fx-border-color: black");
                txtPublisher.setStyle("-fx-border-color: black");
                txtCategory.setStyle("-fx-border-color: black");
                dpPublishDate.setStyle("-fx-border-color: black");
                txtQuantity.setStyle("-fx-border-color: black");
            }
        });
    }

    private void initTableData(List<Book> data) {
        ObservableList<Book> books = FXCollections.observableArrayList(data);

        colBookId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPublishDate.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        tbBooks.setItems(books);
    }

    public void onTableSelected(MouseEvent mouseEvent) {
        Book selectedBook = tbBooks.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            return;
        }

        txtId.setText(selectedBook.getId());
        txtTitle.setText(selectedBook.getTitle());
        txtAuthor.setText(selectedBook.getAuthor());
        txtPublisher.setText(selectedBook.getPublisher());
        txtCategory.setText(selectedBook.getCategory());
        dpPublishDate.setValue(selectedBook.getPublishedDate());
        txtQuantity.setText(String.valueOf(selectedBook.getQuantity()));

    }

//    private void showValidationError(ValidationResult validationResult) {
//        StringBuilder errorMessage = new StringBuilder();
//        validationResult.getMessages().forEach(message -> errorMessage.append(message.getText()).append("\n"));
//
//        AlterUtil.alter(errorMessage.toString(), Alert.AlertType.ERROR, "Lỗi", "Vui lòng thực hiện đầy đủ yêu cầu sau:");
//    }


    private Book getInputData() {
        String id = txtId.getText();
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        String publisher = txtPublisher.getText();
        String category = txtCategory.getText();
        LocalDate publishedDate = dpPublishDate.getValue();
        int quantity = Integer.parseInt(txtQuantity.getText());


        return new Book(id, title, author, publisher, category, publishedDate, quantity);
    }

//    private boolean showError() {
//        ValidationResult validationResult = validationSupport.getValidationResult();
//
//        if (!validationResult.getMessages().isEmpty()) {
//            showValidationError(validationResult);
//            return true;
//        }
//        return false;
//    }

    private void clear() {
        txtId.setText(bookService.generateBookId());
        txtTitle.clear();
        txtAuthor.clear();
        txtPublisher.clear();
        txtCategory.clear();
        dpPublishDate.setValue(null);
        txtQuantity.clear();
        txtKeyword.clear();
    }

    public void onClickAdd(MouseEvent mouseEvent) {

        if (Validate.showError(validationSupport)) return;

        Book book = getInputData();
        bookService.createNewBook(book);
        initTableData(bookService.getAllBook());

        AlterUtil.alter("Thêm sách thành công", Alert.AlertType.INFORMATION, "Thành công", null);

        clear();
    }


    public void onClickUpdate(MouseEvent mouseEvent) {
        Book book = getInputData();

        if (Validate.showError(validationSupport)) return;

        bookService.updateBook(book);
        initTableData(bookService.getAllBook());

        AlterUtil.alter("Cập nhật sách thành công", Alert.AlertType.INFORMATION, "Thành công", null);

        clear();
    }

    public void onClickDelete(MouseEvent mouseEvent) {
        Book book = tbBooks.getSelectionModel().getSelectedItem();

        if (book == null) {
            AlterUtil.alter("Vui lòng chọn sách cần xóa", Alert.AlertType.ERROR, "Lỗi", null);
            return;
        }

        if (!AlterUtil.alterYesNo("Bạn có chắc chắn muốn xóa sách này không?", "Xác nhận", null)) {
            return;
        }

        bookService.deleteBook(book);
        initTableData(bookService.getAllBook());
        clear();

        AlterUtil.alter("Xóa sách thành công", Alert.AlertType.INFORMATION, "Thành công", null);
    }

    public void onClickRefresh(MouseEvent mouseEvent) {
        clear();
    }

    public void onClickExport(MouseEvent mouseEvent) {
        ExportToExcel.export(tbBooks, new Stage());
        AlterUtil.alter("Xuất file thành công", Alert.AlertType.INFORMATION, "Thành công", null);
    }

    public void pressKey(KeyEvent keyEvent) {
        String keyword = txtKeyword.getText();
        initTableData(bookService.search(keyword));
    }
}
