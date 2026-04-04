package com.vfrol.supermarket.config;

import lombok.Getter;

@Getter
public enum AppView {
    EMPLOYEE_LIST("view/employee/employee-list-view.fxml"),
    EMPLOYEE_DETAILS("view/employee/employee-details-view.fxml"),
    EMPLOYEE_FORM("view/employee/employee-form-view.fxml"),

    CATEGORY_LIST("view/category/category-list-view.fxml"),
    CATEGORY_FORM("view/category/category-form-view.fxml"),
    CATEGORY_DETAILS("view/category/category-details-view.fxml");

    private final String fxmlPath;

    AppView(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }
}