module com.vfrol.supermarket {

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

    opens com.vfrol.supermarket to javafx.fxml, com.google.guice;
    opens com.vfrol.supermarket.controller to javafx.fxml, com.google.guice;
    opens com.vfrol.supermarket.database to com.google.guice;
    opens com.vfrol.supermarket.dao to com.google.guice;
    opens com.vfrol.supermarket.service to com.google.guice;
    opens com.vfrol.supermarket.tools to com.google.guice, javafx.fxml;
    opens com.vfrol.supermarket.controller.employee to com.google.guice, javafx.fxml;
    opens com.vfrol.supermarket.controller.category to com.google.guice, javafx.fxml;
    opens com.vfrol.supermarket.controller.product to com.google.guice, javafx.fxml;
    opens com.vfrol.supermarket.controller.customer_card to com.google.guice, javafx.fxml;
    opens com.vfrol.supermarket.controller.store_product to com.google.guice, javafx.fxml;
    opens com.vfrol.supermarket.controller.util to com.google.guice, javafx.fxml;
    opens com.vfrol.supermarket.controller.check to com.google.guice, javafx.fxml;
    opens com.vfrol.supermarket.config to com.google.guice, javafx.fxml;

    exports com.vfrol.supermarket;
    exports com.vfrol.supermarket.controller;
    exports com.vfrol.supermarket.database;
    exports com.vfrol.supermarket.dao;
    exports com.vfrol.supermarket.entity;
    exports com.vfrol.supermarket.filter;
    exports com.vfrol.supermarket.dto.employee;
    exports com.vfrol.supermarket.dto.customer_card;
    exports com.vfrol.supermarket.dto.check;
    exports com.vfrol.supermarket.dto.sale;
    exports com.vfrol.supermarket.dto.product;
    exports com.vfrol.supermarket.dto.category;
    exports com.vfrol.supermarket.dto.store_product;
    exports com.vfrol.supermarket.enums;
    exports com.vfrol.supermarket.enums.sortby;
    exports com.vfrol.supermarket.tools;
    exports com.vfrol.supermarket.service;
    exports com.vfrol.supermarket.config;
    exports com.vfrol.supermarket.controller.util;
    exports com.vfrol.supermarket.controller.check;
}