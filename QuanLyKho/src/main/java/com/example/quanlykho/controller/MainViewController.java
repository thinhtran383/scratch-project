package com.example.quanlykho.controller;

import com.example.quanlykho.App;
import com.example.quanlykho.dao.OrderDao;
import com.example.quanlykho.dao.ProductDao;
import com.example.quanlykho.entity.Order;
import com.example.quanlykho.entity.OrderDetail;
import com.example.quanlykho.entity.Product;
import com.example.quanlykho.util.Notification;
import com.example.quanlykho.util.UserContext;
import com.example.quanlykho.util.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    public TextField txtIdExport;
    public TextField txtPriceOut;
    public TextField txtNumOut;
    public TextField txtNameOut;
    public Text lbTotal;
    public ComboBox<String> cbType;
    public TextField txtId;
    public TextArea txtNote;
    public TextField txtNum;
    public TextField txtName;
    public TextField txtPrice;
    @FXML
    private TableView<Product> tbProducts;
    @FXML
    private TableColumn<Product, String> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, BigDecimal> priceColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, String> typeColumn;
    @FXML
    private Label lbStaffName;

    private final ProductDao productDao;
    private final OrderDao orderDao;

    public MainViewController() {
        productDao = new ProductDao();
        orderDao = new OrderDao();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lbStaffName.setText(UserContext.getInstance().getFullName());

        loadData(productDao.getAllProducts());

        cbType.getItems().addAll("Đồ uống", "Thực phẩm", "Đồ dùng");
    }

    private void loadData(List<Product> data) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        tbProducts.getItems().setAll(data);
    }

    private Product getDataExport() {
        String id = txtIdExport.getText();
        String name = txtNameOut.getText();
        String price = txtPriceOut.getText();
        String quantity = txtNumOut.getText();

        if (Validator.isNull(id, name, price, quantity)) {
            Notification.alter("Vui lòng nhập đầy đủ thông tin", Alert.AlertType.WARNING, "THÔNG BÁO");
            return null;
        }

        if (!Validator.isNumber(txtNumOut.getText())) {
            Notification.alter("Vui lòng nhập số", Alert.AlertType.WARNING, "THÔNG BÁO");
            return null;
        }


        Product product = new Product();
        product.setId(id);
        product.setProductName(name);
        product.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
        product.setQuantity(Integer.parseInt(quantity));

        return product;
    }

    private Product getDataImport() {
        String id = txtId.getText();
        String name = txtName.getText();
        String type = cbType.getValue();
        String price = txtPrice.getText();
        String quantity = txtNum.getText();

        if (Validator.isNull(id, name, price, quantity, type)) {
            Notification.alter("Vui lòng nhập đầy đủ thông tin", Alert.AlertType.WARNING, "THÔNG BÁO");
            return null;
        }

        if (!Validator.isNumber(txtNum.getText())) {
            Notification.alter("Vui lòng nhập số", Alert.AlertType.WARNING, "THÔNG BÁO");
            return null;
        }


        Product product = new Product();
        product.setId(id);
        product.setProductName(name);
        product.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
        product.setQuantity(Integer.parseInt(quantity));
        product.setCategory(type);


        return product;
    }

    public void onClickLogout(ActionEvent actionEvent) throws IOException {
        App.setRoot("LoginView", "Đăng nhập");
    }

    public void onClickExport(ActionEvent actionEvent) {
        Product product = getDataExport();

        if (product == null) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nhập mã xuất");
        dialog.setContentText("Mã xuất:");

        String exportCode = dialog.showAndWait().orElse(null);

        if (exportCode == null) {
            return;
        }


        int currentQuantity = tbProducts.getSelectionModel().getSelectedItem().getQuantity();
        int quantityOut = Integer.parseInt(txtNumOut.getText());

        if (currentQuantity <= quantityOut) {
            Notification.alter("Số lượng hàng trong kho không đủ", Alert.AlertType.WARNING, "THÔNG BÁO");
            return;
        }

        Order order = new Order();
        order.setId(Integer.parseInt(exportCode));
        order.setType("Export");
        order.setUser(UserContext.getInstance().getUserId());

        orderDao.createOrder(order);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(Integer.parseInt(exportCode));
        orderDetail.setProductId(product.getId());
        orderDetail.setQuantity(quantityOut);
        orderDetail.setPrice(product.getPrice());

        orderDao.updateOrderDetail(orderDetail);

        loadData(productDao.getAllProducts());

        Notification.alter("Xuất hàng thành công", Alert.AlertType.INFORMATION, "THÔNG BÁO");

    }

    public void onSelected(MouseEvent mouseEvent) {
        Product product = tbProducts.getSelectionModel().getSelectedItem();

        txtIdExport.setText(product.getId());
        txtNameOut.setText(product.getProductName());
        txtPriceOut.setText(product.getPrice().toString());
        txtNumOut.setText(product.getQuantity().toString());

        txtId.setText(product.getId());
        txtName.setText(product.getProductName());
        txtPrice.setText(product.getPrice().toString());
        txtNum.setText(product.getQuantity().toString());
        cbType.setValue(product.getCategory());
    }

    public void onTyped(KeyEvent keyEvent) {
        String quantity = txtNumOut.getText();

        if (quantity.isEmpty()) {
            lbTotal.setText("0");
            return;
        }

        if (!Validator.isNumber(quantity)) {
            Notification.alter("Vui lòng nhập số", Alert.AlertType.WARNING, "THÔNG BÁO");
            return;
        }

        BigDecimal price = new BigDecimal(txtPriceOut.getText());
        BigDecimal total = price.multiply(new BigDecimal(quantity));

        lbTotal.setText(total.toString());


    }

    public void onClickImport(ActionEvent actionEvent) {
        Product product = getDataImport();

        if (product == null) {
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nhập mã nhập");
        dialog.setContentText("Mã nhập:");

        String importCode = dialog.showAndWait().orElse(null);

        if (importCode == null) {
            return;
        }

        if (orderDao.isDuplicateOrder(Integer.parseInt(importCode))) {
            Notification.alter("Mã này đã được xử dụng", Alert.AlertType.WARNING, "THÔNG BÁO");
            return;
        }


        if (!productDao.isExistedProduct(product.getId())) {
            productDao.saveProduct(product);
        }


        Order order = new Order();
        order.setId(Integer.parseInt(importCode));
        order.setType("Import");
        order.setNote(txtNote.getText());
        order.setUser(UserContext.getInstance().getUserId());

        orderDao.createOrder(order);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(Integer.parseInt(importCode));
        orderDetail.setProductId(product.getId());
        orderDetail.setQuantity(product.getQuantity());
        orderDetail.setPrice(product.getPrice());

        orderDao.updateOrderDetail(orderDetail);

        loadData(productDao.getAllProducts());

        Notification.alter("Nhập hàng thành công", Alert.AlertType.INFORMATION, "THÔNG BÁO");
    }

    public void onClickShowImport(ActionEvent actionEvent) throws IOException {
        DetailController.setType("Import");

        App.setRootPop("DetailView", "Nhập hàng", false, 796, 455);
    }

    public void onClickShowExport(ActionEvent actionEvent) throws IOException {
        DetailController.setType("Export");

        App.setRootPop("DetailView", "Xuất hàng", false, 796, 455);
    }

    public void onClickReset(ActionEvent actionEvent) {
        txtId.clear();
        txtName.clear();
        txtPrice.clear();
        txtNum.clear();
        txtNote.clear();

        txtIdExport.clear();
        txtNameOut.clear();
        txtPriceOut.clear();
        txtNumOut.clear();
        lbTotal.setText("0");

        tbProducts.getSelectionModel().clearSelection();

    }

    public void onClickUpdate(ActionEvent actionEvent) {
        Product product = getDataImport();

        if (product == null) {
            Notification.alter("Vui lòng chọn sản phẩm cần cập nhật", Alert.AlertType.WARNING, "THÔNG BÁO");
            return;
        }

        productDao.updateProduct(product);

        Notification.alter("Cập nhật thành công", Alert.AlertType.INFORMATION, "THÔNG BÁO");

        loadData(productDao.getAllProducts());
    }

    public void onClickDelete(ActionEvent actionEvent) {
        Product product = tbProducts.getSelectionModel().getSelectedItem();

        if (product == null) {
            Notification.alter("Vui lòng chọn sản phẩm cần xóa", Alert.AlertType.WARNING, "THÔNG BÁO");
            return;
        }

        if (!Notification.confirm("Bạn có chắc chắn muốn xóa sản phẩm này?", "Xác nhận xóa")) {
            return;
        }

        productDao.deleteProduct(product);

        Notification.alter("Xóa thành công", Alert.AlertType.INFORMATION, "THÔNG BÁO");

        loadData(productDao.getAllProducts());
    }
}
