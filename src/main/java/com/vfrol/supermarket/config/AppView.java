package com.vfrol.supermarket.config;

import lombok.Getter;

@Getter
public enum AppView {

    LOGIN("view/login-view.fxml"),
    MAIN("view/main-view.fxml"),

    ACCOUNT_VIEW("view/account/account-view.fxml"),

    EMPLOYEE_LIST("view/employee/employee-list-view.fxml"),
    EMPLOYEE_DETAILS("view/employee/employee-details-view.fxml"),
    EMPLOYEE_FORM("view/employee/employee-form-view.fxml"),
    EMPLOYEE_REPORT("view/employee/employee-report-view.fxml"),

    CATEGORY_LIST("view/category/category-list-view.fxml"),
    CATEGORY_FORM("view/category/category-form-view.fxml"),
    CATEGORY_DETAILS("view/category/category-details-view.fxml"),
    CATEGORY_REPORT("view/category/category-report-view.fxml"),

    PRODUCT_LIST("view/product/product-list-view.fxml"),
    PRODUCT_FORM("view/product/product-form-view.fxml"),
    PRODUCT_DETAILS("view/product/product-details-view.fxml"),
    PRODUCT_REPORT("view/product/product-report-view.fxml"),

    CUSTOMER_CARD_LIST("view/customer_card/customer-card-list-view.fxml"),
    CUSTOMER_CARD_FORM("view/customer_card/customer-card-form-view.fxml"),
    CUSTOMER_CARD_DETAILS("view/customer_card/customer-card-details-view.fxml"),
    CUSTOMER_CARD_REPORT("view/customer_card/customer-card-report-view.fxml"),

    STORE_PRODUCT_LIST("view/store_product/store-product-list-view.fxml"),
    STORE_PRODUCT_FORM("view/store_product/store-product-form-view.fxml"),
    STORE_PRODUCT_DETAILS("view/store_product/store-product-details-view.fxml"),
    STORE_PRODUCT_REPORT("view/store_product/store-product-report-view.fxml"),

    CHECK_LIST("view/check/check-list-view.fxml"),
    CHECK_FORM("view/check/check-form-view.fxml"),
    CHECK_DETAILS("view/check/check-details-view.fxml"),
    SALE_FORM("view/check/sale-form-view.fxml"),
    CHECK_REPORT("view/check/check-report-view.fxml");

    private final String fxmlPath;

    AppView(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }
}