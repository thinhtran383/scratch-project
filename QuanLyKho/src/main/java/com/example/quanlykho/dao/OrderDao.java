package com.example.quanlykho.dao;

import com.example.quanlykho.dto.OrderDetailDao;
import com.example.quanlykho.dto.OrderDto;
import com.example.quanlykho.entity.Order;
import com.example.quanlykho.entity.OrderDetail;

import java.util.List;

public class OrderDao {
    private final GenericDao<Order, Integer> orderDao;
    private final GenericDao<OrderDetail, Integer> orderDetailDao;

    public OrderDao() {
        orderDao = new GenericDao<>(Order.class);
        orderDetailDao = new GenericDao<>(OrderDetail.class);
    }

    public void createOrder(Order order) {
        orderDao.save(order);
    }

    public void createOrderDetail(OrderDetail orderDetail) {
        orderDetailDao.save(orderDetail);
    }

    public void updateOrderDetail(OrderDetail orderDetail) {
        orderDetailDao.save(orderDetail);
    }

    public boolean isDuplicateOrder(Integer orderId) {
        Order order = orderDao.findById(orderId);

        return order != null && order.getType().equals("Export");
    }

    public List<OrderDto> getOrdersByType(String orderType) {
        String sql = """
                    SELECT
                        o.order_id AS orderId,
                        o.order_date AS date,
                        o.total_amount AS total,
                        u.full_name AS staffName
                    FROM
                        Orders o
                    JOIN
                        Users u
                    ON
                        o.user_id = u.id
                    WHERE
                        o.type = ?;
                """;

        return orderDetailDao.executeNativeQueryToDTO(sql, OrderDto.class, orderType);
    }

    public List<OrderDetailDao> getOrderDetailsByOrderId(int orderId) {
        String sql = """
                   SELECT
                       od.product_id AS productId,
                       p.product_name AS productName,
                       od.quantity AS quantity,
                       od.price AS price
                   FROM
                       OrderDetails od
                   LEFT JOIN
                       Products p
                   ON
                       od.product_id = p.id
                   LEFT JOIN
                       Orders o
                   ON
                       od.order_id = o.order_id
                   WHERE
                       o.order_id = ?;
                
                """;

        return orderDetailDao.executeNativeQueryToDTO(sql, OrderDetailDao.class, orderId);
    }


    public static void main(String[] args) {
        OrderDao orderDao = new OrderDao();
        List<OrderDetailDao> exportOrders = orderDao.getOrderDetailsByOrderId(222);
        exportOrders.forEach(System.out::println);
    }


}
