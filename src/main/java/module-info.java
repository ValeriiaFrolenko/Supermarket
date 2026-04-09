open module com.vfrol.supermarket {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.guice;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires jbcrypt;
    requires static lombok;
    requires org.jdbi.v3.core;
    requires org.jdbi.v3.sqlobject;
    requires org.jdbi.v3.stringtemplate4;
    requires org.slf4j;
    requires com.h2database;
    requires java.naming;
    requires org.checkerframework.checker.qual;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    exports com.vfrol.supermarket;
}