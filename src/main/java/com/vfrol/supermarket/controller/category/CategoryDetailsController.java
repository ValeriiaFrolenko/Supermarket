package com.vfrol.supermarket.controller.category;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.BaseModalController;
import com.vfrol.supermarket.controller.SecurityUIHelper;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.service.CategoryService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CategoryDetailsController extends BaseModalController {

    @FXML private VBox detailsPanel;
    @FXML private Label nameLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private CategoryListDTO currentCategory;
    private final CategoryService categoryService;

    @Inject
    public CategoryDetailsController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @FXML
    public void initialize() {
        SecurityUIHelper.configureManagerOnlyNodes(sessionManager, editButton, deleteButton);
    }

    public void setCategoryDetails(CategoryListDTO dto) {
        this.currentCategory = dto;
        nameLabel.setText(dto.name());
    }

    @FXML
    public void onEdit() {
        viewManager.showDialog(AppView.CATEGORY_FORM, (CategoryFormController controller) ->
                controller.setCategory(currentCategory));
        hide();
    }

    @FXML
    public void onDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this category?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                categoryService.deleteCategory(currentCategory.id());
                hide();
            }
        });
    }

    @FXML
    public void hide() {
        closeWindow(detailsPanel);
    }
}