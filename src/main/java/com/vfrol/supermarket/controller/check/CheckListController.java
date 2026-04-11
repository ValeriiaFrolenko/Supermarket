package com.vfrol.supermarket.controller.check;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseListController;
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

    @FXML private TextField searchField;
    @FXML private Button addButton;

    @FXML private TableView<CheckListDTO> checkTable;
    @FXML private TableColumn<CheckListDTO, String> checkNumberColumn;
    @FXML private TableColumn<CheckListDTO, String> employeeNameColumn;
    @FXML private TableColumn<CheckListDTO, String> dateColumn;
    @FXML private TableColumn<CheckListDTO, Number> sumTotalColumn;

    @FXML private VBox filterPanel;
    @FXML private TextField cashierSurnameFilterField;
    @FXML private DatePicker dateFromPicker;
    @FXML private DatePicker dateToPicker;
    @FXML private ComboBox<CheckSortBy> sortByComboBox;

    private ObservableList<CheckListDTO> checkData;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Inject
    public CheckListController(CheckService checkService) {
        this.checkService = checkService;
    }

    @Override
    protected TableView<CheckListDTO> getTableView() {
        return checkTable;
    }

    @Override
    protected void showDetails(CheckListDTO item) {
        showCheckDetails(item.checkNumber());
    }

    @FXML
    public void initialize() {
        initializeTable();
        sortByComboBox.getItems().addAll(CheckSortBy.values());

        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());

        SessionUIHelper.configureCashierOnlyNodes(sessionManager, addButton);

        if (!sessionManager.isManager()) {
            cashierSurnameFilterField.setText(sessionManager.getCurrentUser().surname());
            cashierSurnameFilterField.setDisable(true);
        }

        applyFilter();
    }

    private void initializeTable() {
        checkData = FXCollections.observableArrayList();

        checkNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().checkNumber()));
        employeeNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().employeeName()));

        dateColumn.setCellValueFactory(cellData -> {
            var dateTime = cellData.getValue().dateTime();
            return new SimpleStringProperty(dateTime != null ? dateTime.format(DATE_FORMATTER) : "");
        });

        sumTotalColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().sumTotal()));

        checkTable.setItems(checkData);
        setupTableDoubleClick();
    }

    @FXML
    public void onToggleFilterClick() {
        toggleFilterPanel(filterPanel);
    }

    @FXML
    public void applyFilter() {
        String searchText = searchField.getText();
        String checkNumber = (searchText != null && !searchText.isBlank()) ? searchText.trim() : null;

        String cashierText = cashierSurnameFilterField.getText();
        String cashierSurname = (cashierText != null && !cashierText.isBlank()) ? cashierText.trim() : null;

        LocalDate dateFrom = dateFromPicker.getValue();
        LocalDate dateTo = dateToPicker.getValue();

        String employeeId = null;
        if (!sessionManager.isManager()) {
            employeeId = sessionManager.getCurrentUser().id();
            cashierSurname = null;
        }

        CheckFilter filter = CheckFilter.builder()
                .checkNumber(checkNumber)
                .cashierSurname(cashierSurname)
                .employeeId(employeeId)
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .sortBy(sortByComboBox.getValue())
                .build();

        List<CheckListDTO> filtered = checkService.getCheckByFilter(filter);
        checkData.setAll(filtered);
    }

    @FXML
    public void onClearFilterClick() {
        searchField.clear();
        cashierSurnameFilterField.clear();
        dateFromPicker.setValue(null);
        dateToPicker.setValue(null);
        sortByComboBox.setValue(null);
        applyFilter();
    }

    private void loadChecks() {
        List<CheckListDTO> checks = checkService.getAllChecks();
        checkData.clear();
        checkData.addAll(checks);
        applyFilter();
    }

    private void showCheckDetails(String checkNumber) {
        CheckDetailsDTO details = checkService.getCheckById(checkNumber);
        if (details != null) {
            viewManager.showDialog(AppView.CHECK_DETAILS, (CheckDetailsController controller) -> {
                controller.setCheckDetails(details);
            });
        }
        refreshChecks();
    }

    public void refreshChecks() {
        loadChecks();
    }

    @FXML
    public void onAddCheckClick() {
        viewManager.showDialog(AppView.CHECK_FORM);
        refreshChecks();
    }
}