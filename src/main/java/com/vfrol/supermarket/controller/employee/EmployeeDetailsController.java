package com.vfrol.supermarket.controller.employee;

import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeDetailsController {

    @FXML private VBox detailsPanel;

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

    @FXML
    public void initialize() {
    }

    public void setEmployeeDetails(EmployeeDetailsDTO dto) {
        idLabel.setText(dto.id());
        surnameLabel.setText(dto.surname());
        nameLabel.setText(dto.name());
        patronymicLabel.setText(dto.patronymic() != null ? dto.patronymic() : "");
        roleLabel.setText(dto.role().name());
        salaryLabel.setText(String.valueOf(dto.salary()));
        dobLabel.setText(dto.dateOfBirth().toString());
        dosLabel.setText(dto.dateOfStart().toString());
        phoneLabel.setText(dto.phoneNumber());
        cityLabel.setText(dto.city());
        streetLabel.setText(dto.street());
        zipLabel.setText(dto.zipCode());
    }

    @FXML
    public void hide() {
        Stage window = (Stage) detailsPanel.getScene().getWindow();
        if (window != null) {
            window.close();
        }
    }
}