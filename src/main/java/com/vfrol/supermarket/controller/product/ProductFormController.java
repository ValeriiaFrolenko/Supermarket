package com.vfrol.supermarket.controller.product;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseFormController;
import com.vfrol.supermarket.controller.ui_validator.ProductFormValidator;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.controller.util.SearchableComboBoxHelper;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.dto.product.ProductCreateDTO;
import com.vfrol.supermarket.dto.product.ProductDetailsDTO;
import com.vfrol.supermarket.service.CategoryService;
import com.vfrol.supermarket.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProductFormController extends BaseFormController<ProductCreateDTO, ProductDetailsDTO> {

    private final ProductService productService;
    private final CategoryService categoryService;
    private ProductDetailsDTO currentProduct;

    @FXML private TextField nameField;
    @FXML private ComboBox<CategoryListDTO> categoryComboBox;
    @FXML private TextArea characteristicsArea;

    @Inject
    public ProductFormController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @FXML
    @Override
    public void initialize() {
        super.initialize();

        SearchableComboBoxHelper.configure(
                categoryComboBox,
                categoryService::getAllCategories,
                categoryService::getCategoriesByName,
                CategoryListDTO::name
        );
    }

    @Override
    protected String getEntityName() {
        return "Product";
    }

    @Override
    protected void setupValidation() {
        ProductFormValidator productValidator = new ProductFormValidator(validator);

        productValidator.validateProductName(nameField);
        productValidator.validateCategory(categoryComboBox);
        productValidator.validateCharacteristics(characteristicsArea);
    }

    @Override
    protected void populateFields(ProductDetailsDTO dto) {
        this.currentProduct = dto;
        nameField.setText(dto.name());
        characteristicsArea.setText(dto.characteristics());

        CategoryListDTO currentCategory = new CategoryListDTO(dto.categoryId(), dto.categoryName());
        categoryComboBox.getItems().setAll(currentCategory);
        categoryComboBox.setValue(currentCategory);
    }

    @Override
    protected ProductCreateDTO buildDTO() {
        return new ProductCreateDTO(
                currentProduct == null ? 0 : currentProduct.id(),
                categoryComboBox.getValue().id(),
                InputHelper.getString(nameField),
                characteristicsArea.getText().trim()
        );
    }

    @Override
    protected void saveEntity(ProductCreateDTO dto) {
        if (isEditMode) {
            productService.updateProduct(dto);
        } else {
            productService.addProduct(dto);
        }
    }
}