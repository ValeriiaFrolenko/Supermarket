package com.vfrol.supermarket.controller.category;

import com.google.inject.Inject;
import com.vfrol.supermarket.dto.category.CategoryCreateDTO;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.service.CategoryService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CategoryFormController {

    @FXML private VBox formPanel;
    @FXML private Label label;
    @FXML private TextField nameField;

    private final CategoryService categoryService;

    private boolean isEditMode = false;

    private CategoryListDTO categoryListDTO;

    @Inject
    public CategoryFormController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @FXML
    public void initialize() {
        label.setText("Add Employee");
    }

    public void setCategory(CategoryListDTO dto) {
        this.isEditMode = true;
        label.setText("Edit Employee");
        categoryListDTO = dto;
        nameField.setText(dto.name());
    }

    @FXML
    public void onSave() {
        try {
            CategoryCreateDTO dto = new CategoryCreateDTO(
                    categoryListDTO == null ? 0 : categoryListDTO.id(),
                    nameField.getText().trim()
            );

            if (isEditMode) {
                categoryService.updateCategory(dto);
            } else {
                categoryService.addCategory(dto);
            }
            closeWindow();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while saving: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage window = (Stage) formPanel.getScene().getWindow();
        if (window != null) {
            window.close();
        }
    }
}