package com.vfrol.supermarket.controller.util;

import javafx.util.StringConverter;
import javafx.stage.Window;
import org.controlsfx.control.SearchableComboBox;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SearchableComboBoxHelper {

    private SearchableComboBoxHelper() {}

    public static <T> void configureForForm(
            SearchableComboBox<T> comboBox,
            Supplier<List<T>> allItemsSupplier,
            Function<T, String> toStringFunction,
            Function<T, ?> idExtractor) {

        setupConverter(comboBox, toStringFunction);
        setupOnWindowShowing(comboBox, allItemsSupplier, true, idExtractor);
    }

    public static <T> void configureForFilter(
            SearchableComboBox<T> comboBox,
            Supplier<List<T>> allItemsSupplier,
            Function<T, String> toStringFunction) {

        setupConverter(comboBox, toStringFunction);
        setupOnWindowShowing(comboBox, allItemsSupplier, false, null);
    }

    private static <T> void setupConverter(
            SearchableComboBox<T> comboBox,
            Function<T, String> toStringFunction) {

        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(T item) {
                return item == null ? "" : toStringFunction.apply(item);
            }

            @Override
            public T fromString(String text) {
                if (text == null || text.isBlank()) return null;
                return comboBox.getItems().stream()
                        .filter(item -> toStringFunction.apply(item).equals(text))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    private static <T> void setupOnWindowShowing(
            SearchableComboBox<T> comboBox,
            Supplier<List<T>> allItemsSupplier,
            boolean restoreSelection,
            Function<T, ?> idExtractor) {

        comboBox.sceneProperty().addListener((obsScene, oldScene, newScene) -> {
            if (newScene == null) return;

            newScene.windowProperty().addListener((obsWindow, oldWindow, newWindow) -> {
                if (newWindow == null) return;
                attachWindowListener(comboBox, allItemsSupplier, restoreSelection, idExtractor, newWindow);
            });

            if (newScene.getWindow() != null) {
                attachWindowListener(comboBox, allItemsSupplier, restoreSelection, idExtractor, newScene.getWindow());
            }

            reload(comboBox, allItemsSupplier, restoreSelection, idExtractor);
        });

        if (comboBox.getScene() != null) {
            reload(comboBox, allItemsSupplier, restoreSelection, idExtractor);
        }
    }

    private static <T> void attachWindowListener(
            SearchableComboBox<T> comboBox,
            Supplier<List<T>> allItemsSupplier,
            boolean restoreSelection,
            Function<T, ?> idExtractor,
            Window window) {

        window.showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                reload(comboBox, allItemsSupplier, restoreSelection, idExtractor);
            }
        });
    }

    private static <T> void reload(
            SearchableComboBox<T> comboBox,
            Supplier<List<T>> allItemsSupplier,
            boolean restoreSelection,
            Function<T, ?> idExtractor) {

        Object token = new Object();
        comboBox.setUserData(token);

        AsyncRunner.runAsync(allItemsSupplier, items -> {
            if (comboBox.getUserData() != token) return;

            T current = comboBox.getValue();
            Object currentId = (restoreSelection && current != null && idExtractor != null)
                    ? idExtractor.apply(current)
                    : null;

            items.addFirst(comboBox.getConverter().fromString(""));

            comboBox.getItems().setAll(items);

            if (currentId != null) {
                items.stream()
                        .filter(item -> item != null && idExtractor.apply(item).equals(currentId))
                        .findFirst()
                        .ifPresent(comboBox::setValue);
            }
        }, null);
    }
}