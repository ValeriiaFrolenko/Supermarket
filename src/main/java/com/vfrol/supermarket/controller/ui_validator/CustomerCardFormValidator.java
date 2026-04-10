package com.vfrol.supermarket.controller.ui_validator;

import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;

public class CustomerCardFormValidator {

    private final Validator validator;

    public CustomerCardFormValidator(Validator validator) {
        this.validator = validator;
    }

    public void validateCardNumber(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Card Number is required");
        ValidationHelper.checkMaxLength(validator, field, 13, "Card Number max length is 13 characters");
        ValidationHelper.checkRegex(validator, field, "^[a-zA-Z0-9]+$", "Card Number must contain only letters and digits");
    }

    public void validateSurname(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Surname is required");
        ValidationHelper.checkMaxLength(validator, field, 50, "Surname max length is 50 characters");
        ValidationHelper.checkRegex(validator, field, "^[\\p{L}\\s'-]+$", "Surname must contain only letters, spaces, or hyphens");
    }

    public void validateName(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Name is required");
        ValidationHelper.checkMaxLength(validator, field, 50, "Name max length is 50 characters");
        ValidationHelper.checkRegex(validator, field, "^[\\p{L}\\s'-]+$", "Name must contain only letters, spaces, or hyphens");
    }

    public void validatePatronymic(TextField field) {
        ValidationHelper.checkMaxLength(validator, field, 50, "Patronymic max length is 50 characters");
        ValidationHelper.checkRegex(validator, field, "^[\\p{L}\\s'-]+$", "Patronymic contains invalid characters");
    }

    public void validatePhone(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Phone is required");
        ValidationHelper.checkRegex(validator, field, "^\\+380\\d{9}$", "Phone must match format +380XXXXXXXXX");
    }

    public void validateCity(TextField field) {
        ValidationHelper.checkMaxLength(validator, field, 50, "City max length is 50 characters");
        ValidationHelper.checkRegex(validator, field, "^[\\p{L}\\p{N}\\s'.,/\\-]+$", "City contains invalid characters");
    }

    public void validateStreet(TextField field) {
        ValidationHelper.checkMaxLength(validator, field, 50, "Street max length is 50 characters");
        ValidationHelper.checkRegex(validator, field, "^[\\p{L}\\p{N}\\s'.,/\\-]+$", "Street contains invalid characters");
    }

    public void validateZipCode(TextField field) {
        ValidationHelper.checkMaxLength(validator, field, 9, "Zip Code max length is 9 characters");
        ValidationHelper.checkRegex(validator, field, "^\\d+$", "Zip Code must contain only digits");
    }

    public void validateDiscount(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Discount is required");
        ValidationHelper.checkIsInt(validator, field, "Discount must be a valid whole number");
        ValidationHelper.checkMinInt(validator, field, 0, "Discount must be at least 0");
        ValidationHelper.checkMaxInt(validator, field, 100, "Discount cannot be greater than 100");
    }
}