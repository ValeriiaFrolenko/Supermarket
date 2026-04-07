package com.vfrol.supermarket.controller.store_product;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.BaseListController;
import com.vfrol.supermarket.controller.util.SearchableComboBoxHelper;
import com.vfrol.supermarket.controller.util.SessionUIHelper;
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

public class StoreProductListController extends BaseListController<StoreProductListDTO> {

    private final StoreProductService storeProductService;
    private final ProductService productService;

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
                                      ProductService productService) {
        this.storeProductService = storeProductService;
        this.productService = productService;
    }

    @Override
    protected TableView<StoreProductListDTO> getTableView() {
        return storeProductTable;
    }

    @Override
    protected void showDetails(StoreProductListDTO item) {
        showStoreProductDetails(item.UPC());
    }

    @FXML
    public void initialize() {
        SessionUIHelper.configureManagerOnlyNodes(sessionManager, addButton);
        initializeTable();
        initializeFilters();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        loadStoreProducts();
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

        setupTableDoubleClick();
    }

    private void initializeFilters() {
        SearchableComboBoxHelper.configure(
                productFilterComboBox,
                productService::getAllProductNames,
                productService::getProductByName,
                ProductNameDTO::name
        );
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
        toggleFilterPanel(filterPanel);
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