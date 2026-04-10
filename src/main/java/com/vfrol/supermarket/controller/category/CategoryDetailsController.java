package com.vfrol.supermarket.controller.category;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseDetailsController;
import com.vfrol.supermarket.controller.util.SessionUIHelper;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.service.CategoryService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CategoryDetailsController extends BaseDetailsController<CategoryListDTO> {

    private final CategoryService categoryService;

    @FXML private Label nameLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @Inject
    public CategoryDetailsController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @FXML
    public void initialize() {
        SessionUIHelper.configureManagerOnlyNodes(sessionManager, editButton, deleteButton);
    }

    @Override
    protected String getEntityName() {
        return "Category";
    }

    @Override
    protected void populateFields(CategoryListDTO dto) {
        nameLabel.setText(dto.name());
    }

    @Override
    protected void deleteEntity() {
        categoryService.deleteCategory(currentItem.id());
    }

    @Override
    protected void navigateToForm() {
        viewManager.showDialog(AppView.CATEGORY_FORM, (CategoryFormController controller) ->
                controller.setupForEdit(currentItem));
    }
}