package com.vfrol.supermarket.controller.util;

import javafx.scene.control.TextField;

public final class InputHelper {
    private InputHelper() {}

    public static String getString(TextField field) {
        if (field == null || field.getText() == null || field.getText().isBlank()) {
            return null;
        }
        return field.getText().trim();
    }

    public static Integer getInt(TextField field) {
        String text = getString(field);
        if (text == null) {
            return null;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double getDouble(TextField field) {
        String text = getString(field);
        if (text == null) {
            return null;
        }
        try {
            return Double.parseDouble(text.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}