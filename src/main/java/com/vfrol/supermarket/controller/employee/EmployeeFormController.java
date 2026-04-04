package com.vfrol.supermarket.controller.employee;

import com.google.inject.Inject;
import com.vfrol.supermarket.dto.employee.EmployeeCreateDTO;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.enums.EmployeeRole;
import com.vfrol.supermarket.service.EmployeeService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeFormController {

    @FXML private VBox formPanel;
    @FXML private Label label;
    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private TextField surnameField;
    @FXML private TextField nameField;
    @FXML private TextField patronymicField;
    @FXML private ComboBox<EmployeeRole> roleComboBox;
    @FXML private TextField salaryField;
    @FXML private DatePicker dobPicker;
    @FXML private DatePicker dosPicker;
    @FXML private TextField phoneField;
    @FXML private TextField cityField;
    @FXML private TextField streetField;
    @FXML private TextField zipField;

    private final EmployeeService employeeService;

    private boolean isEditMode = false;

    @Inject
    public EmployeeFormController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @FXML
    public void initialize() {
        label.setText("Add Employee");
        roleComboBox.getItems().setAll(EmployeeRole.values());
    }

    public void setEmployee(EmployeeDetailsDTO dto) {
        this.isEditMode = true;
        label.setText("Edit Employee");
        idField.setText(dto.id());
        idField.setDisable(true);
        surnameField.setText(dto.surname());
        nameField.setText(dto.name());
        patronymicField.setText(dto.patronymic() != null ? dto.patronymic() : "");
        roleComboBox.setValue(dto.role());
        salaryField.setText(String.valueOf(dto.salary()));
        dobPicker.setValue(dto.dateOfBirth());
        dosPicker.setValue(dto.dateOfStart());
        phoneField.setText(dto.phoneNumber());
        cityField.setText(dto.city());
        streetField.setText(dto.street());
        zipField.setText(dto.zipCode());
    }

    @FXML
    public void onSave() {
        if (!isEditMode && (passwordField.getText() == null || passwordField.getText().isBlank())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Password is required for a new employee.");
            alert.showAndWait();
            return;
        }

        try {
            EmployeeCreateDTO dto = EmployeeCreateDTO.builder()
                    .id(idField.getText())
                    .rawPassword(passwordField.getText())
                    .surname(surnameField.getText())
                    .name(nameField.getText())
                    .patronymic(patronymicField.getText().isBlank() ? null : patronymicField.getText())
                    .role(roleComboBox.getValue())
                    .salary(Double.parseDouble(salaryField.getText()))
                    .dateOfBirth(dobPicker.getValue())
                    .dateOfStart(dosPicker.getValue())
                    .phoneNumber(phoneField.getText())
                    .city(cityField.getText())
                    .street(streetField.getText())
                    .zipCode(zipField.getText())
                    .build();

            if (isEditMode) {
                employeeService.updateEmployee(dto);
            } else {
                employeeService.addEmployee(dto);
            }
            closeWindow();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid salary format. Please enter a valid number.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while saving: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage window = (Stage) formPanel.getScene().getWindow();
        if (window != null) {
            window.close();
        }
    }
}