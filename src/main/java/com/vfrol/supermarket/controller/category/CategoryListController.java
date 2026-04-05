package com.vfrol.supermarket.controller.category;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.BaseListController;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.service.CategoryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class CategoryListController extends BaseListController<CategoryListDTO> {

    private final CategoryService categoryService;

    @FXML private TextField searchField;
    @FXML private TableView<CategoryListDTO> categoryTable;
    @FXML private TableColumn<CategoryListDTO, String> nameColumn;

    private ObservableList<CategoryListDTO> categoryData;

    @Inject
    public CategoryListController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    protected TableView<CategoryListDTO> getTableView() {
        return categoryTable;
    }

    @Override
    protected void showDetails(CategoryListDTO item) {
        showCategoryDetails(item);
    }

    @FXML
    public void initialize() {
        initializeTable();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        loadCategories();
    }

    private void initializeTable() {
        categoryData = FXCollections.observableArrayList();
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().name()));
        categoryTable.setItems(categoryData);
        setupTableDoubleClick();
    }

    private void showCategoryDetails(CategoryListDTO categoryListDTO) {
        if (categoryListDTO != null) {
            viewManager.showDialog(AppView.CATEGORY_DETAILS, (CategoryDetailsController controller) -> {
                controller.setCategoryDetails(categoryListDTO);
            });
        }
        loadCategories();
    }

    private void applyFilter() {
        String query = searchField.getText().trim();
        List<CategoryListDTO> filtered = query.isBlank()
                ? categoryService.getAllCategories()
                : categoryService.getCategoriesByName(query);
        categoryData.setAll(filtered);
    }

    private void loadCategories() {
        categoryData.setAll(categoryService.getAllCategories());
    }

    public void onAddCategoryClick(ActionEvent actionEvent) {
        viewManager.showDialog(AppView.CATEGORY_FORM);
        loadCategories();
    }
}