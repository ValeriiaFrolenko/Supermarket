package com.vfrol.supermarket.controller.employee;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseListController;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.controller.util.Debouncer;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.dto.employee.EmployeeListDTO;
import com.vfrol.supermarket.enums.EmployeeRole;
import com.vfrol.supermarket.filter.EmployeeFilter;
import com.vfrol.supermarket.service.EmployeeService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class EmployeeListController extends BaseListController<EmployeeListDTO> {

    private final EmployeeService employeeService;

    @FXML private TextField searchField;
    @FXML private TableView<EmployeeListDTO> employeeTable;
    @FXML private TableColumn<EmployeeListDTO, String> surnameColumn;
    @FXML private TableColumn<EmployeeListDTO, String> nameColumn;
    @FXML private TableColumn<EmployeeListDTO, String> roleColumn;
    @FXML private TableColumn<EmployeeListDTO, String> phoneColumn;
    @FXML private VBox filterPanel;
    @FXML private TextField phoneFilterField;
    @FXML private ComboBox<EmployeeRole> roleComboBox;

    private ObservableList<EmployeeListDTO> employeeData;
    private final Debouncer searchDebouncer = new Debouncer(300);

    @Inject
    public EmployeeListController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    protected TableView<EmployeeListDTO> getTableView() {
        return employeeTable;
    }

    @Override
    protected void showDetails(EmployeeListDTO item) {
        AsyncRunner.runAsync(
                () -> employeeService.getEmployeeById(item.id()),
                details -> {
                    viewManager.showDialog(AppView.EMPLOYEE_DETAILS,
                            (EmployeeDetailsController controller) -> controller.setEmployeeDetails(details));
                    loadEmployees();
                },
                employeeTable
        );
    }

    @FXML
    public void initialize() {
        employeeData = FXCollections.observableArrayList();

        surnameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().surname()));
        nameColumn.setCellValueFactory(cell    -> new SimpleStringProperty(cell.getValue().name()));
        roleColumn.setCellValueFactory(cell    -> new SimpleStringProperty(cell.getValue().role().name()));
        phoneColumn.setCellValueFactory(cell   -> new SimpleStringProperty(cell.getValue().phoneNumber()));

        employeeTable.setItems(employeeData);
        setupTableDoubleClick();

        searchField.textProperty().addListener((obs, old, val) -> searchDebouncer.debounce(this::applyFilter));
        phoneFilterField.textProperty().addListener((obs, old, val) -> searchDebouncer.debounce(this::applyFilter));
        roleComboBox.valueProperty().addListener((obs, old, val) -> applyFilter());
        roleComboBox.getItems().addAll(EmployeeRole.values());

        loadEmployees();
    }

    @FXML
    public void onToggleFilterClick() {
        toggleFilterPanel(filterPanel);
    }

    @FXML
    public void applyFilter() {
        String surname = null;
        String name = null;
        String searchText = InputHelper.getString(searchField);

        if (searchText != null) {
            String[] parts = searchText.split("\\s+");
            surname = parts[0];
            if (parts.length > 1) name = parts[1];
        }

        EmployeeFilter filter = EmployeeFilter.builder()
                .surname(surname)
                .name(name)
                .phoneNumber(InputHelper.getString(phoneFilterField))
                .role(roleComboBox.getValue())
                .build();

        employeeTable.setPlaceholder(new ProgressIndicator());
        AsyncRunner.runAsync(
                () -> employeeService.getEmployeesByFilter(filter),
                this::updateTableData,
                employeeTable
        );
    }

    @FXML
    public void onClearFilterClick() {
        searchField.clear();
        phoneFilterField.clear();
        roleComboBox.setValue(null);
        applyFilter();
    }

    @FXML
    public void onAddEmployeeClick() {
        viewManager.showDialog(AppView.EMPLOYEE_FORM);
        loadEmployees();
    }

    @FXML
    public void onExportClick() {
        employeeTable.setPlaceholder(new ProgressIndicator());
        AsyncRunner.runAsync(
                employeeService::getAllEmployeeDetails,
                employees -> {
                    employeeTable.setPlaceholder(new Label("No content in table"));
                    viewManager.showDialog(AppView.EMPLOYEE_REPORT,
                            (EmployeeReportController controller) -> controller.setData(employees));
                },
                employeeTable
        );
    }

    private void loadEmployees() {
        employeeTable.setPlaceholder(new ProgressIndicator());
        AsyncRunner.runAsync(
                employeeService::getAllEmployees,
                this::updateTableData,
                employeeTable
        );
    }

    private void updateTableData(List<EmployeeListDTO> data) {
        employeeData.setAll(data);
        employeeTable.setPlaceholder(new Label("No content in table"));
    }
}