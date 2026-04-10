package com.vfrol.supermarket.controller.product;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseListController;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.controller.util.Debouncer;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.controller.util.SearchableComboBoxHelper;
import com.vfrol.supermarket.controller.util.SessionUIHelper;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.dto.product.ProductDetailsDTO;
import com.vfrol.supermarket.dto.product.ProductListDTO;
import com.vfrol.supermarket.enums.sortby.ProductSortBy;
import com.vfrol.supermarket.filter.ProductFilter;
import com.vfrol.supermarket.service.CategoryService;
import com.vfrol.supermarket.service.ProductService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class ProductListController extends BaseListController<ProductListDTO> {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final Debouncer searchDebouncer = new Debouncer(300);
    private ObservableList<ProductListDTO> productData;

    @FXML private TextField searchField;
    @FXML private TableView<ProductListDTO> productTable;
    @FXML private TableColumn<ProductListDTO, String> nameColumn;
    @FXML private TableColumn<ProductListDTO, String> categoryColumn;
    @FXML private Button addButton;

    @FXML private VBox filterPanel;
    @FXML private ComboBox<CategoryListDTO> categoryFilterComboBox;
    @FXML private ComboBox<ProductSortBy> sortByComboBox;

    @Inject
    public ProductListController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @FXML
    public void initialize() {
        SessionUIHelper.configureManagerOnlyNodes(sessionManager, addButton);
        initializeTable();
        initializeFilters();
        loadAllProducts();
    }

    private void initializeTable() {
        productData = FXCollections.observableArrayList();

        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().name()));
        categoryColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().categoryName()));

        productTable.setItems(productData);
        setupTableDoubleClick();
    }

    private void initializeFilters() {
        searchField.textProperty().addListener((_,_,_) ->
                searchDebouncer.debounce(this::applyFilter));
        categoryFilterComboBox.valueProperty().addListener((_,_,_) ->
                applyFilter());
        sortByComboBox.valueProperty().addListener((_,_,_) ->
                applyFilter());

        sortByComboBox.getItems().addAll(ProductSortBy.values());

        SearchableComboBoxHelper.configure(
                categoryFilterComboBox,
                categoryService::getAllCategories,
                categoryService::getCategoriesByName,
                CategoryListDTO::name
        );
    }

    @Override
    protected TableView<ProductListDTO> getTableView() {
        return productTable;
    }

    @Override
    protected void showDetails(ProductListDTO item) {
        AsyncRunner.runAsync(
                () -> productService.getProductById(item.id()),
                details -> {
                    navigateToDetails(details);
                    loadAllProducts();
                },
                productTable
        );
    }

    @FXML
    public void onAddProductClick() {
        navigateToForm();
        loadAllProducts();
    }

    @FXML
    public void onExportClick() {
        AsyncRunner.runAsync(
                productService::getAllProductDetails,
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
        ProductFilter filter = buildFilter();
        setProgressIndicator();

        if (filter.isEmpty()) {
            AsyncRunner.runAsync(
                    productService::getAllProducts,
                    this::updateTableData,
                    productTable
            );
        } else {
            AsyncRunner.runAsync(
                    () -> productService.getProductsByFilter(filter),
                    this::updateTableData,
                    productTable
            );
        }
    }

    @FXML
    public void onClearFilterClick() {
        searchField.clear();
        categoryFilterComboBox.setValue(null);
        categoryFilterComboBox.getEditor().clear();
        sortByComboBox.setValue(null);
        applyFilter();
    }

    private ProductFilter buildFilter() {
        return ProductFilter.builder()
                .name(InputHelper.getString(searchField))
                .categoryId(categoryFilterComboBox.getValue() != null ? categoryFilterComboBox.getValue().id() : null)
                .sortBy(sortByComboBox.getValue())
                .build();
    }

    private void loadAllProducts() {
        setProgressIndicator();
        AsyncRunner.runAsync(
                productService::getAllProducts,
                this::updateTableData,
                productTable
        );
    }

    private void updateTableData(List<ProductListDTO> data) {
        productData.setAll(data);
        removeProgressIndicator();
    }

    private void navigateToDetails(ProductDetailsDTO details) {
        viewManager.showDialog(AppView.PRODUCT_DETAILS,
                (ProductDetailsController controller) ->
                        controller.setDetails(details));
    }

    private void navigateToForm() {
        viewManager.showDialog(AppView.PRODUCT_FORM);
    }

    private void navigateToReport(List<ProductDetailsDTO> products) {
        viewManager.showDialog(AppView.PRODUCT_REPORT,
                (ProductReportController controller) ->
                        controller.setData(products));
    }
}