package com.vfrol.supermarket.controller.ui_validator;

import com.vfrol.supermarket.controller.util.InputHelper;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
                    if (InputHelper.getString(field) == null) {
                        c.error(message);
                    }
                })
                .decorates(field)
                .immediate();
    }

    public static void checkRequiredConditional(Validator validator, TextField field, Supplier<Boolean> condition, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    if (condition.get() && InputHelper.getString(field) == null) {
                        c.error(message);
                    }
                })
                .decorates(field)
                .immediate();
    }

    public static void checkMaxLength(Validator validator, TextField field, int maxLength, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    String text = InputHelper.getString(field);
                    if (text != null && text.length() > maxLength) {
                        c.error(message);
                    }
                })
                .decorates(field)
                .immediate();
    }

    public static void checkRegex(Validator validator, TextField field, String regex, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    String text = InputHelper.getString(field);
                    if (text != null && !text.matches(regex)) {
                        c.error(message);
                    }
                })
                .decorates(field)
                .immediate();
    }

    public static void checkIsDouble(Validator validator, TextField field, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    String text = InputHelper.getString(field);
                    if (text != null && InputHelper.getDouble(field) == null) {
                        c.error(message);
                    }
                })
                .decorates(field)
                .immediate();
    }

    public static void checkMinDouble(Validator validator, TextField field, double min, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    Double val = InputHelper.getDouble(field);
                    if (val != null && val < min) {
                        c.error(message);
                    }
                })
                .decorates(field)
                .immediate();
    }

    public static void checkMaxDouble(Validator validator, TextField field, double max, String message) {
        validator.createCheck()
                .dependsOn("text", field.textProperty())
                .withMethod(c -> {
                    Double val = InputHelper.getDouble(field);
                    if (val != null && val > max) {
                        c.error(message);
                    }
                })
                .decorates(field)
                .immediate();
    }

    public static void checkRequiredDate(Validator validator, DatePicker picker, String message) {
        validator.createCheck()
                .dependsOn("date", picker.valueProperty())
                .withMethod(c -> {
                    if (c.get("date") == null) {
                        c.error(message);
                    }
                })
                .decorates(picker)
                .immediate();
    }

    public static void checkDateDifferenceMinYears(Validator validator, DatePicker startDatePicker, DatePicker endDatePicker, int minYears, String message) {
        validator.createCheck()
                .dependsOn("start", startDatePicker.valueProperty())
                .dependsOn("end", endDatePicker.valueProperty())
                .withMethod(c -> {
                    LocalDate start = c.get("start");
                    LocalDate end = c.get("end");
                    if (start != null && end != null && ChronoUnit.YEARS.between(start, end) < minYears) {
                        c.error(message);
                    }
                })
                .decorates(endDatePicker)
                .immediate();
    }

    public static void checkRequiredComboBox(Validator validator, ComboBox<?> box, String message) {
        validator.createCheck()
                .dependsOn("val", box.valueProperty())
                .withMethod(c -> {
                    if (c.get("val") == null) {
                        c.error(message);
                    }
                })
                .decorates(box)
                .immediate();
    }
}