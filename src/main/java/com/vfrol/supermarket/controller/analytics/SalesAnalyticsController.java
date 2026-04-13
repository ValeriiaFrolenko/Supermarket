package com.vfrol.supermarket.controller.analytics;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseListController;
import com.vfrol.supermarket.controller.check.CheckDetailsController;
import com.vfrol.supermarket.controller.util.AlertHelper;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.controller.util.SearchableComboBoxHelper;
import com.vfrol.supermarket.dto.analytics.SalesAnalyticsDTO;
import com.vfrol.supermarket.dto.employee.EmployeeListDTO;
import com.vfrol.supermarket.dto.product.ProductNameDTO;
import com.vfrol.supermarket.enums.sortby.SalesAnalyticsSortBy;
import com.vfrol.supermarket.filter.SalesAnalyticsFilter;
import com.vfrol.supermarket.service.CheckService;
import com.vfrol.supermarket.service.EmployeeService;
import com.vfrol.supermarket.service.ProductService;
import com.vfrol.supermarket.service.SalesAnalyticsService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class SalesAnalyticsController extends BaseListController<SalesAnalyticsDTO> {

    private final SalesAnalyticsService salesAnalyticsService;
    private final ProductService productService;
    private final EmployeeService employeeService;
    private final CheckService checkService;

    @FXML private TableView<SalesAnalyticsDTO> analyticsTable;
    @FXML private TableColumn<SalesAnalyticsDTO, String> productNameColumn;
    @FXML private TableColumn<SalesAnalyticsDTO, Number> quantitySoldColumn;
    @FXML private TableColumn<SalesAnalyticsDTO, Number> totalAmountColumn;
    @FXML private TableColumn<SalesAnalyticsDTO, String> checkNumberColumn;
    @FXML private TableColumn<SalesAnalyticsDTO, String> cashierNameColumn;
    @FXML private TableColumn<SalesAnalyticsDTO, String> dateTimeColumn;

    @FXML private Label totalAmountLabel;
    @FXML private Label totalQuantityLabel;

    @FXML private VBox filterPanel;
    @FXML private DatePicker dateFromPicker;
    @FXML private DatePicker dateToPicker;
    @FXML private SearchableComboBox<ProductNameDTO> productComboBox;
    @FXML private SearchableComboBox<EmployeeListDTO> cashierComboBox;
    @FXML private ComboBox<SalesAnalyticsSortBy> sortByComboBox;

    private ObservableList<SalesAnalyticsDTO> tableData;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Inject
    public SalesAnalyticsController(SalesAnalyticsService salesAnalyticsService,
                                    ProductService        productService,
                                    EmployeeService       employeeService,
                                    CheckService          checkService) {
        this.salesAnalyticsService = salesAnalyticsService;
        this.productService        = productService;
        this.employeeService       = employeeService;
        this.checkService          = checkService;
    }

    @FXML
    public void initialize() {
        initializeTable();
        initializeProductComboBox();
        initializeCashierComboBox();
        initializeSortComboBox();
        initializeDatePickerListeners();
        applyFilter();
    }

    private void initializeTable() {
        tableData = FXCollections.observableArrayList();

        productNameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().productName()));
        quantitySoldColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().quantitySold()));
        totalAmountColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().totalAmount()));
        checkNumberColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().checkNumber()));
        cashierNameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().cashierName()));
        dateTimeColumn.setCellValueFactory(c -> {
            var dt = c.getValue().dateTime();
            return new SimpleStringProperty(dt != null ? dt.format(DATE_FORMATTER) : "");
        });

        analyticsTable.setItems(tableData);
        setupTableDoubleClick();
    }

    private void initializeProductComboBox() {
        SearchableComboBoxHelper.configureForFilter(
                productComboBox,
                productService::getAllProductNames,
                ProductNameDTO::name
        );
        productComboBox.valueProperty().addListener((_, oldValue, newValue) -> {
            if (newValue == null && oldValue != null) return;
            applyFilter();
        });

    }

    private void initializeCashierComboBox() {
        SearchableComboBoxHelper.configureForFilter(
                cashierComboBox,
                employeeService::getAllEmployees,
                emp -> emp.surname() + " " + emp.name()
        );
        cashierComboBox.valueProperty().addListener((_, oldValue, newValue) -> {
            if (newValue == null && oldValue != null) return;
            applyFilter();
        });
    }

    private void initializeSortComboBox() {
        sortByComboBox.getItems().addAll(SalesAnalyticsSortBy.values());
        sortByComboBox.valueProperty().addListener((_, _, _) -> applyFilter());
    }

    private void initializeDatePickerListeners() {
        dateFromPicker.valueProperty().addListener((_, _, _) -> applyFilter());
        dateToPicker.valueProperty().addListener((_, _, _) -> applyFilter());
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
        productComboBox.setValue(null);
        cashierComboBox.setValue(null);
        sortByComboBox.setValue(null);
        applyFilter();
    }

    @FXML
    public void applyFilter() {
        SalesAnalyticsFilter filter = buildFilter();
        setProgressIndicator();

        AsyncRunner.runAsync(
                () -> salesAnalyticsService.getByFilter(filter),
                this::updateTableData,
                analyticsTable
        );
    }

    private SalesAnalyticsFilter buildFilter() {
        ProductNameDTO  selectedProduct = productComboBox.getValue();
        EmployeeListDTO selectedCashier = cashierComboBox.getValue();

        return SalesAnalyticsFilter.builder()
                .dateFrom(dateFromPicker.getValue())
                .dateTo(dateToPicker.getValue())
                .productId(selectedProduct != null ? selectedProduct.id() : null)
                .employeeId(selectedCashier != null ? selectedCashier.id() : null)
                .sortBy(sortByComboBox.getValue())
                .build();
    }

    private void updateTableData(List<SalesAnalyticsDTO> data) {
        tableData.setAll(data);
        updateTotals(data);
        removeProgressIndicator();
    }

    private void updateTotals(List<SalesAnalyticsDTO> data) {
        long totalQty = data.stream().mapToLong(SalesAnalyticsDTO::quantitySold).sum();
        double totalAmount = data.stream().mapToDouble(SalesAnalyticsDTO::totalAmount).sum();

        totalQuantityLabel.setText(String.valueOf(totalQty));
        totalAmountLabel.setText(String.format(Locale.US, "%.2f UAH", totalAmount));
    }

    @FXML
    public void onExportClick() {
        if (tableData == null || tableData.isEmpty()) {
            AlertHelper.showWarning("No data to export. Apply a filter first.");
            return;
        }

        List<SalesAnalyticsDTO> snapshot = List.copyOf(tableData);
        navigateToReport(snapshot);
    }

    private void navigateToReport(List<SalesAnalyticsDTO> snapshot) {
        viewManager.showDialog(AppView.SALES_ANALYTICS_REPORT,
                (SalesAnalyticsReportController controller) ->
                        controller.setData(snapshot));
    }


    @Override
    protected TableView<SalesAnalyticsDTO> getTableView() {
        return analyticsTable;
    }


    @Override
    protected void showDetails(SalesAnalyticsDTO item) {
        AsyncRunner.runAsync(
                () -> checkService.getCheckById(item.checkNumber()),
                details -> viewManager.showDialog(AppView.CHECK_DETAILS,
                        (CheckDetailsController ctrl) -> ctrl.setDetails(details)),
                analyticsTable
        );
    }
}