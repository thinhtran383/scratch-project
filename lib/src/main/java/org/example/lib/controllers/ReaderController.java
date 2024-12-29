package org.example.lib.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import org.example.lib.App;
import org.example.lib.models.Reader;
import org.example.lib.services.BookService;
import org.example.lib.services.ReaderService;
import org.example.lib.utils.AlterUtil;
import org.example.lib.utils.ExportToExcel;
import org.example.lib.utils.Validate;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ReaderController implements Initializable {
    public TableView<Reader> tbReaders;
    public TableColumn<String, Reader> colId;
    public TableColumn<String, Reader> colAddress;
    public TableColumn<String, LocalDate> colDob;
    public TableColumn<String, Reader> colFullname;
    public TableColumn<String, Reader> colEmail;
    public TableColumn<String, Reader> colPhoneNumber;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtFullname;
    @FXML
    private TextField txtAddress;
    @FXML
    private DatePicker dpDob;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private TextField txtKeyword;


    private final ReaderService readerService;
    private final ValidationSupport validationSupport;

    public ReaderController() {
        readerService = new ReaderService();
        validationSupport = new ValidationSupport();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataToTable(readerService.getAllReaders());

        txtId.setText(readerService.generateReaderId());

        validationSupport.registerValidator(txtId, Validator.createEmptyValidator("ID không được để trống"));
        validationSupport.registerValidator(txtFullname, Validator.createEmptyValidator("Tên độc giả không được để trống"));
        validationSupport.registerValidator(txtAddress, Validator.createEmptyValidator("Địa chỉ không được để trống"));
        validationSupport.registerValidator(txtPhoneNumber, Validator.createEmptyValidator("Số điện thoại không được để trống"));
        validationSupport.registerValidator(txtEmail, Validator.createEmptyValidator("Email không được để trống"));
        validationSupport.registerValidator(dpDob, Validator.createEmptyValidator("Ngày sinh không được để trống"));

        validationSupport.registerValidator(txtPhoneNumber, Validator.createRegexValidator("Số điện thoại không hợp lệ", "\\d{10,11}", Severity.ERROR));
        validationSupport.registerValidator(txtEmail, Validator.createRegexValidator("Email không hợp lệ", "\\w+@\\w+\\.\\w+", Severity.ERROR));


        validationSupport.validationResultProperty().addListener((o, oldValue, newValue) -> {
            if (!newValue.getMessages().isEmpty()) {
                txtId.setStyle("-fx-border-color: red");
                txtFullname.setStyle("-fx-border-color: red");
                txtAddress.setStyle("-fx-border-color: red");
                txtEmail.setStyle("-fx-border-color: red");
                txtPhoneNumber.setStyle("-fx-border-color: red");
                dpDob.setStyle("-fx-border-color: red");
            } else {
                txtId.setStyle("-fx-border-color: black");
                txtFullname.setStyle("-fx-border-color: black");
                txtAddress.setStyle("-fx-border-color: black");
                txtEmail.setStyle("-fx-border-color: black");
                txtPhoneNumber.setStyle("-fx-border-color: black");
                dpDob.setStyle("-fx-border-color: black");
            }
        });
    }


    private void loadDataToTable(List<Reader> data) {
        ObservableList<Reader> observableList = FXCollections.observableList(data);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFullname.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        tbReaders.setItems(observableList);
    }

    private void clearForm() {
        txtId.clear();
        txtFullname.clear();
        txtAddress.clear();
        txtEmail.clear();
        txtPhoneNumber.clear();
        dpDob.setValue(null);
    }


    public void onTableSelected(MouseEvent mouseEvent) {
        Reader selectedReader = tbReaders.getSelectionModel().getSelectedItem();

        if (selectedReader != null) {
            txtId.setText(selectedReader.getId());
            txtFullname.setText(selectedReader.getFullName());
            txtAddress.setText(selectedReader.getAddress());
            dpDob.setValue(selectedReader.getDob());
            txtEmail.setText(selectedReader.getEmail());
            txtPhoneNumber.setText(selectedReader.getPhoneNumber());
        }

    }

    private Reader getReaderFromForm() {
        return new Reader(txtId.getText(), txtFullname.getText(), txtAddress.getText(), txtPhoneNumber.getText(), txtEmail.getText(), dpDob.getValue());
    }

    public void onClickAdd(MouseEvent mouseEvent) {
        if (Validate.showError(validationSupport)) return;

        Reader reader = getReaderFromForm();

        try {
            readerService.createNewReader(reader);
        } catch (IllegalArgumentException e) {
            AlterUtil.alter(e.getMessage(), Alert.AlertType.ERROR, "Lỗi", "Lỗi");
            return;
        }
        loadDataToTable(readerService.getAllReaders());
        AlterUtil.alter("Thêm độc giả thành công", Alert.AlertType.INFORMATION, "Thông báo", "Thông báo");

        clearForm();

    }

    public void onClickUpdate(MouseEvent mouseEvent) {
        if (Validate.showError(validationSupport)) return;

        Reader reader = getReaderFromForm();

        try {
            readerService.updateReader(reader);
        } catch (IllegalArgumentException e) {
            AlterUtil.alter(e.getMessage(), Alert.AlertType.ERROR, "Lỗi", "Lỗi");
            return;
        }
        loadDataToTable(readerService.getAllReaders());
        AlterUtil.alter("Cập nhật độc giả thành công", Alert.AlertType.INFORMATION, "Thông báo", "Thông báo");

        clearForm();
    }

    public void onClickDelete(MouseEvent mouseEvent) {
        Reader reader = tbReaders.getSelectionModel().getSelectedItem();
        if (reader == null) {
            AlterUtil.alter("Vui lòng chọn độc giả cần xóa", Alert.AlertType.ERROR, "Lỗi", "Lỗi");
            return;
        }

        if (!AlterUtil.alterYesNo("Bạn có chắc chắn muốn độc giả này không?", "Xác nhận", null)) {
            return;
        }

        readerService.deleteReader(reader);
        loadDataToTable(readerService.getAllReaders());
        AlterUtil.alter("Xóa độc giả thành công", Alert.AlertType.INFORMATION, "Thông báo", "Thông báo");

        clearForm();
    }

    public void onClickRefresh(MouseEvent mouseEvent) {
        clearForm();
        txtId.setText(readerService.generateReaderId());
    }

    public void onClickExport(MouseEvent mouseEvent) {
        ExportToExcel.export(tbReaders, new Stage());
        AlterUtil.alter("Xuất file thành công", Alert.AlertType.INFORMATION, "Thông báo", "Thông báo");
    }

    public void pressKey(KeyEvent keyEvent) {
        String keyword = txtKeyword.getText();
        loadDataToTable(readerService.search(keyword));
    }

    public void onClickHistory(ActionEvent actionEvent) throws IOException {
        Reader reader = tbReaders.getSelectionModel().getSelectedItem();
        if (reader == null) {
            AlterUtil.alter("Vui lòng chọn độc giả cần xem lịch sử", Alert.AlertType.ERROR, "Lỗi", "Lỗi");
            return;
        }

        HistoryBorrowController.setReaderId(reader.getId());
        App.setRootPop("HistoryBorrowFrm", "Lịch sử mượn trả", false, 950, 581);
    }
}
