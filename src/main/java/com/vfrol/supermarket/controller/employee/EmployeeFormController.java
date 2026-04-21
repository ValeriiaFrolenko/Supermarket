package com.vfrol.supermarket.controller.employee;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseFormController;
import com.vfrol.supermarket.controller.ui_validator.EmployeeFormValidator;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.dto.employee.EmployeeCreateDTO;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.enums.EmployeeRole;
import com.vfrol.supermarket.service.EmployeeService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.util.Objects;

public class EmployeeFormController extends BaseFormController<EmployeeCreateDTO, EmployeeDetailsDTO> {

    private final EmployeeService employeeService;

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

    @Inject
    public EmployeeFormController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        roleComboBox.getItems().setAll(EmployeeRole.values());
    }
    @Override
    protected String getEntityName() {
        return "Employee";
    }

    @Override
    protected void setupValidation() {
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
    }

    @Override
    protected void populateFields(EmployeeDetailsDTO dto) {
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

    @Override
    protected EmployeeCreateDTO buildDTO() {
        return EmployeeCreateDTO.builder()
                .id(InputHelper.getString(idField))
                .rawPassword(InputHelper.getString(passwordField))
                .surname(InputHelper.getString(surnameField))
                .name(InputHelper.getString(nameField))
                .patronymic(InputHelper.getString(patronymicField))
                .role(roleComboBox.getValue())
                .salary(Objects.requireNonNull(InputHelper.getDouble(salaryField)))
                .dateOfBirth(dobPicker.getValue())
                .dateOfStart(dosPicker.getValue())
                .phoneNumber(InputHelper.getString(phoneField))
                .city(InputHelper.getString(cityField))
                .street(InputHelper.getString(streetField))
                .zipCode(InputHelper.getString(zipField))
                .build();
    }

    @Override
    protected void saveEntity(EmployeeCreateDTO dto) {
        if (isEditMode) {
            employeeService.updateEmployee(dto);
        } else {
            employeeService.addEmployee(dto);
        }
    }
}