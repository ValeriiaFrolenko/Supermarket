package com.vfrol.supermarket.controller.employee;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseModalController;
import com.vfrol.supermarket.controller.ui_validator.EmployeeFormValidator;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.dto.employee.EmployeeCreateDTO;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.enums.EmployeeRole;
import com.vfrol.supermarket.service.EmployeeService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import net.synedra.validatorfx.Validator;

import java.math.BigDecimal;

public class EmployeeFormController extends BaseModalController {

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
    @FXML private Button saveButton;

    private final EmployeeService employeeService;
    private final Validator validator = new Validator();
    private boolean isEditMode = false;

    @Inject
    public EmployeeFormController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @FXML
    public void initialize() {
        label.setText("Add Employee");
        roleComboBox.getItems().setAll(EmployeeRole.values());
        setupValidation();
    }

    private void setupValidation() {
        EmployeeFormValidator employeeValidator = new EmployeeFormValidator(validator, () -> !isEditMode);

        employeeValidator.validateId(idField);
        employeeValidator.validatePassword(passwordField);
        employeeValidator.validateSurname(surnameField);
        employeeValidator.validateName(nameField);
        employeeValidator.validatePatronymic(patronymicField);
        employeeValidator.validateRole(roleComboBox);
        employeeValidator.validateSalary(salaryField);
        employeeValidator.validateDates(dobPicker, dosPicker);
        employeeValidator.validatePhone(phoneField);
        employeeValidator.validateCity(cityField);
        employeeValidator.validateStreet(streetField);
        employeeValidator.validateZipCode(zipField);

        saveButton.disableProperty().bind(validator.containsErrorsProperty());
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
        salaryField.setText(BigDecimal.valueOf(dto.salary()).toPlainString());
        dobPicker.setValue(dto.dateOfBirth());
        dosPicker.setValue(dto.dateOfStart());
        phoneField.setText(dto.phoneNumber());
        cityField.setText(dto.city());
        streetField.setText(dto.street());
        zipField.setText(dto.zipCode());
    }

    @FXML
    public void onSave() {
        EmployeeCreateDTO dto = EmployeeCreateDTO.builder()
                .id(InputHelper.getString(idField))
                .rawPassword(InputHelper.getString(passwordField))
                .surname(InputHelper.getString(surnameField))
                .name(InputHelper.getString(nameField))
                .patronymic(InputHelper.getString(patronymicField))
                .role(roleComboBox.getValue())
                .salary(InputHelper.getDouble(salaryField))
                .dateOfBirth(dobPicker.getValue())
                .dateOfStart(dosPicker.getValue())
                .phoneNumber(InputHelper.getString(phoneField))
                .city(InputHelper.getString(cityField))
                .street(InputHelper.getString(streetField))
                .zipCode(InputHelper.getString(zipField))
                .build();

        AsyncRunner.runAsync(() -> saveEmployee(dto), this::closeForm, formPanel);
    }

    private void saveEmployee(EmployeeCreateDTO dto) {
        if (isEditMode) {
            employeeService.updateEmployee(dto);
        } else {
            employeeService.addEmployee(dto);
        }
    }

    private void closeForm() {
        closeWindow(formPanel);
    }

    @FXML
    public void onCancel() {
        closeWindow(formPanel);
    }
}