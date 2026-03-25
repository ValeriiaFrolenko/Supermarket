module com.vfrol.supermarket {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.guice;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;

    opens com.vfrol.supermarket to javafx.fxml, com.google.guice;
    opens com.vfrol.supermarket.controller to javafx.fxml, com.google.guice;

    exports com.vfrol.supermarket;
    exports com.vfrol.supermarket.controller;
}