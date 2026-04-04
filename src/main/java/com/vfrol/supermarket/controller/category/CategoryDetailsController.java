package com.vfrol.supermarket.controller.category;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.config.ViewManager;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.service.CategoryService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CategoryDetailsController {
    @FXML
    private VBox detailsPanel;

    @FXML private Label nameLabel;

    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private CategoryListDTO currentCategory;
    private final CategoryService categoryService;
    private final ViewManager viewManager;
    private final SessionManager sessionManager;

    @Inject
    public CategoryDetailsController(CategoryService categoryService, ViewManager viewManager, SessionManager sessionManager) {
        this.categoryService = categoryService;
        this.viewManager = viewManager;
        this.sessionManager = sessionManager;
    }

    @FXML
    public void initialize() {
        configureForRole();
    }

    private void configureForRole() {
        if (!sessionManager.isManager()) {
            editButton.setVisible(false);
            editButton.setManaged(false);

            deleteButton.setVisible(false);
            deleteButton.setManaged(false);
        } else {
            editButton.setVisible(true);
            editButton.setManaged(true);

            deleteButton.setVisible(true);
            deleteButton.setManaged(true);
        }
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
        Stage window = (Stage) detailsPanel.getScene().getWindow();
        if (window != null) {
            window.close();
        }
    }
}
