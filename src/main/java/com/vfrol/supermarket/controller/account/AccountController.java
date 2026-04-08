package com.vfrol.supermarket.controller.account;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.config.ViewManager;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AccountController {

    @FXML private Label idLabel;
    @FXML private Label surnameLabel;
    @FXML private Label nameLabel;
    @FXML private Label roleLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;

    private final SessionManager sessionManager;
    private final ViewManager viewManager;

    @Inject
    public AccountController(SessionManager sessionManager, ViewManager viewManager) {
        this.sessionManager = sessionManager;
        this.viewManager = viewManager;
    }

    @FXML
    public void initialize() {
        EmployeeDetailsDTO currentUser = sessionManager.getCurrentUser();

        if (currentUser != null) {
            idLabel.setText(currentUser.id());
            surnameLabel.setText(currentUser.surname());
            nameLabel.setText(currentUser.name());
            roleLabel.setText(currentUser.role().name());
            phoneLabel.setText(currentUser.phoneNumber());

            String fullAddress = currentUser.city() + ", " + currentUser.street() + ", " + currentUser.zipCode();
            addressLabel.setText(fullAddress);
        }
    }

    @FXML
    public void onLogout() {
        sessionManager.logout();
        viewManager.clearCache();
        viewManager.navigateTo(AppView.LOGIN);
    }
}