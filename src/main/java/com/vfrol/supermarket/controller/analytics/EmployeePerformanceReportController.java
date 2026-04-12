package com.vfrol.supermarket.controller.analytics;

import com.vfrol.supermarket.controller.base.BaseReportController;
import com.vfrol.supermarket.dto.analytics.EmployeePerformanceDTO;
import com.vfrol.supermarket.tools.excel.BaseExcelExporter;
import com.vfrol.supermarket.tools.excel.EmployeePerformanceExcelExporter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import java.util.ArrayList;
import java.util.List;

public class EmployeePerformanceReportController extends BaseReportController<EmployeePerformanceDTO> {

    @FXML private TableColumn<EmployeePerformanceDTO, String> cashierNameColumn;
    @FXML private TableColumn<EmployeePerformanceDTO, Number> receiptCountColumn;
    @FXML private TableColumn<EmployeePerformanceDTO, Number> totalAmountColumn;

    @FXML
    public void initialize() {
        cashierNameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().cashierName()));
        receiptCountColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().receiptCount()));
        totalAmountColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().totalAmount()));
    }

    @Override
    public void setData(List<EmployeePerformanceDTO> data) {
        List<EmployeePerformanceDTO> tableRows = new ArrayList<>(data);

        long totalReceipts = data.stream().mapToLong(EmployeePerformanceDTO::receiptCount).sum();
        double totalAmount = data.stream().mapToDouble(EmployeePerformanceDTO::totalAmount).sum();

        tableRows.add(EmployeePerformanceDTO.builder().cashierName("").build());
        tableRows.add(EmployeePerformanceDTO.builder()
                .cashierName("TOTALS")
                .receiptCount((int) totalReceipts)
                .totalAmount(totalAmount)
                .build());

        super.setData(tableRows);
    }

    @Override protected BaseExcelExporter<EmployeePerformanceDTO> getExporter() { return new EmployeePerformanceExcelExporter(); }
    @Override protected String getDefaultFileName() { return "Employee_Performance_Report.xlsx"; }
    @Override protected String getReportTitle() { return "Save Employee Performance Report"; }
}