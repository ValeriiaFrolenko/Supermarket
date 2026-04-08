package com.vfrol.supermarket.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.config.ViewManager;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.service.EmployeeService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Singleton
public class LoginController {
    private final ViewManager viewManager;
    private final SessionManager sessionManager;
    private final EmployeeService employeeService;

    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @Inject
    public LoginController(ViewManager viewManager, SessionManager sessionManager, EmployeeService employeeService) {
        this.viewManager = viewManager;
        this.sessionManager = sessionManager;
        this.employeeService = employeeService;
    }

    @FXML
    public void onLogin() {
        String id = idField.getText();
        String password = passwordField.getText();

        if (id.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter both ID and password.");
            return;
        }

        if (employeeService.authenticateEmployee(id, password)) {
            EmployeeDetailsDTO employeeDetails = employeeService.getEmployeeById(id);
            sessionManager.setCurrentUser(employeeDetails);
            viewManager.navigateTo(AppView.MAIN);
        }

    }
}