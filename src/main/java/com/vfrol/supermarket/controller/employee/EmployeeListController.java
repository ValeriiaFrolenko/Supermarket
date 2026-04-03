package com.vfrol.supermarket.controller.employee;

import com.google.inject.Inject;
import com.vfrol.supermarket.AppView;
import com.vfrol.supermarket.ViewManager;
import com.vfrol.supermarket.dto.employee.EmployeeCreateDTO;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.dto.employee.EmployeeListDTO;
import com.vfrol.supermarket.enums.EmployeeRole;
import com.vfrol.supermarket.filter.EmployeeFilter;
import com.vfrol.supermarket.service.EmployeeService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.List;

public class EmployeeListController {
    private final EmployeeService employeeListService;
    private final ViewManager viewManager;

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
    public EmployeeListController(EmployeeService employeeService, ViewManager viewManager) {
        this.employeeListService = employeeService;
        this.viewManager = viewManager;
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

        employeeTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                EmployeeListDTO selected = employeeTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showEmployeeDetails(selected.id());
                }
            }
        });
    }

    @FXML
    public void onToggleFilterClick() {
        boolean isCurrentlyVisible = filterPanel.isVisible();
        filterPanel.setVisible(!isCurrentlyVisible);
        filterPanel.setManaged(!isCurrentlyVisible);
    }

    @FXML
    public void applyFilter() {
        String surname = null;
        String name = null;
        String searchText = searchField.getText();

        if (searchText != null && !searchText.isBlank()) {
            String[] parts = searchText.trim().split("\\s+");
            surname = formatName(parts[0]);

            if (parts.length > 1) {
                name = formatName(parts[1]);
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

    private String formatName(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }
        if (input.length() == 1) {
            return input.toUpperCase();
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
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

                // TODO: Replace with actual logged-in user role later
                controller.configureForRole(EmployeeRole.MANAGER);
            });
        }
        refreshEmployees();
    }

    public void refreshEmployees() {
        loadEmployees();
    }

    public void onAddEmployeeClick(ActionEvent actionEvent) {
        viewManager.showDialog(AppView.EMPLOYEE_FORM);
        refreshEmployees();
    }
}