module online.thinhtran.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires commons.dbutils;


    opens online.thinhtran.demo to javafx.fxml;
    opens online.thinhtran.demo.controllers to javafx.fxml;
    opens online.thinhtran.demo.utils to javafx.fxml;
    opens online.thinhtran.demo.models to javafx.fxml;

    exports online.thinhtran.demo;
    exports online.thinhtran.demo.controllers;
    exports online.thinhtran.demo.utils;
    exports online.thinhtran.demo.models;
}