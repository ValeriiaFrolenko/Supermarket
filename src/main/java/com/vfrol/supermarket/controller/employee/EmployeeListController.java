package com.vfrol.supermarket.controller.employee;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.BaseListController;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.dto.employee.EmployeeListDTO;
import com.vfrol.supermarket.enums.EmployeeRole;
import com.vfrol.supermarket.filter.EmployeeFilter;
import com.vfrol.supermarket.service.EmployeeService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
        String searchText = searchField.getText();

        if (searchText != null && !searchText.isBlank()) {
            String[] parts = searchText.trim().split("\\s+");
            surname = (parts[0] != null && !parts[0].isBlank()) ? parts[0].trim() : null;

            if (parts.length > 1) {
                name = (parts[1] != null && !parts[1].isBlank()) ? parts[1].trim() : null;
            }
        }

        String phoneText = phoneFilterField.getText();
        String phone = (phoneText != null && !phoneText.isBlank()) ? phoneText.trim() : null;

        EmployeeFilter filter = EmployeeFilter.builder()
                .surname(surname)
                .name(name)
                .phoneNumber(phone)
                .role(roleComboBox.getValue())
                .build();

        List<EmployeeListDTO> filteredEmployees = employeeListService.getEmployeesByFilter(filter);
        employeeData.clear();
        employeeData.addAll(filteredEmployees);
    }

    @FXML
    public void onClearFilterClick() {
        searchField.clear();
        phoneFilterField.clear();
        roleComboBox.setValue(null);
        applyFilter();
    }

    private void loadEmployees() {
        List<EmployeeListDTO> employees = employeeListService.getAllEmployees();
        employeeData.clear();
        employeeData.addAll(employees);
    }

    private void showEmployeeDetails(String employeeId) {
        EmployeeDetailsDTO details = employeeListService.getEmployeeById(employeeId);
        if (details != null) {
            viewManager.showDialog(AppView.EMPLOYEE_DETAILS, (EmployeeDetailsController controller) -> {
                controller.setEmployeeDetails(details);
            });
        }
        refreshEmployees();
    }

    public void refreshEmployees() {
        loadEmployees();
    }

    public void onAddEmployeeClick() {
        viewManager.showDialog(AppView.EMPLOYEE_FORM);
        refreshEmployees();
    }
}