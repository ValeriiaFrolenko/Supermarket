module com.vfrol.supermarket {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.guice;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires jbcrypt;
    requires static lombok;
    requires org.xerial.sqlitejdbc;
    requires org.jdbi.v3.core;
    requires org.jdbi.v3.sqlobject;
    requires org.jdbi.v3.stringtemplate4;

    opens com.vfrol.supermarket to javafx.fxml, com.google.guice;
    opens com.vfrol.supermarket.controller to javafx.fxml, com.google.guice;
    opens com.vfrol.supermarket.database to com.google.guice;
    opens com.vfrol.supermarket.dao to com.google.guice;

    exports com.vfrol.supermarket;
    exports com.vfrol.supermarket.controller;
}