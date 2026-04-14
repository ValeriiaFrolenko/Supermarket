package com.vfrol.supermarket.controller.employee;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseListController;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.controller.util.Debouncer;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
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

import java.util.ArrayList;
import java.util.List;

public class EmployeeListController extends BaseListController<EmployeeListDTO> {

    private final EmployeeService employeeService;
    private final Debouncer searchDebouncer = new Debouncer(300);
    private ObservableList<EmployeeListDTO> employeeData;

    @FXML private TextField searchField;
    @FXML private TableView<EmployeeListDTO> employeeTable;
    @FXML private TableColumn<EmployeeListDTO, String> surnameColumn;
    @FXML private TableColumn<EmployeeListDTO, String> nameColumn;
    @FXML private TableColumn<EmployeeListDTO, String> roleColumn;
    @FXML private TableColumn<EmployeeListDTO, String> phoneColumn;

    @FXML private VBox filterPanel;
    @FXML private TextField phoneFilterField;
    @FXML private ComboBox<EmployeeRole> roleComboBox;

    @Inject
    public EmployeeListController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @FXML
    public void initialize() {
        initializeTable();
        initializeFilter();
        loadAllEmployees();
    }

    private void initializeTable() {
        employeeData = FXCollections.observableArrayList();

        surnameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().surname()));
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().name()));
        roleColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().role().name()));
        phoneColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().phoneNumber()));

        employeeTable.setItems(employeeData);
        setupTableDoubleClick();
    }

    private void initializeFilter() {
        searchField.textProperty().addListener((_,_,_) ->
                searchDebouncer.debounce(this::applyFilter));
        phoneFilterField.textProperty().addListener((_,_,_) ->
                searchDebouncer.debounce(this::applyFilter));
        List<EmployeeRole> roles = new ArrayList<>(List.of(EmployeeRole.values()));
        roles.addFirst(null);
        roleComboBox.getItems().addAll(roles);
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
                    navigateToDetails(details);
                    loadAllEmployees();
                },
                employeeTable
        );
    }

    @FXML
    public void onAddEmployeeClick() {
        navigateToForm();
        loadAllEmployees();
    }

    @FXML
    public void onExportClick() {
        AsyncRunner.runAsync(
                employeeService::getAllEmployeeDetails,
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
        EmployeeFilter filter = buildFilter();
        setProgressIndicator();

        if (filter.isEmpty()) {
            AsyncRunner.runAsync(
                    employeeService::getAllEmployees,
                    this::updateTableData,
                    employeeTable
            );
        } else {
            AsyncRunner.runAsync(
                    () -> employeeService.getEmployeesByFilter(filter),
                    this::updateTableData,
                    employeeTable
            );
        }
    }

    @FXML
    public void onClearFilterClick() {
        searchField.clear();
        phoneFilterField.clear();
        roleComboBox.setValue(null);
        applyFilter();
    }

   private EmployeeFilter buildFilter() {
        String surname = null;
        String name = null;
        String searchText = InputHelper.getString(searchField);

        if (searchText != null) {
            String[] parts = searchText.split("\\s+");
            surname = parts[0];
            if (parts.length > 1) name = parts[1];
        }

        return EmployeeFilter.builder()
                .surname(surname)
                .name(name)
                .phoneNumber(InputHelper.getString(phoneFilterField))
                .role(roleComboBox.getValue())
                .build();
    }

    private void loadAllEmployees() {
        setProgressIndicator();
        AsyncRunner.runAsync(
                employeeService::getAllEmployees,
                this::updateTableData,
                employeeTable
        );
    }

    private void updateTableData(List<EmployeeListDTO> data) {
        employeeData.setAll(data);
        removeProgressIndicator();
    }

    private void navigateToDetails(EmployeeDetailsDTO details) {
        viewManager.showDialog(AppView.EMPLOYEE_DETAILS,
                (EmployeeDetailsController controller) ->
                        controller.setDetails(details));
    }

    private void navigateToForm() {
        viewManager.showDialog(AppView.EMPLOYEE_FORM);
    }

    private void navigateToReport(List<EmployeeDetailsDTO> employees) {
        viewManager.showDialog(AppView.EMPLOYEE_REPORT,
                (EmployeeReportController controller) ->
                        controller.setData(employees));
    }
}