package com.vfrol.supermarket.controller.ui_validator;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;

public class SaleFormValidator {

    private final Validator validator;

    public SaleFormValidator(Validator validator) {
        this.validator = validator;
    }

    public void validateProduct(ComboBox<?> box) {
        ValidationHelper.checkRequiredComboBox(validator, box, "Product is required");
    }

    public void validatePrice(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Unit Price is required");
        ValidationHelper.checkIsDouble(validator, field, "Price must be a valid number");
        ValidationHelper.checkMinDouble(validator, field, 0.0, "Price cannot be negative");
    }

    public void validateQuantity(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Quantity is required");
        ValidationHelper.checkIsInt(validator, field, "Quantity must be a valid whole number");
        ValidationHelper.checkMinInt(validator, field, 1, "Quantity must be at least 1");
    }
}