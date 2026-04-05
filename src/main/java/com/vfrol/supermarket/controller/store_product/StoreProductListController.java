package com.vfrol.supermarket.controller.store_product;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.config.ViewManager;
import com.vfrol.supermarket.dto.product.ProductNameDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductListDTO;
import com.vfrol.supermarket.enums.sortby.StoreProductSortBy;
import com.vfrol.supermarket.filter.StoreProductFilter;
import com.vfrol.supermarket.service.ProductService;
import com.vfrol.supermarket.service.StoreProductService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class StoreProductListController {

    private final StoreProductService storeProductService;
    private final ProductService productService;
    private final ViewManager viewManager;
    private final SessionManager sessionManager;

    @FXML private TextField searchField;
    @FXML private Button addButton;

    @FXML private TableView<StoreProductListDTO> storeProductTable;
    @FXML private TableColumn<StoreProductListDTO, String> upcColumn;
    @FXML private TableColumn<StoreProductListDTO, String> nameColumn;
    @FXML private TableColumn<StoreProductListDTO, Number> priceColumn;
    @FXML private TableColumn<StoreProductListDTO, Number> quantityColumn;
    @FXML private TableColumn<StoreProductListDTO, Boolean> promotionalColumn;

    @FXML private VBox filterPanel;
    @FXML private ComboBox<ProductNameDTO> productFilterComboBox;
    @FXML private ComboBox<Boolean> promotionalFilterComboBox;
    @FXML private ComboBox<StoreProductSortBy> sortByComboBox;

    private ObservableList<StoreProductListDTO> storeProductData;

    @Inject
    public StoreProductListController(StoreProductService storeProductService,
                                      ProductService productService,
                                      ViewManager viewManager,
                                      SessionManager sessionManager) {
        this.storeProductService = storeProductService;
        this.productService = productService;
        this.viewManager = viewManager;
        this.sessionManager = sessionManager;
    }

    @FXML
    public void initialize() {
        configureForRole();
        initializeTable();
        initializeFilters();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        loadStoreProducts();
    }

    private void configureForRole() {
        addButton.setVisible(sessionManager.isManager());
        addButton.setManaged(sessionManager.isManager());
    }

    private void initializeTable() {
        storeProductData = FXCollections.observableArrayList();

        upcColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().UPC()));
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().productName()));
        priceColumn.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().price()));
        quantityColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().quantity()));
        promotionalColumn.setCellValueFactory(cell -> new SimpleBooleanProperty(
                cell.getValue().promotional() != null && cell.getValue().promotional()));

        storeProductTable.setItems(storeProductData);

        storeProductTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                StoreProductListDTO selected = storeProductTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    showStoreProductDetails(selected.UPC());
                }
            }
        });
    }

    private void initializeFilters() {
        SearchableProductComboBoxHelper.configure(productFilterComboBox, productService);
    }

    @FXML
    public void applyFilter() {
        String searchText = searchField.getText();
        String upc = (searchText != null && !searchText.isBlank()) ? searchText.trim() : null;

        ProductNameDTO selectedProduct = productFilterComboBox.getValue();
        String filterProductName = (selectedProduct != null) ? selectedProduct.name() : null;
        StoreProductFilter filter = StoreProductFilter.builder()
                .upc(upc)
                .productName(filterProductName)
                .promotional(promotionalFilterComboBox.getValue())
                .sortBy(sortByComboBox.getValue())
                .build();

        storeProductData.setAll(storeProductService.getStoreProductsByFilter(filter));
    }

    @FXML
    public void onClearFilterClick() {
        searchField.clear();
        productFilterComboBox.setValue(null);
        productFilterComboBox.getEditor().clear();
        productFilterComboBox.getItems().clear();
        promotionalFilterComboBox.setValue(null);
        sortByComboBox.setValue(null);
        loadStoreProducts();
    }

    @FXML
    public void onToggleFilterClick() {
        boolean isCurrentlyVisible = filterPanel.isVisible();
        filterPanel.setVisible(!isCurrentlyVisible);
        filterPanel.setManaged(!isCurrentlyVisible);
    }

    private void loadStoreProducts() {
        storeProductData.setAll(storeProductService.getAllStoreProducts());
    }

    private void showStoreProductDetails(String upc) {
        StoreProductDetailsDTO details = storeProductService.getStoreProductByUpc(upc);
        if (details != null) {
            viewManager.showDialog(AppView.STORE_PRODUCT_DETAILS, (StoreProductDetailsController controller) ->
                    controller.setStoreProductDetails(details));
        }
        applyFilter();
    }

    @FXML
    public void onAddStoreProductClick() {
        viewManager.showDialog(AppView.STORE_PRODUCT_FORM);
        applyFilter();
    }
}