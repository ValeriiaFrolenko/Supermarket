package com.vfrol.supermarket.controller.check;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.BaseModalController;
import com.vfrol.supermarket.controller.util.SearchableComboBoxHelper;
import com.vfrol.supermarket.dto.store_product.StoreProductListDTO;
import com.vfrol.supermarket.filter.StoreProductFilter;
import com.vfrol.supermarket.service.StoreProductService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.util.function.Consumer;

public class SaleFormController extends BaseModalController {

    @FXML private VBox formPanel;
    @FXML private ComboBox<StoreProductListDTO> storeProductComboBox;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;

    private final StoreProductService storeProductService;
    @Setter
    private Consumer<CheckFormController.SaleItemModel> saveCallback;

    @Inject
    public SaleFormController(StoreProductService storeProductService) {
        this.storeProductService = storeProductService;
    }

    @FXML
    public void initialize() {
        configureComboBox();

        storeProductComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                priceField.setText(String.format("%.2f", newVal.price()).replace(",", "."));
            } else {
                priceField.setText("0.00");
            }
        });
    }

    private void configureComboBox() {
        SearchableComboBoxHelper.configure(
                storeProductComboBox,
                storeProductService::getAllStoreProducts,
                text -> storeProductService.getStoreProductsByFilter(StoreProductFilter.builder().productName(text).build()),
                sp -> sp.productName() + " (" + sp.UPC() + ")"
        );
    }

    @FXML
    public void onAdd() {
        StoreProductListDTO selectedProduct = storeProductComboBox.getValue();
        if (selectedProduct == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a product!").showAndWait();
            return;
        }

        try {
            int qty = Integer.parseInt(quantityField.getText().trim());
            double finalPrice = Double.parseDouble(priceField.getText().trim().replace(",", "."));

            if (qty <= 0 || finalPrice < 0) {
                throw new NumberFormatException();
            }

            if (qty > selectedProduct.quantity()) {
                new Alert(Alert.AlertType.WARNING, "Not enough items in store! Available: " + selectedProduct.quantity()).showAndWait();
                return;
            }

            if (saveCallback != null) {
                CheckFormController.SaleItemModel saleItem = new CheckFormController.SaleItemModel(
                        selectedProduct.UPC(),
                        selectedProduct.productName(),
                        finalPrice,
                        qty
                );
                saveCallback.accept(saleItem);
            }
            closeWindow(formPanel);

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid quantity or price format! Please check your inputs.").showAndWait();
        }
    }

    @FXML
    public void onCancel() {
        closeWindow(formPanel);
    }
}