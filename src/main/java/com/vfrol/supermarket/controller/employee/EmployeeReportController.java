package com.vfrol.supermarket.controller.employee;

import com.vfrol.supermarket.controller.base.BaseModalController;
import com.vfrol.supermarket.controller.util.AlertHelper;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.tools.excel.EmployeeExcelExporter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class EmployeeReportController extends BaseModalController {

    @FXML private TableView<EmployeeDetailsDTO>           previewTable;
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
    @FXML private Button exportButton;

    private List<EmployeeDetailsDTO> employees;

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
        cityColumn.setCellValueFactory(cell        -> new SimpleStringProperty(cell.getValue().city() != null ? cell.getValue().city() : ""));
        streetColumn.setCellValueFactory(cell      -> new SimpleStringProperty(cell.getValue().street() != null ? cell.getValue().street() : ""));
        zipCodeColumn.setCellValueFactory(cell     -> new SimpleStringProperty(cell.getValue().zipCode() != null ? cell.getValue().zipCode() : ""));
    }

    public void setData(List<EmployeeDetailsDTO> employees) {
        this.employees = employees;
        previewTable.setItems(FXCollections.observableArrayList(employees));
    }

    @FXML
    private void onExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Employee Report");

        fileChooser.setInitialFileName("Employees_Full_Report.xlsx");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());
        if (file == null) return;

        exportButton.setDisable(true);

        AsyncRunner.runAsync(
                () -> {
                    new EmployeeExcelExporter().export(employees, file);
                    return file.getAbsolutePath();
                },
                (filePath) -> {
                    AlertHelper.showInfo("Export Successful", "File saved to:\n" + filePath);
                    closeWindow(exportButton);
                },
                previewTable
        );
    }

    @FXML
    private void onCancel() {
        closeWindow(exportButton);
    }
}