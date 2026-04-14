package com.vfrol.supermarket.controller.category;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseListController;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.controller.util.Debouncer;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.controller.util.SessionUIHelper;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.service.CategoryService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class CategoryListController extends BaseListController<CategoryListDTO> {

    private final CategoryService categoryService;
    private final Debouncer searchDebouncer = new Debouncer(300);
    private ObservableList<CategoryListDTO> categoryData;

    @FXML private TextField searchField;
    @FXML private TableView<CategoryListDTO> categoryTable;
    @FXML private TableColumn<CategoryListDTO, String> nameColumn;

    @Inject
    public CategoryListController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @FXML
    public void initialize() {
        initializeTable();
        SessionUIHelper.configureManagerOnlyNodes(sessionManager);
        searchField.textProperty().addListener((_,_,_) ->
                searchDebouncer.debounce(this::applyFilter));
        loadAllCategories();
    }

    private void initializeTable() {
        categoryData = FXCollections.observableArrayList();
        nameColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().name()));
        categoryTable.setItems(categoryData);
        setupTableDoubleClick();
    }

    @Override
    protected TableView<CategoryListDTO> getTableView() {
        return categoryTable;
    }

    @Override
    protected void showDetails(CategoryListDTO item) {
        AsyncRunner.runAsync(
                () -> categoryService.getCategoryById(item.id()),
                details -> {
                    navigateToDetails(details);
                   loadAllCategories();
                },
                categoryTable
        );
    }

    @FXML
    public void onAddCategoryClick() {
        navigateToForm();
        loadAllCategories();
    }

    @FXML
    public void onExportClick() {
        AsyncRunner.runAsync(
                categoryService::getAllCategories,
                this::navigateToReport,
                getRootNode()
        );
    }

    private void applyFilter() {
        String query = InputHelper.getString(searchField);
        setProgressIndicator();

        if (query == null) {
            AsyncRunner.runAsync(
                    categoryService::getAllCategories,
                    this::updateTableData,
                    categoryTable
            );
        } else {
            AsyncRunner.runAsync(
                    () -> categoryService.getCategoriesByName(query),
                    this::updateTableData,
                    categoryTable
            );
        }
    }

    private void loadAllCategories() {
        setProgressIndicator();
        AsyncRunner.runAsync(
                categoryService::getAllCategories,
                this::updateTableData,
                categoryTable
        );
    }

    private void updateTableData(List<CategoryListDTO> categories) {
        categoryData.setAll(categories);
        removeProgressIndicator();
    }

    private void navigateToDetails(CategoryListDTO details) {
        viewManager.showDialog(AppView.CATEGORY_DETAILS,
                (CategoryDetailsController controller) ->
                        controller.setDetails(details));
    }

    private void navigateToForm() {
        viewManager.showDialog(AppView.CATEGORY_FORM);
    }

    private void navigateToReport() {
        viewManager.showDialog(AppView.CATEGORY_REPORT,
                (CategoryReportController controller) ->
                        controller.setData(categoryData));
    }
}