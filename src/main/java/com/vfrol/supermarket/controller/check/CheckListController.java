package com.vfrol.supermarket.controller.check;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseListController;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.controller.util.Debouncer;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.controller.util.SessionUIHelper;
import com.vfrol.supermarket.dto.check.CheckDetailsDTO;
import com.vfrol.supermarket.dto.check.CheckListDTO;
import com.vfrol.supermarket.enums.sortby.CheckSortBy;
import com.vfrol.supermarket.filter.CheckFilter;
import com.vfrol.supermarket.service.CheckService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CheckListController extends BaseListController<CheckListDTO> {

    private final CheckService checkService;
    private final Debouncer searchDebouncer = new Debouncer(300);
    private ObservableList<CheckListDTO> checkData;

    @FXML private TextField searchField;
    @FXML private TableView<CheckListDTO> checkTable;
    @FXML private TableColumn<CheckListDTO, String> checkNumberColumn;
    @FXML private TableColumn<CheckListDTO, String> employeeNameColumn;
    @FXML private TableColumn<CheckListDTO, String> dateColumn;
    @FXML private TableColumn<CheckListDTO, Number> sumTotalColumn;
    @FXML private Button addButton;

    @FXML private VBox filterPanel;
    @FXML private TextField cashierSurnameFilterField;
    @FXML private DatePicker dateFromPicker;
    @FXML private DatePicker dateToPicker;
    @FXML private ComboBox<CheckSortBy> sortByComboBox;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Inject
    public CheckListController(CheckService checkService) {
        this.checkService = checkService;
    }

    @FXML
    public void initialize() {
        SessionUIHelper.configureCashierOnlyNodes(sessionManager, addButton);
        if (!sessionManager.isManager()) {
            cashierSurnameFilterField.setText(sessionManager.getCurrentUser().surname());
            cashierSurnameFilterField.setDisable(true);
        }
        initializeTable();
        initializeFilters();
        loadAllChecks();
    }

    private void initializeTable() {
        checkData = FXCollections.observableArrayList();

        checkNumberColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().checkNumber()));
        employeeNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().employeeName()));
        dateColumn.setCellValueFactory(cell -> {
            var dateTime = cell.getValue().dateTime();
            return new SimpleStringProperty(dateTime != null ? dateTime.format(DATE_FORMATTER) : "");
        });
        sumTotalColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().sumTotal()));

        checkTable.setItems(checkData);
        setupTableDoubleClick();
    }

    private void initializeFilters() {
        searchField.textProperty().addListener((_,_,_) ->
                searchDebouncer.debounce(this::applyFilter));
        cashierSurnameFilterField.textProperty().addListener((_,_,_) ->
                searchDebouncer.debounce(this::applyFilter));
        dateFromPicker.valueProperty().addListener((_,_,_) ->
                applyFilter());
        dateToPicker.valueProperty().addListener((_,_,_) ->
                applyFilter());

        sortByComboBox.getItems().addAll(CheckSortBy.values());
    }

    @Override
    protected TableView<CheckListDTO> getTableView() {
        return checkTable;
    }

    @Override
    protected void showDetails(CheckListDTO item) {
        AsyncRunner.runAsync(
                () -> checkService.getCheckById(item.checkNumber()),
                details -> {
                    navigateToDetails(details);
                    loadAllChecks();
                },
                checkTable
        );
    }

    @FXML
    public void onAddCheckClick() {
        navigateToForm();
        loadAllChecks();
    }

    @FXML
    public void onExportClick() {
        AsyncRunner.runAsync(
                checkService::getAllCheckDetails,
                this::navigateToReport,
                getRootNode()
        );
    }

    @FXML
    public void onToggleFilterClick() {
        toggleFilterPanel(filterPanel);
    }

    @FXML
    public void applyFilter() {
        CheckFilter filter = buildFilter();
        setProgressIndicator();

        if (filter.isEmpty() && sessionManager.isManager()) {
            AsyncRunner.runAsync(
                    checkService::getAllChecks,
                    this::updateTableData,
                    checkTable
            );
        } else {
            AsyncRunner.runAsync(
                    () -> checkService.getCheckByFilter(filter),
                    this::updateTableData,
                    checkTable
            );
        }
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
    public void onClearFilterClick() {
        searchField.clear();
        if (sessionManager.isManager()) {
            cashierSurnameFilterField.clear();
        }
        dateFromPicker.setValue(null);
        dateToPicker.setValue(null);
        sortByComboBox.setValue(null);
        applyFilter();
    }

    private CheckFilter buildFilter() {
        return CheckFilter.builder()
                .checkNumber(InputHelper.getString(searchField))
                .cashierSurname(InputHelper.getString(cashierSurnameFilterField))
                .employeeId(sessionManager.isManager() ? null : sessionManager.getCurrentUser().id())
                .dateFrom(dateFromPicker.getValue())
                .dateTo(dateToPicker.getValue())
                .sortBy(sortByComboBox.getValue())
                .build();
    }

    private void loadAllChecks() {
        applyFilter();
    }

    private void updateTableData(List<CheckListDTO> data) {
        checkData.setAll(data);
        removeProgressIndicator();
    }

    private void navigateToDetails(CheckDetailsDTO details) {
        viewManager.showDialog(AppView.CHECK_DETAILS,
                (CheckDetailsController controller) ->
                        controller.setDetails(details));
    }

    private void navigateToForm() {
        viewManager.showDialog(AppView.CHECK_FORM);
    }

    private void navigateToReport(List<CheckDetailsDTO> checks) {
        viewManager.showDialog(AppView.CHECK_REPORT,
                (CheckReportController controller) ->
                        controller.setData(checks));
    }
}