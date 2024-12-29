package com.example.quanlykho.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class OrderDto {
    private Integer orderId;
    private Date date;
    private BigDecimal total;
    private String staffName;

    public OrderDto() {
    }

    public OrderDto(Integer orderId, Date date, BigDecimal total, String staffName) {
        this.orderId = orderId;
        this.date = date;
        this.total = total;
        this.staffName = staffName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    @Override
    public String toString() {
        return "ExportDto{" +
                "orderId=" + orderId +
                ", date=" + date +
                ", total=" + total +
                ", staffName='" + staffName + '\'' +
                '}';
    }
}
