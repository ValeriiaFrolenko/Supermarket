package com.vfrol.supermarket.controller.util;

import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class AsyncRunner {

    private AsyncRunner() {}

    public static <T> void runAsync(Supplier<T> backgroundTask, Consumer<T> onSuccess, Node disableNode) {
        if (disableNode != null) {
            disableNode.setDisable(true);
            if (disableNode.getScene() != null) {
                disableNode.getScene().setCursor(Cursor.WAIT);
            }
        }

        Task<T> task = new Task<>() {
            @Override
            protected T call() {
                return backgroundTask.get();
            }
        };

        task.setOnSucceeded(event -> {
            if (disableNode != null) {
                disableNode.setDisable(false);
                if (disableNode.getScene() != null) {
                    disableNode.getScene().setCursor(Cursor.DEFAULT);
                }
            }
            if (onSuccess != null) {
                onSuccess.accept(task.getValue());
            }
        });

        task.setOnFailed(event -> {
            if (disableNode != null) {
                disableNode.setDisable(false);
                if (disableNode.getScene() != null) {
                    disableNode.getScene().setCursor(Cursor.DEFAULT);
                }
            }
            AlertHelper.showError(task.getException().getMessage());
        });

        new Thread(task).start();
    }

    public static void runAsync(Runnable backgroundTask, Runnable onSuccess, Node disableNode) {
        runAsync(() -> {
            backgroundTask.run();
            return null;
        }, _ -> {
            if (onSuccess != null) {
                onSuccess.run();
            }
        }, disableNode);
    }
}