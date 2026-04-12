package com.vfrol.supermarket.controller.check;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseDetailsController;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.controller.util.SessionUIHelper;
import com.vfrol.supermarket.dto.check.CheckDetailsDTO;
import com.vfrol.supermarket.dto.sale.SaleListDTO;
import com.vfrol.supermarket.service.CheckService;
import com.vfrol.supermarket.service.SaleService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;

public class CheckDetailsController extends BaseDetailsController<CheckDetailsDTO> {

    private final CheckService checkService;
    private final SaleService saleService;
    private final ObservableList<SaleListDTO> salesData = FXCollections.observableArrayList();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML private Label checkNumberLabel;
    @FXML private Label cashierLabel;
    @FXML private Label customerLabel;
    @FXML private Label dateLabel;
    @FXML private Label sumTotalLabel;
    @FXML private Label vatLabel;

    @FXML private TableView<SaleListDTO> salesTable;
    @FXML private TableColumn<SaleListDTO, String> upcColumn;
    @FXML private TableColumn<SaleListDTO, String> productColumn;
    @FXML private TableColumn<SaleListDTO, Number> quantityColumn;
    @FXML private TableColumn<SaleListDTO, Number> priceColumn;
    @FXML private TableColumn<SaleListDTO, Number> totalColumn;

    @FXML private Button deleteButton;

    @Inject
    public CheckDetailsController(CheckService checkService, SaleService saleService) {
        this.checkService = checkService;
        this.saleService = saleService;
    }

    @FXML
    public void initialize() {
        SessionUIHelper.configureManagerOnlyNodes(sessionManager, deleteButton);
        initializeTable();
    }

    private void initializeTable() {
        upcColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().UPC()));
        productColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().productName()));
        quantityColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().quantity()));
        priceColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().unitPrice()));
        totalColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().totalPrice()));
        salesTable.setItems(salesData);
    }

    @Override
    protected String getEntityName() {
        return "Check";
    }

    @Override
    protected void populateFields(CheckDetailsDTO check) {
        checkNumberLabel.setText(check.checkNumber());
        cashierLabel.setText(check.employeeName());

        String customerInfo = check.customerName() != null ? check.customerName() : "No card used";
        if (check.cardNumber() != null) customerInfo += " (" + check.cardNumber() + ")";
        customerLabel.setText(customerInfo);

        dateLabel.setText(check.dateTime() != null ? check.dateTime().format(DATE_FORMATTER) : "");
        vatLabel.setText(String.format("%.2f ₴", check.vat()));

        if (check.discountAmount() > 0.01) {
            sumTotalLabel.setText(String.format(
                    "%.2f ₴  (Without discount: %.2f ₴, Discount: %.2f ₴)",
                    check.sumTotal(), check.baseSum(), check.discountAmount()
            ));
        } else {
            sumTotalLabel.setText(String.format("%.2f ₴", check.sumTotal()));
        }

        loadSalesForCheck(check.checkNumber());
    }

    private void loadSalesForCheck(String checkNumber) {
        AsyncRunner.runAsync(
                () -> saleService.getSalesByCheckNumber(checkNumber),
                sales -> {
                    salesData.clear();
                    salesData.addAll(sales);
                },
                salesTable
        );
    }

    @Override
    protected void deleteEntity() {
        checkService.deleteCheckByNumber(currentItem.checkNumber());
    }

    @Override
    protected void navigateToForm() {
        throw new UnsupportedOperationException("Checks cannot be edited after creation.");
    }
}