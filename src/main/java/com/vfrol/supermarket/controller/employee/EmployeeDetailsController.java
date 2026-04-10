package com.vfrol.supermarket.controller.employee;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseDetailsController;
import com.vfrol.supermarket.controller.util.SessionUIHelper;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.service.EmployeeService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.util.Locale;

public class EmployeeDetailsController extends BaseDetailsController<EmployeeDetailsDTO> {

    private final EmployeeService employeeService;

    @FXML private Label idLabel;
    @FXML private Label surnameLabel;
    @FXML private Label nameLabel;
    @FXML private Label patronymicLabel;
    @FXML private Label roleLabel;
    @FXML private Label salaryLabel;
    @FXML private Label dobLabel;
    @FXML private Label dosLabel;
    @FXML private Label phoneLabel;
    @FXML private Label cityLabel;
    @FXML private Label streetLabel;
    @FXML private Label zipLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @Inject
    public EmployeeDetailsController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @FXML
    public void initialize() {
            SessionUIHelper.configureManagerOnlyNodes(sessionManager, editButton, deleteButton);
    }

    @Override
    protected String getEntityName() {
        return "Employee";
    }

    @Override
    protected void populateFields(EmployeeDetailsDTO dto) {
        idLabel.setText(dto.id());
        surnameLabel.setText(dto.surname());
        nameLabel.setText(dto.name());
        patronymicLabel.setText(dto.patronymic() != null ? dto.patronymic() : "");
        roleLabel.setText(dto.role().name());
        salaryLabel.setText(String.format(Locale.US, "%.2f", dto.salary()));
        dobLabel.setText(dto.dateOfBirth().toString());
        dosLabel.setText(dto.dateOfStart().toString());
        phoneLabel.setText(dto.phoneNumber());
        cityLabel.setText(dto.city());
        streetLabel.setText(dto.street());
        zipLabel.setText(dto.zipCode());
    }

    @Override
    protected void deleteEntity() {
        employeeService.deleteEmployee(currentItem.id());
    }

    @Override
    protected void navigateToForm() {
        viewManager.showDialog(AppView.EMPLOYEE_FORM, (EmployeeFormController controller) ->
                controller.setupForEdit(currentItem));
    }
}