package com.vfrol.supermarket.controller.category;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseFormController;
import com.vfrol.supermarket.controller.ui_validator.CategoryFormValidator;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.dto.category.CategoryCreateDTO;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.service.CategoryService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CategoryFormController extends BaseFormController<CategoryCreateDTO, CategoryListDTO> {

    private final CategoryService categoryService;
    private CategoryListDTO currentCategory;

    @FXML private TextField nameField;

    @Inject
    public CategoryFormController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    protected String getEntityName() {
        return "Category";
    }

    @Override
    protected void setupValidation() {
        CategoryFormValidator categoryValidator = new CategoryFormValidator(validator);
        categoryValidator.validateCategoryName(nameField);
    }

    @Override
    protected void populateFields(CategoryListDTO dto) {
        this.currentCategory = dto;
        nameField.setText(dto.name());
    }

    @Override
    protected CategoryCreateDTO buildDTO() {
        return new CategoryCreateDTO(
                currentCategory == null ? 0 : currentCategory.id(),
                InputHelper.getString(nameField)
        );
    }

    @Override
    protected void saveEntity(CategoryCreateDTO dto) {
        if (isEditMode) {
            categoryService.updateCategory(dto);
        } else {
            categoryService.addCategory(dto);
        }
    }
}