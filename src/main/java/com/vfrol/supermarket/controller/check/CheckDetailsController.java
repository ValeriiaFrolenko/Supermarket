package com.vfrol.supermarket.controller.check;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseModalController;
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
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CheckDetailsController extends BaseModalController {

    @FXML private VBox detailsPanel;

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

    private CheckDetailsDTO currentCheck;
    private final CheckService checkService;
    private final SaleService saleService;

    private final ObservableList<SaleListDTO> salesData = FXCollections.observableArrayList();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    public void setCheckDetails(CheckDetailsDTO check) {
        this.currentCheck = check;

        checkNumberLabel.setText(check.checkNumber());
        cashierLabel.setText(check.employeeName());

        String customerInfo = check.customerName() != null ? check.customerName() : "No card used";
        if (check.cardNumber() != null) {
            customerInfo += " (" + check.cardNumber() + ")";
        }
        customerLabel.setText(customerInfo);

        dateLabel.setText(check.dateTime() != null ? check.dateTime().format(DATE_FORMATTER) : "");
        sumTotalLabel.setText(String.format("%.2f ₴", check.sumTotal()));
        vatLabel.setText(String.format("%.2f ₴", check.vat()));

        loadSalesForCheck(check.checkNumber());
    }

    private void loadSalesForCheck(String checkNumber) {
        try {
            List<SaleListDTO> sales = saleService.getSalesByCheckNumber(checkNumber);
            salesData.setAll(sales);
        } catch (Exception e) {
            System.err.println("Could not load sales list: " + e.getMessage());
        }
    }

    @FXML
    public void onDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this check? This will also remove all associated sales records.",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                checkService.deleteCheckByNumber(currentCheck.checkNumber());
                hide();
            }
        });
    }

    @FXML
    public void hide() {
        closeWindow(detailsPanel);
    }
}