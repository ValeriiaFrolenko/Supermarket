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

        Debouncer debouncer = new Debouncer(300);
        comboBox.setEditable(true);

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
                        .findFirst().orElse(null);
            }
        });

        comboBox.getEditor().textProperty().addListener((_, _, newVal) -> {
            if (matchesCurrentValue(comboBox, newVal, toStringFunction)) return;

            if (newVal == null || newVal.isBlank()) {
                debouncer.debounce(() -> AsyncRunner.runAsync(allItemsSupplier,
                        items -> updateItemsAndRestoreValue(comboBox, items), null));
                return;
            }

            debouncer.debounce(() -> AsyncRunner.runAsync(() -> searchFunction.apply(newVal),
                    results -> {
                        updateItemsAndRestoreValue(comboBox, results);
                        if (!results.isEmpty()) comboBox.show();
                    }, null));
        });

        AsyncRunner.runAsync(allItemsSupplier, items -> updateItemsAndRestoreValue(comboBox, items), null);
    }

    private static <T> void updateItemsAndRestoreValue(ComboBox<T> comboBox, List<T> newItems) {
        T current = comboBox.getValue();
        comboBox.getItems().setAll(newItems);
        if (current != null) {
            comboBox.setValue(current);
        }
    }

    private static <T> boolean matchesCurrentValue(ComboBox<T> comboBox, String text, Function<T, String> toStringFunction) {
        T current = comboBox.getValue();
        return current != null && toStringFunction.apply(current).equals(text);
    }
}