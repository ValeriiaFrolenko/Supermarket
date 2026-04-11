package com.vfrol.supermarket.controller.check;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseModalController;
import com.vfrol.supermarket.controller.util.SearchableComboBoxHelper;
import com.vfrol.supermarket.dto.check.CheckCreateDTO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardListDTO;
import com.vfrol.supermarket.dto.employee.EmployeeListDTO;
import com.vfrol.supermarket.dto.sale.SaleCreateDTO;
import com.vfrol.supermarket.filter.CustomerCardFilter;
import com.vfrol.supermarket.filter.EmployeeFilter;
import com.vfrol.supermarket.service.CheckService;
import com.vfrol.supermarket.service.CustomerCardService;
import com.vfrol.supermarket.service.EmployeeService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class CheckFormController extends BaseModalController {

    @FXML private VBox formPanel;
    @FXML private TextField checkNumberField;
    @FXML private ComboBox<EmployeeListDTO> cashierComboBox;
    @FXML private ComboBox<CustomerCardListDTO> customerCardComboBox;
    @FXML private CheckBox useDiscount;

    @FXML private TableView<SaleItemModel> salesTable;
    @FXML private TableColumn<SaleItemModel, String> upcColumn;
    @FXML private TableColumn<SaleItemModel, String> nameColumn;
    @FXML private TableColumn<SaleItemModel, Number> priceColumn;
    @FXML private TableColumn<SaleItemModel, Number> quantityColumn;
    @FXML private TableColumn<SaleItemModel, Number> totalColumn;

    @FXML private Label sumTotalLabel;
    @FXML private Label vatLabel;

    private final CheckService checkService;
    private final EmployeeService employeeService;
    private final CustomerCardService customerCardService;
    private final ObservableList<SaleItemModel> salesData = FXCollections.observableArrayList();

    @Inject
    public CheckFormController(CheckService checkService, EmployeeService employeeService, CustomerCardService customerCardService) {
        this.checkService = checkService;
        this.employeeService = employeeService;
        this.customerCardService = customerCardService;
    }

    @FXML
    public void initialize() {
        configureComboBoxes();
        configureDiscountElements(false);
        useDiscount.selectedProperty().addListener((obs, oldVal, newVal) -> recalculateTotals());
        initializeTable();
    }

    private void configureDiscountElements(Boolean isDiscountPresent) {
        useDiscount.setVisible(isDiscountPresent);
        useDiscount.setManaged(isDiscountPresent);
        useDiscount.setText("Apply discount (" + (customerCardComboBox.getValue() != null ? customerCardComboBox.getValue().discount() : "0") + "%)");
    }

    private void configureComboBoxes() {
        SearchableComboBoxHelper.configure(
                cashierComboBox,
                employeeService::getAllEmployees,
                text -> employeeService.getEmployeesByFilter(EmployeeFilter.builder().surname(text).build()),
                emp -> emp.surname() + " " + emp.name() + " (" + emp.id() + ")"
        );

        SearchableComboBoxHelper.configure(
                customerCardComboBox,
                customerCardService::getAllCards,
                text -> customerCardService.getCardsByFilter(CustomerCardFilter.builder().surname(text).build()),
                card -> card.surname() + " " + card.name() + " [" + card.cardNumber() + "]"
        );
        customerCardComboBox.valueProperty().addListener((obs, oldVal, newVal) -> setUseDiscountVisible());    }

    private void setUseDiscountVisible() {
        if (customerCardComboBox.getValue() != null){
            try {
                CustomerCardListDTO dto = customerCardComboBox.getValue();
                if (dto.discount()>0) {
                    configureDiscountElements(true);
                    recalculateTotals();
                }
            } catch (Exception e) {
                configureDiscountElements(false);
                recalculateTotals();
            }
        }
    }

    private void initializeTable() {
        upcColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().upc()));
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().productName()));
        priceColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().price()));
        quantityColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().quantity()));
        totalColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getTotal()));

        salesTable.setItems(salesData);
    }

    @FXML
    public void onAddSale() {
        viewManager.showDialog(AppView.SALE_FORM, (SaleFormController controller) -> {
            controller.setSaveCallback(this::addSaleItem);
        });
    }

    private void addSaleItem(SaleItemModel newItem) {
        for (SaleItemModel item : salesData) {
            if (item.upc().equals(newItem.upc())) {
                item.setQuantity(item.quantity() + newItem.quantity());
                salesTable.refresh();
                recalculateTotals();
                return;
            }
        }
        salesData.add(newItem);
        recalculateTotals();
    }

    @FXML
    public void onRemoveSale() {
        SaleItemModel selected = salesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            salesData.remove(selected);
            recalculateTotals();
        }
    }

    private void recalculateTotals() {
        double total = salesData.stream().mapToDouble(SaleItemModel::getTotal).sum();
        if (useDiscount.isSelected() && customerCardComboBox.getValue() != null) {
            try {
                CustomerCardListDTO dto = customerCardComboBox.getValue();
                double discountPercent = dto.discount();
                total = total * (1.0 - (discountPercent / 100.0));
            } catch (Exception e) {
                // If there's an error getting the discount, just ignore it and use the original total
            }
        }
        double vat = total * 0.20;

        sumTotalLabel.setText(String.format("%.2f", total));
        vatLabel.setText(String.format("%.2f", vat));
    }

    @FXML
    public void onSave() {
        String checkNumber = checkNumberField.getText().trim();
        EmployeeListDTO cashier = cashierComboBox.getValue();

        if (checkNumber.isBlank() || cashier == null || salesData.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Check number, cashier, and at least one item are required!").showAndWait();
            return;
        }

        String cardNumber = customerCardComboBox.getValue() != null ? customerCardComboBox.getValue().cardNumber() : null;

        List<SaleCreateDTO> saleCreateDTOs = salesData.stream()
                .map(item -> new SaleCreateDTO(item.upc(), checkNumber, item.quantity(), item.price()))
                .collect(Collectors.toList());

        CheckCreateDTO dto = CheckCreateDTO.builder()
                .checkNumber(checkNumber)
                .cardNumber(cardNumber)
                .idEmployee(cashier.id())
                .useDiscount(useDiscount.isSelected())
                .sales(saleCreateDTOs)
                .build();

        try {
            checkService.createCheck(dto);
            closeWindow(formPanel);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to create check: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void onCancel() {
        closeWindow(formPanel);
    }

    public static class SaleItemModel {
        private final String upc;
        private final String productName;
        private final double price;
        @Setter
        private int quantity;

        public SaleItemModel(String upc, String productName, double price, int quantity) {
            this.upc = upc;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
        }

        public String upc() { return upc; }
        public String productName() { return productName; }
        public double price() { return price; }
        public int quantity() { return quantity; }

        public double getTotal() { return price * quantity; }
    }
}