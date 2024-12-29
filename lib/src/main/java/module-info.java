module org.example.lib {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;

    requires net.synedra.validatorfx;
    requires org.controlsfx.controls;


    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires commons.math3;

    opens org.example.lib to javafx.fxml;
    opens org.example.lib.controllers to javafx.fxml;
    opens org.example.lib.models to javafx.fxml;
    opens org.example.lib.services to javafx.fxml;
    opens org.example.lib.repositories to javafx.fxml;

    exports org.example.lib;
    exports org.example.lib.models;
    exports org.example.lib.services;
    exports org.example.lib.repositories;
    exports org.example.lib.controllers;
}
