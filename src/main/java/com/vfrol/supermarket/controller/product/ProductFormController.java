package com.vfrol.supermarket.controller.product;

import com.google.inject.Inject;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.dto.product.ProductCreateDTO;
import com.vfrol.supermarket.dto.product.ProductDetailsDTO;
import com.vfrol.supermarket.service.CategoryService;
import com.vfrol.supermarket.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.List;

public class ProductFormController {

    @FXML private VBox formPanel;
    @FXML private Label titleLabel;
    @FXML private TextField nameField;
    @FXML private ComboBox<CategoryListDTO> categoryComboBox;
    @FXML private TextArea characteristicsArea;

    private final ProductService productService;
    private final CategoryService categoryService;
    private boolean isEditMode = false;
    private ProductDetailsDTO currentProduct;

    @Inject
    public ProductFormController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @FXML
    public void initialize() {
        titleLabel.setText("Add Product");
        categoryComboBox.getItems().setAll(categoryService.getAllCategories());
    }

    public void setProduct(ProductDetailsDTO dto) {
        this.isEditMode = true;
        this.currentProduct = dto;
        titleLabel.setText("Edit Product");
        nameField.setText(dto.name());
        characteristicsArea.setText(dto.characteristics());

        List<CategoryListDTO> matchingCategories = categoryService.getCategoriesByName(dto.categoryName());
        if (!matchingCategories.isEmpty()) {
            categoryComboBox.setValue(matchingCategories.getFirst());
        }
    }

    @FXML
    public void onSave() {
        if (categoryComboBox.getValue() == null) return;

        ProductCreateDTO dto = new ProductCreateDTO(
                isEditMode ? currentProduct.id() : 0,
                categoryComboBox.getValue().id(),
                nameField.getText(),
                characteristicsArea.getText()
        );

        if (isEditMode) {
            productService.updateProduct(dto);
        } else {
            productService.addProduct(dto);
        }
        closeWindow();
    }

    @FXML
    public void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage window = (Stage) formPanel.getScene().getWindow();
        if (window != null) window.close();
    }
}