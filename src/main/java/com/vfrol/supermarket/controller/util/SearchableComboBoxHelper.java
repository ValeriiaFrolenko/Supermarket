package com.vfrol.supermarket.controller.util;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class SearchableComboBoxHelper {

    private SearchableComboBoxHelper() {}

    public static <T> void configure(
            ComboBox<T> comboBox,
            Supplier<List<T>> allItemsSupplier,
            Function<String, List<T>> searchFunction,
            Function<T, String> toStringFunction) {

        comboBox.setEditable(true);

        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(T item) {
                return item == null ? "" : toStringFunction.apply(item);
            }

            @Override
            public T fromString(String text) {
                if (text == null || text.isBlank()) {
                    return null;
                }
                return comboBox.getItems().stream()
                        .filter(item -> toStringFunction.apply(item).equals(text))
                        .findFirst()
                        .orElse(null);
            }
        });

        comboBox.getEditor().textProperty().addListener((_, _, newVal) -> {
            if (matchesCurrentValue(comboBox, newVal, toStringFunction)) {
                return;
            }

            if (newVal == null || newVal.isBlank()) {
                resetToFullList(comboBox, allItemsSupplier);
                return;
            }

            List<T> results = searchFunction.apply(newVal);
            comboBox.getItems().setAll(results);

            if (!results.isEmpty()) {
                comboBox.show();
            }
        });

        comboBox.getItems().setAll(allItemsSupplier.get());
    }

    private static <T> boolean matchesCurrentValue(ComboBox<T> comboBox,
                                                   String text,
                                                   Function<T, String> toStringFunction) {
        T current = comboBox.getValue();
        return current != null && toStringFunction.apply(current).equals(text);
    }

    private static <T> void resetToFullList(ComboBox<T> comboBox, Supplier<List<T>> allItemsSupplier) {
        comboBox.setValue(null);
        comboBox.getItems().setAll(allItemsSupplier.get());
    }
}