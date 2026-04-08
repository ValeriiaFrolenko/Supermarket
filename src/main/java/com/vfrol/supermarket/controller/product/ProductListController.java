package com.vfrol.supermarket.controller.product;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseListController;
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

public class ProductListController extends BaseListController<ProductListDTO> {

    private final ProductService productService;
    private final CategoryService categoryService;

    @FXML private TextField searchField;
    @FXML private Button addButton;
    @FXML private TableView<ProductListDTO> productTable;
    @FXML private TableColumn<ProductListDTO, String> nameColumn;
    @FXML private TableColumn<ProductListDTO, String> categoryColumn;

    @FXML private VBox filterPanel;
    @FXML private ComboBox<CategoryListDTO> categoryFilterComboBox;
    @FXML private ComboBox<ProductSortBy> sortByComboBox;

    private ObservableList<ProductListDTO> productData;

    @Inject
    public ProductListController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Override
    protected TableView<ProductListDTO> getTableView() {
        return productTable;
    }

    @Override
    protected void showDetails(ProductListDTO item) {
        showProductDetails(item.id());
    }

    @FXML
    public void initialize() {
        SessionUIHelper.configureManagerOnlyNodes(sessionManager, addButton);
        initializeTable();
        initializeFilters();

        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                applyFilter());

        loadProducts();
    }

    private void initializeTable() {
        productData = FXCollections.observableArrayList();
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().name()));
        categoryColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().categoryName()));
        productTable.setItems(productData);

        setupTableDoubleClick();
    }

    private void initializeFilters() {
        sortByComboBox.getItems().setAll(ProductSortBy.values());
        refreshCategories();

        filterPanel.visibleProperty().addListener((obs, wasVisible, isNowVisible) -> {
            if (isNowVisible) {
                refreshCategories();
            }
        });

        categoryFilterComboBox.setOnShowing(e -> refreshCategories());
    }

    private void refreshCategories() {
        Integer selectedId = categoryFilterComboBox.getValue() != null
                ? categoryFilterComboBox.getValue().id()
                : null;

        categoryFilterComboBox.setItems(
                FXCollections.observableArrayList(categoryService.getAllCategories())
        );

        if (selectedId != null) {
            categoryFilterComboBox.setValue(categoryService.getCategoryById(selectedId));
        }
    }

    @FXML
    public void onToggleFilterClick() {
        toggleFilterPanel(filterPanel);
    }

    @FXML
    public void onClearFilterClick() {
        searchField.clear();
        categoryFilterComboBox.setValue(null);
        sortByComboBox.setValue(null);
        applyFilter();
    }

    @FXML
    public void applyFilter() {
        String searchText = searchField.getText();
        if (searchText != null && searchText.isBlank()) {
            searchText = null;
        }

        Integer categoryId = null;
        if (categoryFilterComboBox.getValue() != null) {
            categoryId = categoryFilterComboBox.getValue().id();
        }

        ProductFilter filter = ProductFilter.builder()
                .name(searchText)
                .categoryId(categoryId)
                .sortBy(sortByComboBox.getValue())
                .build();

        productData.setAll(productService.getProductsByFilter(filter));
    }

    private void showProductDetails(int productId) {
        ProductDetailsDTO details = productService.getProductById(productId);
        if (details != null) {
            viewManager.showDialog(AppView.PRODUCT_DETAILS, (ProductDetailsController controller) ->
                    controller.setProductDetails(details));
        }
        applyFilter();
    }

    private void loadProducts() {
        productData.setAll(productService.getAllProducts());
    }

    @FXML
    public void onAddProductClick() {
        viewManager.showDialog(AppView.PRODUCT_FORM);
        applyFilter();
    }
}