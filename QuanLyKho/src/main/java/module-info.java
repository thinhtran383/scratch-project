module com.example.quanlykho {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;


    opens com.example.quanlykho to javafx.fxml;
    opens com.example.quanlykho.entity to org.hibernate.orm.core;
    opens com.example.quanlykho.util to org.hibernate.orm.core;
    opens com.example.quanlykho.controller to javafx.fxml;


    exports com.example.quanlykho;
    exports com.example.quanlykho.entity;
    exports com.example.quanlykho.util;
    exports com.example.quanlykho.controller;
    exports com.example.quanlykho.dao;
    exports com.example.quanlykho.dto;
}