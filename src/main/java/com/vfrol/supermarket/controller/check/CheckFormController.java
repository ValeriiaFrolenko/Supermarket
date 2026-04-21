package com.vfrol.supermarket.controller.check;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseFormController;
import com.vfrol.supermarket.controller.ui_validator.CheckFormValidator;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.controller.util.SearchableComboBoxHelper;
import com.vfrol.supermarket.dto.check.CheckCreateDTO;
import com.vfrol.supermarket.dto.check.CheckDetailsDTO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardListDTO;
import com.vfrol.supermarket.dto.sale.SaleCreateDTO;
import com.vfrol.supermarket.service.CheckService;
import com.vfrol.supermarket.service.CustomerCardService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Setter;
import org.controlsfx.control.SearchableComboBox;

import java.util.List;
import java.util.stream.Collectors;

public class CheckFormController extends BaseFormController<CheckCreateDTO, CheckDetailsDTO> {

    private final CheckService checkService;
    private final CustomerCardService customerCardService;
    private final ObservableList<SaleItemModel> salesData = FXCollections.observableArrayList();

    @FXML private TextField checkNumberField;
    @FXML private SearchableComboBox<CustomerCardListDTO> customerCardComboBox;
    @FXML private CheckBox useDiscount;

    @FXML private TableView<SaleItemModel> salesTable;
    @FXML private TableColumn<SaleItemModel, String> upcColumn;
    @FXML private TableColumn<SaleItemModel, String> nameColumn;
    @FXML private TableColumn<SaleItemModel, Number> priceColumn;
    @FXML private TableColumn<SaleItemModel, Number> quantityColumn;
    @FXML private TableColumn<SaleItemModel, Number> totalColumn;

    @FXML private Label sumTotalLabel;
    @FXML private Label vatLabel;

    @Inject
    public CheckFormController(CheckService checkService, CustomerCardService customerCardService) {
        this.checkService = checkService;
        this.customerCardService = customerCardService;
    }

    @FXML
    @Override
    public void initialize() {
        super.initialize();
        initializeTable();
        configureCustomerCard();
        configureDiscountElements(false);
        useDiscount.selectedProperty().addListener((_,_,_) ->
                recalculateTotals());
    }

    private void initializeTable() {
        upcColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().upc()));
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().productName()));
        priceColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().price()));
        quantityColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().quantity()));
        totalColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getTotal()));
        salesTable.setItems(salesData);
    }

    private void configureCustomerCard() {
        SearchableComboBoxHelper.configureForForm(
                customerCardComboBox,
                customerCardService::getAllCards,
                card -> card.surname() + " " + card.name() + " [" + card.cardNumber() + "]",
                CustomerCardListDTO::cardNumber
        );
        customerCardComboBox.valueProperty().addListener((_,_,_) ->
                onCustomerCardChanged());
    }

    @Override
    protected String getEntityName() {
        return "Check";
    }

    @Override
    protected void setupValidation() {
        CheckFormValidator checkValidator = new CheckFormValidator(validator);
        checkValidator.validateCheckNumber(checkNumberField);
    }

    @Override
    protected void populateFields(CheckDetailsDTO dto) {
        throw new UnsupportedOperationException("Checks cannot be edited after creation.");
    }

    @Override
    protected CheckCreateDTO buildDTO() {
        List<SaleCreateDTO> saleDTOs = salesData.stream()
                .map(item -> new SaleCreateDTO(item.upc(), InputHelper.getString(checkNumberField), item.quantity()))
                .collect(Collectors.toList());

        return CheckCreateDTO.builder()
                .checkNumber(InputHelper.getString(checkNumberField))
                .cardNumber(customerCardComboBox.getValue() != null ? customerCardComboBox.getValue().cardNumber() : null)
                .idEmployee(sessionManager.getCurrentUser().id())
                .useDiscount(useDiscount.isSelected())
                .sales(saleDTOs)
                .build();
    }

    @Override
    protected void saveEntity(CheckCreateDTO dto) {
        checkService.createCheck(dto);
    }

    @FXML
    public void onAddSale() {
        navigateToSaleForm();
    }

    private void navigateToSaleForm() {
        viewManager.showDialog(AppView.SALE_FORM, (SaleFormController controller) ->
                controller.setSaveCallback(this::addSaleItem));
    }

    @FXML
    public void onRemoveSale() {
        SaleItemModel selected = salesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            salesData.remove(selected);
            recalculateTotals();
        }
    }


    private void onCustomerCardChanged() {
        configureDiscountElements(
                customerCardComboBox.getValue() != null
                        && customerCardComboBox.getValue().discount() > 0);
        recalculateTotals();
    }

    private void configureDiscountElements(boolean isDiscountPresent) {
        useDiscount.setVisible(isDiscountPresent);
        useDiscount.setManaged(isDiscountPresent);
        int discount = customerCardComboBox.getValue() != null ? customerCardComboBox.getValue().discount() : 0;
        useDiscount.setText("Apply discount (" + discount + "%)");
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

    private void recalculateTotals() {
        CustomerCardListDTO cardDto = customerCardComboBox.getValue();
        boolean discountSelected = useDiscount.isSelected();

        AsyncRunner.runAsync(
                () -> {
                    double total = salesData.stream().mapToDouble(SaleItemModel::getTotal).sum();
                    if (discountSelected && cardDto != null) {
                        total = total * (1.0 - (cardDto.discount() / 100.0));
                    }
                    return total;
                },
                total -> {
                    sumTotalLabel.setText(String.format("%.2f", total));
                    vatLabel.setText(String.format("%.2f", total * 0.20));
                },
                null
        );
    }

    public static class SaleItemModel {
        private final String upc;
        private final String productName;
        private final double price;
        @Setter private int quantity;

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