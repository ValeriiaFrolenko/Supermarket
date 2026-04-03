package com.vfrol.supermarket.controller;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.config.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class MainController {

    private final ViewManager viewManager;

    @FXML
    private StackPane contentArea;

    @Inject
    public MainController(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    @FXML
    public void initialize() {
        viewManager.setContentArea(contentArea);
        viewManager.navigateTo(AppView.EMPLOYEE_LIST);
    }

    @FXML
    public void goToEmployees() {
        viewManager.navigateTo(AppView.EMPLOYEE_LIST);
    }
}