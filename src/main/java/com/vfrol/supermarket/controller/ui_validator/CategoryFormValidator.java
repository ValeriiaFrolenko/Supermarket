package com.vfrol.supermarket.controller.ui_validator;

import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;

public class CategoryFormValidator {

    private final Validator validator;

    public CategoryFormValidator(Validator validator) {
        this.validator = validator;
    }
    public void validateCategoryName(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Category Name is required");
        ValidationHelper.checkMaxLength(validator, field, 50, "Category Name max length is 50 characters");
        ValidationHelper.checkRegex(validator, field, "^[\\p{L}\\s'-]+$", "Category Name must contain only letters, spaces, or hyphens");
    }
}
