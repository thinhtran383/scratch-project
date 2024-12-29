module com.example.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.compiler;
    requires java.sql;
    requires static lombok;
    requires java.mail;


    opens com.example.library to javafx.fxml;
    opens com.example.library.controller to javafx.fxml;
    opens com.example.library.model to javafx.fxml;
    opens com.example.library.controller.client to javafx.fxml;

    exports com.example.library;
    exports com.example.library.controller;
    exports com.example.library.model;
    exports com.example.library.service;
    exports com.example.library.util;
    exports com.example.library.repository;
    exports com.example.library.controller.client;
    exports com.example.library.repository.impl;
    exports com.example.library.service.impl;
}