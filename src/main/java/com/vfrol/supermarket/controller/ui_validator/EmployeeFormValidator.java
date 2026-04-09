package com.vfrol.supermarket.controller.ui_validator;

import com.vfrol.supermarket.enums.EmployeeRole;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;

import java.util.function.Supplier;

public class EmployeeFormValidator {

    private final Validator validator;
    private final Supplier<Boolean> isNewRecordSupplier;

    public EmployeeFormValidator(Validator validator, Supplier<Boolean> isNewRecordSupplier) {
        this.validator = validator;
        this.isNewRecordSupplier = isNewRecordSupplier;
    }

    public void validateId(TextField field) {
        ValidationHelper.checkRequired(validator, field, "ID is required");
        ValidationHelper.checkMaxLength(validator, field, 10, "ID max length is 10 characters");
        ValidationHelper.checkRegex(validator, field, "^[a-zA-Z0-9]+$", "ID must contain only letters and digits");
    }

    public void validatePassword(PasswordField field) {
        ValidationHelper.checkRequiredConditional(validator, field, isNewRecordSupplier, "Password is required for new employees");
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
        ValidationHelper.checkRegex(validator, field, "^[\\p{L}\\s'-]+$", "Patronymic must contain only letters, spaces, or hyphens");
    }

    public void validateRole(ComboBox<EmployeeRole> field) {
        ValidationHelper.checkRequiredComboBox(validator, field, "Role is required");
    }

    public void validateSalary(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Salary is required");
        ValidationHelper.checkIsDouble(validator, field, "Salary must be a valid number");
        ValidationHelper.checkMinDouble(validator, field, 0.0, "Salary cannot be negative");
        ValidationHelper.checkMaxDouble(validator, field, 999999999.9999, "Salary exceeds database limit");
    }

    public void validateDates(DatePicker dobPicker, DatePicker dosPicker) {
        ValidationHelper.checkRequiredDate(validator, dobPicker, "Date of birth is required");
        ValidationHelper.checkRequiredDate(validator, dosPicker, "Date of start is required");
        ValidationHelper.checkDateDifferenceMinYears(validator, dobPicker, dosPicker, 18, "Employee must be at least 18 years old at start date");
    }

    public void validatePhone(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Phone is required");
        ValidationHelper.checkRegex(validator, field, "^\\+380\\d{9}$", "Phone must match format +380XXXXXXXXX");
    }

    public void validateCity(TextField field) {
        ValidationHelper.checkRequired(validator, field, "City is required");
        ValidationHelper.checkMaxLength(validator, field, 50, "City max length is 50 characters");
        ValidationHelper.checkRegex(validator, field, "^[\\p{L}\\p{N}\\s'.,/-]+$", "City contains invalid characters");
    }

    public void validateStreet(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Street is required");
        ValidationHelper.checkMaxLength(validator, field, 50, "Street max length is 50 characters");
        ValidationHelper.checkRegex(validator, field, "^[\\p{L}\\p{N}\\s'.,/-]+$", "Street contains invalid characters");
    }

    public void validateZipCode(TextField field) {
        ValidationHelper.checkRequired(validator, field, "Zip Code is required");
        ValidationHelper.checkMaxLength(validator, field, 9, "Zip Code max length is 9 characters");
        ValidationHelper.checkRegex(validator, field, "^\\d+$", "Zip Code must contain only digits");
    }
}