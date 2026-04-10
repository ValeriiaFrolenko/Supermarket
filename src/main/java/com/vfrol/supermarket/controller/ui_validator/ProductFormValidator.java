package com.vfrol.supermarket.controller.ui_validator;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;

public class ProductFormValidator {

    private final Validator validator;

    public ProductFormValidator(Validator validator) {
        this.validator = validator;
    }

    public void validateProductName(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Product Name is required");
        ValidationHelper.checkMaxLength(validator, field, 50, "Product Name max length is 50 characters");
        ValidationHelper.checkRegex(validator, field, "^[\\p{L}\\p{N}\\s'\\-]+$", "Product Name contains invalid characters");
    }

    public void validateCategory(ComboBox<?> box) {
        ValidationHelper.checkRequiredComboBox(validator, box, "Category is required");
    }

    public void validateCharacteristics(TextArea field) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    String text = field.getText();
                    if (text == null || text.trim().isEmpty()) {
                        c.error("Characteristics are required");
                    } else if (text.length() > 100) {
                        c.error("Characteristics max length is 100 characters");
                    }
                })
                .decorates(field)
                .immediate();
    }
}