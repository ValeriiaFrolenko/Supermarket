package com.vfrol.supermarket.controller.ui_validator;

import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;

public class CheckFormValidator {

    private final Validator validator;

    public CheckFormValidator(Validator validator) {
        this.validator = validator;
    }

    public void validateCheckNumber(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Check Number is required");
        ValidationHelper.checkMaxLength(validator, field, 10, "Check Number max length is 10 characters");
        ValidationHelper.checkRegex(validator, field, "^[a-zA-Z0-9]+$", "Check Number must contain only letters and digits");
    }
}