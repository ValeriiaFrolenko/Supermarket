package com.vfrol.supermarket.controller.store_product;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseListController;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.controller.util.Debouncer;
import com.vfrol.supermarket.controller.util.InputHelper;
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

import java.util.List;

public class StoreProductListController extends BaseListController<StoreProductListDTO> {

    private final StoreProductService storeProductService;
    private final ProductService productService;
    private final Debouncer searchDebouncer = new Debouncer(300);

    private ObservableList<StoreProductListDTO> storeProductData;

    @FXML private TextField searchField;
    @FXML private Button addButton;

    @FXML private TableView<StoreProductListDTO> storeProductTable;
    @FXML private TableColumn<StoreProductListDTO, String>  upcColumn;
    @FXML private TableColumn<StoreProductListDTO, String>  nameColumn;
    @FXML private TableColumn<StoreProductListDTO, Number>  priceColumn;
    @FXML private TableColumn<StoreProductListDTO, Number>  quantityColumn;
    @FXML private TableColumn<StoreProductListDTO, Boolean> promotionalColumn;

    @FXML private VBox filterPanel;
    @FXML private ComboBox<ProductNameDTO>    productFilterComboBox;
    @FXML private ComboBox<Boolean>           promotionalFilterComboBox;
    @FXML private ComboBox<StoreProductSortBy> sortByComboBox;

    @Inject
    public StoreProductListController(StoreProductService storeProductService,
                                      ProductService productService) {
        this.storeProductService = storeProductService;
        this.productService = productService;
    }

    @FXML
    public void initialize() {
        SessionUIHelper.configureManagerOnlyNodes(sessionManager, addButton);
        initializeTable();
        initializeFilters();
        loadAllStoreProducts();
    }

    private void initializeTable() {
        storeProductData = FXCollections.observableArrayList();

        upcColumn.setCellValueFactory(cell        -> new SimpleStringProperty(cell.getValue().UPC()));
        nameColumn.setCellValueFactory(cell       -> new SimpleStringProperty(cell.getValue().productName()));
        priceColumn.setCellValueFactory(cell      -> new SimpleDoubleProperty(cell.getValue().price()));
        quantityColumn.setCellValueFactory(cell   -> new SimpleIntegerProperty(cell.getValue().quantity()));
        promotionalColumn.setCellValueFactory(cell -> new SimpleBooleanProperty(
                cell.getValue().promotional() != null && cell.getValue().promotional()));

        storeProductTable.setItems(storeProductData);
        setupTableDoubleClick();
    }

    private void initializeFilters() {
        searchField.textProperty().addListener((_, _, _) ->
                searchDebouncer.debounce(this::applyFilter));
        promotionalFilterComboBox.valueProperty().addListener((_, _, _) ->
                applyFilter());
        sortByComboBox.valueProperty().addListener((_, _, _) ->
                applyFilter());
        productFilterComboBox.valueProperty().addListener((_, _, _) ->
                applyFilter());

        promotionalFilterComboBox.getItems().setAll(true, false);
        sortByComboBox.getItems().setAll(StoreProductSortBy.values());

        SearchableComboBoxHelper.configure(
                productFilterComboBox,
                productService::getAllProductNames,
                productService::getProductByName,
                ProductNameDTO::name
        );
    }

    @Override
    protected TableView<StoreProductListDTO> getTableView() {
        return storeProductTable;
    }

    @Override
    protected void showDetails(StoreProductListDTO item) {
        AsyncRunner.runAsync(
                () -> storeProductService.getStoreProductByUpc(item.UPC()),
                details -> {
                    navigateToDetails(details);
                    loadAllStoreProducts();
                },
                storeProductTable
        );
    }

    @FXML
    public void onAddStoreProductClick() {
        navigateToForm();
        loadAllStoreProducts();
    }

    @FXML
    public void onExportClick() {
        AsyncRunner.runAsync(
                storeProductService::getAllStoreProductDetails,
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
        StoreProductFilter filter = buildFilter();
        setProgressIndicator();

        if (filter.isEmpty()) {
            AsyncRunner.runAsync(
                    storeProductService::getAllStoreProducts,
                    this::updateTableData,
                    storeProductTable
            );
        } else {
            AsyncRunner.runAsync(
                    () -> storeProductService.getStoreProductsByFilter(filter),
                    this::updateTableData,
                    storeProductTable
            );
        }
    }

    @FXML
    public void onClearFilterClick() {
        searchField.clear();
        productFilterComboBox.setValue(null);
        productFilterComboBox.getEditor().clear();
        productFilterComboBox.getItems().clear();
        promotionalFilterComboBox.setValue(null);
        sortByComboBox.setValue(null);
        applyFilter();
    }

    private StoreProductFilter buildFilter() {
        ProductNameDTO selectedProduct = productFilterComboBox.getValue();
        return StoreProductFilter.builder()
                .upc(InputHelper.getString(searchField))
                .productName(selectedProduct != null ? selectedProduct.name() : null)
                .promotional(promotionalFilterComboBox.getValue())
                .sortBy(sortByComboBox.getValue())
                .build();
    }

    private void loadAllStoreProducts() {
        setProgressIndicator();
        AsyncRunner.runAsync(
                storeProductService::getAllStoreProducts,
                this::updateTableData,
                storeProductTable
        );
    }

    private void updateTableData(List<StoreProductListDTO> data) {
        storeProductData.setAll(data);
        removeProgressIndicator();
    }

    private void navigateToDetails(StoreProductDetailsDTO details) {
        viewManager.showDialog(AppView.STORE_PRODUCT_DETAILS,
                (StoreProductDetailsController controller) ->
                        controller.setDetails(details));
    }

    private void navigateToForm() {
        viewManager.showDialog(AppView.STORE_PRODUCT_FORM);
    }

    private void navigateToReport(List<StoreProductDetailsDTO> storeProducts) {
        viewManager.showDialog(AppView.STORE_PRODUCT_REPORT,
                (StoreProductReportController controller) ->
                        controller.setData(storeProducts));
    }
}