package com.vfrol.supermarket.controller.ui_validator;

import javafx.beans.binding.Bindings; // <-- Додано імпорт
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;

public class StoreProductFormValidator {

    private final Validator validator;
    private final ObservableBooleanValue isPromotionalObservable;

    /**
     * @param isPromotionalObservable tracked observable from the promotional CheckBox
     * ({@code promotionalCheckBox.selectedProperty()}).
     * Must be an ObservableBooleanValue so the validator
     * re-evaluates when the checkbox changes.
     */
    public StoreProductFormValidator(Validator validator,
                                     ObservableBooleanValue isPromotionalObservable) {
        this.validator = validator;
        this.isPromotionalObservable = isPromotionalObservable;
    }

    public void validateUPC(TextField field) {
        ValidationHelper.checkRequired(validator, field, "UPC is required");
        ValidationHelper.checkMaxLength(validator, field, 12, "UPC max length is 12 characters");
        ValidationHelper.checkRegex(validator, field, "^[a-zA-Z0-9]+$",
                "UPC must contain only letters and digits");
    }

    public void validateUPCProm(TextField field) {
        ValidationHelper.checkRequiredConditional(validator, field, isPromotionalObservable,
                "UPC Promo is required for promotional products");
        ValidationHelper.checkMaxLength(validator, field, 12, "UPC Promo max length is 12 characters");
        ValidationHelper.checkRegex(validator, field, "^[a-zA-Z0-9]+$",
                "UPC Promo must contain only letters and digits");
    }

    public void validateProduct(ComboBox<?> box) {
        ValidationHelper.checkRequiredComboBoxConditional(validator, box,
                Bindings.not(isPromotionalObservable),
                "Product is required for non-promotional store products");
    }

    public void validatePrice(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Price is required");
        ValidationHelper.checkIsDouble(validator, field, "Price must be a valid number");
        ValidationHelper.checkMinDouble(validator, field, 0.0, "Price cannot be negative");
        ValidationHelper.checkMaxDouble(validator, field, 999999999.9999, "Price exceeds database limit");
    }

    public void validateQuantity(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Quantity is required");
        ValidationHelper.checkIsInt(validator, field, "Quantity must be a valid whole number");
        ValidationHelper.checkMinInt(validator, field, 0, "Quantity cannot be negative");
    }
}