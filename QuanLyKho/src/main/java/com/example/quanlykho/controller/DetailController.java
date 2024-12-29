package com.example.quanlykho.controller;

import com.example.quanlykho.dao.OrderDao;
import com.example.quanlykho.dto.OrderDetailDao;
import com.example.quanlykho.dto.OrderDto;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DetailController implements Initializable {
    public TableView<OrderDto> tbOrders;
    public TableColumn colOrderId;
    public TableColumn colCreateDate;
    public TableColumn colStaffName;
    public TableColumn colTotal;
    public TableView<OrderDetailDao> tbOrderDetails;
    public TableColumn colProductId;
    public TableColumn colProductName;
    public TableColumn colQuantity;
    public TableColumn colPrice;

    private final OrderDao orderDao;

    private static String type;

    public static void setType(String type) {
        DetailController.type = type;
    }

    public DetailController() {
        orderDao = new OrderDao();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData(orderDao.getOrdersByType(type));
    }

    private void loadData(List<OrderDto> data) {
        colCreateDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colStaffName.setCellValueFactory(new PropertyValueFactory<>("staffName"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tbOrders.getItems().setAll(data);
    }

    private void loadDetail(List<OrderDetailDao> data) {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        tbOrderDetails.getItems().setAll(data);
    }

    public void onSelected(MouseEvent mouseEvent) {
        OrderDto orderDto = tbOrders.getSelectionModel().getSelectedItem();
        List<OrderDetailDao> orderDetails = orderDao.getOrderDetailsByOrderId(orderDto.getOrderId());
        loadDetail(orderDetails);
    }
}
