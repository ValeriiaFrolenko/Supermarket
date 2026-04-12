package com.vfrol.supermarket.controller.check;

import com.vfrol.supermarket.controller.base.BaseReportController;
import com.vfrol.supermarket.dto.check.CheckDetailsDTO;
import com.vfrol.supermarket.tools.excel.BaseExcelExporter;
import com.vfrol.supermarket.tools.excel.CheckExcelExporter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

import java.time.format.DateTimeFormatter;

public class CheckReportController extends BaseReportController<CheckDetailsDTO> {

    @FXML private TableColumn<CheckDetailsDTO, String> checkNumberColumn;
    @FXML private TableColumn<CheckDetailsDTO, String> cashierColumn;
    @FXML private TableColumn<CheckDetailsDTO, String> customerColumn;
    @FXML private TableColumn<CheckDetailsDTO, String> dateColumn;
    @FXML private TableColumn<CheckDetailsDTO, Number> sumTotalColumn;
    @FXML private TableColumn<CheckDetailsDTO, Number> vatColumn;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        checkNumberColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().checkNumber()));
        cashierColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().employeeName()));
        customerColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().customerName() != null ? cell.getValue().customerName() : ""));
        dateColumn.setCellValueFactory(cell -> {
            var dateTime = cell.getValue().dateTime();
            return new SimpleStringProperty(dateTime != null ? dateTime.format(DATE_FORMATTER) : "");
        });
        sumTotalColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().sumTotal()));
        vatColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().vat()));
    }

    @Override
    protected BaseExcelExporter<CheckDetailsDTO> getExporter() {
        return new CheckExcelExporter();
    }

    @Override
    protected String getDefaultFileName() {
        return "Checks_Full_Report.xlsx";
    }

    @Override
    protected String getReportTitle() {
        return "Save Checks Report";
    }
}