package com.vfrol.supermarket.controller.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class AlertHelper {
    private AlertHelper() {}

    public static void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

    public static void showWarning(String message) {
        new Alert(Alert.AlertType.WARNING, message).showAndWait();
    }

    public static boolean confirmDelete(String entityName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this " + entityName + "?",
                ButtonType.YES, ButtonType.NO);
        return alert.showAndWait().filter(response -> response == ButtonType.YES).isPresent();
    }
}