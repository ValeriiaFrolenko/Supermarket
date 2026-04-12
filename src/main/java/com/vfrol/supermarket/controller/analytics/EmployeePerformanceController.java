package com.vfrol.supermarket.controller.analytics;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseListController;
import com.vfrol.supermarket.controller.util.AlertHelper;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.dto.analytics.EmployeePerformanceDTO;
import com.vfrol.supermarket.enums.sortby.EmployeePerformanceSortBy;
import com.vfrol.supermarket.filter.EmployeePerformanceFilter;
import com.vfrol.supermarket.service.EmployeePerformanceService;
import com.vfrol.supermarket.tools.excel.EmployeePerformanceExcelExporter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class EmployeePerformanceController extends BaseListController<EmployeePerformanceDTO> {

    private final EmployeePerformanceService employeePerformanceService;

    @FXML private TableView<EmployeePerformanceDTO> analyticsTable;
    @FXML private TableColumn<EmployeePerformanceDTO, String> cashierNameColumn;
    @FXML private TableColumn<EmployeePerformanceDTO, Number> receiptCountColumn;
    @FXML private TableColumn<EmployeePerformanceDTO, Number> totalAmountColumn;

    @FXML private Label totalReceiptsLabel;
    @FXML private Label totalAmountLabel;

    @FXML private VBox filterPanel;
    @FXML private DatePicker dateFromPicker;
    @FXML private DatePicker dateToPicker;
    @FXML private CheckBox  onlyWithCardAlwaysCheckBox;
    @FXML private ComboBox<EmployeePerformanceSortBy> sortByComboBox;

    private ObservableList<EmployeePerformanceDTO> tableData;

    @Inject
    public EmployeePerformanceController(EmployeePerformanceService employeePerformanceService) {
        this.employeePerformanceService = employeePerformanceService;
    }

    @FXML
    public void initialize() {
        initializeTable();
        initializeSortComboBox();
        initializeDatePickerListeners();
        initializeCheckBoxListener();
        applyFilter();
    }

    private void initializeTable() {
        tableData = FXCollections.observableArrayList();

        cashierNameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().cashierName()));
        receiptCountColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().receiptCount()));
        totalAmountColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().totalAmount()));

        analyticsTable.setItems(tableData);
    }

    private void initializeSortComboBox() {
        sortByComboBox.getItems().addAll(EmployeePerformanceSortBy.values());
        sortByComboBox.valueProperty().addListener((_, _, _) -> applyFilter());
    }

    private void initializeDatePickerListeners() {
        dateFromPicker.valueProperty().addListener((_, _, _) -> applyFilter());
        dateToPicker.valueProperty().addListener((_, _, _) -> applyFilter());
    }

    private void initializeCheckBoxListener() {
        onlyWithCardAlwaysCheckBox.selectedProperty().addListener((_, _, _) -> applyFilter());
    }

    @FXML
    public void onLast7DaysClick() {
        LocalDate today = LocalDate.now();
        dateFromPicker.setValue(today.minusDays(6));
        dateToPicker.setValue(today);
    }

    @FXML
    public void onLast30DaysClick() {
        LocalDate today = LocalDate.now();
        dateFromPicker.setValue(today.minusDays(29));
        dateToPicker.setValue(today);
    }

    @FXML
    public void onThisMonthClick() {
        LocalDate today = LocalDate.now();
        dateFromPicker.setValue(today.withDayOfMonth(1));
        dateToPicker.setValue(today);
    }

    @FXML
    public void onThisYearClick() {
        LocalDate today = LocalDate.now();
        dateFromPicker.setValue(today.withDayOfYear(1));
        dateToPicker.setValue(today);
    }

    @FXML
    public void onToggleFilterClick() {
        toggleFilterPanel(filterPanel);
    }

    @FXML
    public void onClearFilterClick() {
        dateFromPicker.setValue(null);
        dateToPicker.setValue(null);
        onlyWithCardAlwaysCheckBox.setSelected(false);
        sortByComboBox.setValue(null);
        applyFilter();
    }

    @FXML
    public void applyFilter() {
        EmployeePerformanceFilter filter = buildFilter();
        setProgressIndicator();

        AsyncRunner.runAsync(
                () -> employeePerformanceService.getByFilter(filter),
                this::updateTableData,
                analyticsTable
        );
    }

    private EmployeePerformanceFilter buildFilter() {
        return EmployeePerformanceFilter.builder()
                .dateFrom(dateFromPicker.getValue())
                .dateTo(dateToPicker.getValue())
                .onlyWithCardAlways(onlyWithCardAlwaysCheckBox.isSelected())
                .sortBy(sortByComboBox.getValue())
                .build();
    }

    private void updateTableData(List<EmployeePerformanceDTO> data) {
        tableData.setAll(data);
        updateTotals(data);
        removeProgressIndicator();
    }

    private void updateTotals(List<EmployeePerformanceDTO> data) {
        long totalReceipts = data.stream().mapToLong(EmployeePerformanceDTO::receiptCount).sum();
        double totalAmount = data.stream().mapToDouble(EmployeePerformanceDTO::totalAmount).sum();

        totalReceiptsLabel.setText(String.valueOf(totalReceipts));
        totalAmountLabel.setText(String.format(Locale.US, "%.2f UAH", totalAmount));
    }

    @FXML
    public void onExportClick() {
        if (tableData == null || tableData.isEmpty()) {
            AlertHelper.showWarning("No data to export. Apply a filter first.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Employee Performance Report");
        fileChooser.setInitialFileName("Employee_Performance_Report.xlsx");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showSaveDialog(analyticsTable.getScene().getWindow());
        if (file == null) return;

        List<EmployeePerformanceDTO> snapshot = List.copyOf(tableData);

        AsyncRunner.runAsync(
                () -> {
                    new EmployeePerformanceExcelExporter().export(snapshot, file);
                    return file.getAbsolutePath();
                },
                filePath -> AlertHelper.showInfo("Export Successful", "File saved to:\n" + filePath),
                getRootNode()
        );
    }

    @Override
    protected TableView<EmployeePerformanceDTO> getTableView() {
        return analyticsTable;
    }

    @Override
    protected void showDetails(EmployeePerformanceDTO item) {
        // intentionally no-op: this is an aggregated analytics view
    }
}