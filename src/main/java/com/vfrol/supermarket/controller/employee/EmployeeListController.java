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

import java.util.List;

public class EmployeeListController extends BaseListController<EmployeeListDTO> {

    private final EmployeeService employeeListService;

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
        this.employeeListService = employeeService;
    }

    @Override
    protected TableView<EmployeeListDTO> getTableView() {
        return employeeTable;
    }

    @Override
    protected void showDetails(EmployeeListDTO item) {
        showEmployeeDetails(item.id());
    }

    @FXML
    public void initialize() {
        initializeTable();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchDebouncer.debounce(this::applyFilter);
        });

        phoneFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchDebouncer.debounce(this::applyFilter);
        });

        roleComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });

        roleComboBox.getItems().addAll(EmployeeRole.values());
        loadEmployees();
    }

    private void initializeTable() {
        employeeData = FXCollections.observableArrayList();
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name()));
        surnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().surname()));
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().role().name()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().phoneNumber()));
        employeeTable.setItems(employeeData);
        setupTableDoubleClick();
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
            if (parts.length > 1) {
                name = parts[1];
            }
        }

        EmployeeFilter filter = EmployeeFilter.builder()
                .surname(surname)
                .name(name)
                .phoneNumber(InputHelper.getString(phoneFilterField))
                .role(roleComboBox.getValue())
                .build();

        employeeTable.setPlaceholder(new ProgressIndicator());

        AsyncRunner.runAsync(
                () -> employeeListService.getEmployeesByFilter(filter),
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

    private void loadEmployees() {
        employeeTable.setPlaceholder(new ProgressIndicator());

        AsyncRunner.runAsync(
                employeeListService::getAllEmployees,
                this::updateTableData,
                employeeTable
        );
    }

    private void updateTableData(List<EmployeeListDTO> data) {
        employeeData.setAll(data);
        employeeTable.setPlaceholder(new Label("No content in table"));
    }

    private void showEmployeeDetails(String employeeId) {
        AsyncRunner.runAsync(
                () -> employeeListService.getEmployeeById(employeeId),
                details -> {
                    if (details != null) {
                        viewManager.showDialog(AppView.EMPLOYEE_DETAILS, (EmployeeDetailsController controller) -> {
                            controller.setEmployeeDetails(details);
                        });
                    }
                    refreshEmployees();
                },
                employeeTable
        );
    }

    public void refreshEmployees() {
        loadEmployees();
    }

    public void onAddEmployeeClick() {
        viewManager.showDialog(AppView.EMPLOYEE_FORM);
        refreshEmployees();
    }
}