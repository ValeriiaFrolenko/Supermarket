package com.vfrol.supermarket.controller.store_product;

import com.vfrol.supermarket.dto.product.ProductNameDTO;
import com.vfrol.supermarket.service.ProductService;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public final class SearchableProductComboBoxHelper {

    private SearchableProductComboBoxHelper() {}

    public static void configure(ComboBox<ProductNameDTO> comboBox, ProductService productService) {
        comboBox.setEditable(true);
        comboBox.getItems().setAll(productService.getAllProductNames());

        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ProductNameDTO object) {
                return object == null ? "" : object.name();
            }

            @Override
            public ProductNameDTO fromString(String string) {
                if (string == null || string.isBlank()) {
                    return null;
                }
                return comboBox.getItems().stream()
                        .filter(p -> p.name().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        comboBox.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            if (comboBox.getValue() != null &&
                    comboBox.getValue().name().equals(newVal)) {
                return;
            }

            if (newVal == null || newVal.isBlank()) {
                comboBox.setValue(null);
                comboBox.getItems().setAll(productService.getAllProductNames());
                return;
            }

            var results = productService.getProductByName(newVal);
            comboBox.getItems().setAll(results);
            if (!results.isEmpty()) {
                comboBox.show();
            }
        });
    }
}