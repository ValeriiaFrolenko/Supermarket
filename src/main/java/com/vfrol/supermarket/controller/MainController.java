package com.vfrol.supermarket.controller;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.config.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class MainController {
    private final ViewManager viewManager;
    private final SessionManager sessionManager;

    @FXML private StackPane contentArea;
    @FXML private VBox sideBar;
    @FXML private VBox centerMenu;
    @FXML Button accountButton;
    @FXML Button employeesButton;
    @FXML Button categoriesButton;
    @FXML Button productsButton;
    @FXML Button storeProductsButton;
    @FXML Button checksButton;
    @FXML Button customersButton;

    private boolean menuMoved = false;

    @Inject
    public MainController(ViewManager viewManager, SessionManager sessionManager) {
        this.viewManager = viewManager;
        this.sessionManager = sessionManager;
    }

    @FXML
    public void initialize() {
        viewManager.setContentArea(contentArea);
        configureForRole();
    }

    private void activateSideBar() {
        if (!menuMoved) {
            menuMoved = true;
            centerMenu.setVisible(false);
            centerMenu.setManaged(false);
            sideBar.setVisible(true);
            sideBar.setManaged(true);
        }
    }

    private void configureForRole() {
        if (!sessionManager.isManager()) {
            employeesButton.setVisible(false);
            employeesButton.setManaged(false);
            categoriesButton.setVisible(false);
            categoriesButton.setManaged(false);
            centerMenu.getChildren().get(1).setVisible(false);
            centerMenu.getChildren().get(1).setManaged(false);
            centerMenu.getChildren().get(2).setVisible(false);
            centerMenu.getChildren().get(2).setManaged(false);
        }
    }

    @FXML
    public void goToAccount() {
        activateSideBar();
        viewManager.navigateToContent(AppView.ACCOUNT_VIEW);
    }

    @FXML
    public void goToEmployees() {
        activateSideBar();
        viewManager.navigateToContent(AppView.EMPLOYEE_LIST);
    }

    @FXML
    public void goToCategories() {
        activateSideBar();
        viewManager.navigateToContent(AppView.CATEGORY_LIST);
    }

    @FXML
    public void goToProducts() {
        activateSideBar();
        viewManager.navigateToContent(AppView.PRODUCT_LIST);
    }

    @FXML
    public void goToStoreProducts() {
        activateSideBar();
        viewManager.navigateToContent(AppView.STORE_PRODUCT_LIST);
    }

    @FXML
    public void goToChecks() {
        activateSideBar();
        viewManager.navigateToContent(AppView.CHECK_LIST);
    }

    @FXML
    public void goToCustomers() {
        activateSideBar();
        viewManager.navigateToContent(AppView.CUSTOMER_CARD_LIST);
    }
}