package com.vfrol.supermarket.controller.employee;

import com.vfrol.supermarket.controller.base.BaseReportController;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.tools.excel.BaseExcelExporter;
import com.vfrol.supermarket.tools.excel.EmployeeExcelExporter;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

import java.util.Locale;

public class EmployeeReportController extends BaseReportController<EmployeeDetailsDTO> {

    @FXML private TableColumn<EmployeeDetailsDTO, String> idColumn;
    @FXML private TableColumn<EmployeeDetailsDTO, String> surnameColumn;
    @FXML private TableColumn<EmployeeDetailsDTO, String> nameColumn;
    @FXML private TableColumn<EmployeeDetailsDTO, String> patronymicColumn;
    @FXML private TableColumn<EmployeeDetailsDTO, String> roleColumn;
    @FXML private TableColumn<EmployeeDetailsDTO, String> salaryColumn;
    @FXML private TableColumn<EmployeeDetailsDTO, String> dateOfBirthColumn;
    @FXML private TableColumn<EmployeeDetailsDTO, String> dateOfStartColumn;
    @FXML private TableColumn<EmployeeDetailsDTO, String> phoneColumn;
    @FXML private TableColumn<EmployeeDetailsDTO, String> cityColumn;
    @FXML private TableColumn<EmployeeDetailsDTO, String> streetColumn;
    @FXML private TableColumn<EmployeeDetailsDTO, String> zipCodeColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cell          -> new SimpleStringProperty(cell.getValue().id()));
        surnameColumn.setCellValueFactory(cell     -> new SimpleStringProperty(cell.getValue().surname()));
        nameColumn.setCellValueFactory(cell        -> new SimpleStringProperty(cell.getValue().name()));
        patronymicColumn.setCellValueFactory(cell  -> new SimpleStringProperty(cell.getValue().patronymic() != null ? cell.getValue().patronymic() : ""));
        roleColumn.setCellValueFactory(cell        -> new SimpleStringProperty(cell.getValue().role().name()));
        salaryColumn.setCellValueFactory(cell      -> new SimpleStringProperty(String.format(Locale.US, "%.2f", cell.getValue().salary())));
        dateOfBirthColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().dateOfBirth().toString()));
        dateOfStartColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().dateOfStart().toString()));
        phoneColumn.setCellValueFactory(cell       -> new SimpleStringProperty(cell.getValue().phoneNumber()));
        cityColumn.setCellValueFactory(cell        -> new SimpleStringProperty(cell.getValue().city()));
        streetColumn.setCellValueFactory(cell      -> new SimpleStringProperty(cell.getValue().street()));
        zipCodeColumn.setCellValueFactory(cell     -> new SimpleStringProperty(cell.getValue().zipCode()));
    }

    @Override
    protected BaseExcelExporter<EmployeeDetailsDTO> getExporter() {
        return new EmployeeExcelExporter();
    }

    @Override
    protected String getDefaultFileName() {
        return "Employees_Full_Report.xlsx";
    }

    @Override
    protected String getReportTitle() {
        return "Save Employee Report";
    }
}