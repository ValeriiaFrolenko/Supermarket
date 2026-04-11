package com.vfrol.supermarket.controller.ui_validator;

import com.vfrol.supermarket.controller.util.InputHelper;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

public final class ValidationHelper {

    private ValidationHelper() {}

    public static void checkRequired(Validator validator, TextField field, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    if (InputHelper.getString(field) == null) c.error(message);
                })
                .decorates(field).immediate();
    }

    // --- Conditional required (Supplier version — for static conditions like isNewRecord) ---

    public static void checkRequiredConditional(Validator validator, TextField field,
                                                Supplier<Boolean> condition, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    if (condition.get() && InputHelper.getString(field) == null) c.error(message);
                })
                .decorates(field).immediate();
    }

    // Observable version — tracks condition changes (e.g. a CheckBox toggle during the form)
    public static void checkRequiredConditional(Validator validator, TextField field,
                                                ObservableBooleanValue conditionObservable, String message) {
        validator.createCheck()
                .dependsOn("text",      field.textProperty())
                .dependsOn("condition", conditionObservable)
                .withMethod(c -> {
                    if (Boolean.TRUE.equals(c.get("condition")) && InputHelper.getString(field) == null)
                        c.error(message);
                })
                .decorates(field).immediate();
    }

    public static void checkMaxLength(Validator validator, TextField field, int maxLength, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    String text = InputHelper.getString(field);
                    if (text != null && text.length() > maxLength) c.error(message);
                })
                .decorates(field).immediate();
    }

    public static void checkRegex(Validator validator, TextField field, String regex, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    String text = InputHelper.getString(field);
                    if (text != null && !text.matches(regex)) c.error(message);
                })
                .decorates(field).immediate();
    }

    public static void checkIsDouble(Validator validator, TextField field, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    String text = InputHelper.getString(field);
                    if (text != null && InputHelper.getDouble(field) == null) c.error(message);
                })
                .decorates(field).immediate();
    }

    public static void checkMinDouble(Validator validator, TextField field, double min, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    Double val = InputHelper.getDouble(field);
                    if (val != null && val < min) c.error(message);
                })
                .decorates(field).immediate();
    }

    public static void checkMaxDouble(Validator validator, TextField field, double max, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    Double val = InputHelper.getDouble(field);
                    if (val != null && val > max) c.error(message);
                })
                .decorates(field).immediate();
    }

    public static void checkIsInt(Validator validator, TextField field, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    String text = InputHelper.getString(field);
                    if (text != null && InputHelper.getInt(field) == null) c.error(message);
                })
                .decorates(field).immediate();
    }

    public static void checkMinInt(Validator validator, TextField field, int min, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    Integer val = InputHelper.getInt(field);
                    if (val != null && val < min) c.error(message);
                })
                .decorates(field).immediate();
    }

    public static void checkMaxInt(Validator validator, TextField field, int max, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    Integer val = InputHelper.getInt(field);
                    if (val != null && val > max) c.error(message);
                })
                .decorates(field).immediate();
    }

    public static void checkRequiredDate(Validator validator, DatePicker picker, String message) {
        validator.createCheck()
                .dependsOn("date", picker.valueProperty())
                .withMethod(c -> {
                    if (c.get("date") == null) c.error(message);
                })
                .decorates(picker).immediate();
    }

    public static void checkDateDifferenceFromNowMinYears(Validator validator, DatePicker datePicker,
                                                          int minYears, String message) {
        validator.createCheck()
                .dependsOn("date", datePicker.valueProperty())
                .withMethod(c -> {
                    LocalDate date = c.get("date");
                    if (date != null && ChronoUnit.YEARS.between(date, LocalDate.now()) < minYears)
                        c.error(message);
                })
                .decorates(datePicker).immediate();
    }

    public static void checkDateDifferenceMinYears(Validator validator, DatePicker startDatePicker,
                                                   DatePicker endDatePicker, int minYears, String message) {
        validator.createCheck()
                .dependsOn("start", startDatePicker.valueProperty())
                .dependsOn("end",   endDatePicker.valueProperty())
                .withMethod(c -> {
                    LocalDate start = c.get("start");
                    LocalDate end   = c.get("end");
                    if (start != null && end != null && ChronoUnit.YEARS.between(start, end) < minYears)
                        c.error(message);
                })
                .decorates(endDatePicker).immediate();
    }


    public static void checkRequiredComboBox(Validator validator, ComboBox<?> box, String message) {
        validator.createCheck()
                .dependsOn("val", box.valueProperty())
                .withMethod(c -> {
                    if (c.get("val") == null) c.error(message);
                })
                .decorates(box).immediate();
    }

    public static void checkRequiredComboBoxConditional(Validator validator, ComboBox<?> box,
                                                        Supplier<Boolean> condition, String message) {
        validator.createCheck()
                .dependsOn("val", box.valueProperty())
                .withMethod(c -> {
                    if (condition.get() && c.get("val") == null) c.error(message);
                })
                .decorates(box).immediate();
    }

    public static void checkRequiredComboBoxConditional(Validator validator, ComboBox<?> box,
                                                        ObservableBooleanValue conditionObservable, String message) {
        validator.createCheck()
                .dependsOn("val",       box.valueProperty())
                .dependsOn("condition", conditionObservable)
                .withMethod(c -> {
                    if (Boolean.TRUE.equals(c.get("condition")) && c.get("val") == null)
                        c.error(message);
                })
                .decorates(box).immediate();
    }

    public static void checkRequiredTextArea(Validator validator, TextArea field, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    String text = field.getText();
                    if (text == null || text.trim().isEmpty()) c.error(message);
                })
                .decorates(field).immediate();
    }

    public static void checkMaxLengthTextArea(Validator validator, TextArea field, int maxLength,
                                              String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    String text = field.getText();
                    if (text != null && text.trim().length() > maxLength) c.error(message);
                })
                .decorates(field).immediate();
    }
}