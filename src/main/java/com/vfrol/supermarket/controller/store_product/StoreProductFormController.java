package com.vfrol.supermarket.controller.store_product;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseModalController;
import com.vfrol.supermarket.controller.util.SearchableComboBoxHelper;
import com.vfrol.supermarket.dto.product.ProductNameDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductCreateDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import com.vfrol.supermarket.service.ProductService;
import com.vfrol.supermarket.service.StoreProductService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class StoreProductFormController extends BaseModalController {

    @FXML private VBox formPanel;
    @FXML private Label titleLabel;
    @FXML private TextField upcField;
    @FXML private TextField upcPromField;
    @FXML private ComboBox<ProductNameDTO> productComboBox;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private CheckBox promotionalCheckBox;

    private final StoreProductService storeProductService;
    private final ProductService productService;

    private boolean isEditMode = false;

    @Inject
    public StoreProductFormController(StoreProductService storeProductService,
                                      ProductService productService) {
        this.storeProductService = storeProductService;
        this.productService = productService;
    }

    @FXML
    public void initialize() {
        titleLabel.setText("Add Store Product");
        configureComboBox();
        promotionalCheckBox.setSelected(false);
        promotionalCheckBox.selectedProperty().addListener((_, _, isPromotional) ->
                updateFieldVisibility(isPromotional));
        updateFieldVisibility(false);
    }

    private void configureComboBox() {
        SearchableComboBoxHelper.configure(
                productComboBox,
                productService::getAllProductNames,
                productService::getProductByName,
                ProductNameDTO::name
        );
    }

    private void updateFieldVisibility(boolean isPromotional) {
        upcPromField.setVisible(isPromotional);
        upcPromField.setManaged(isPromotional);

        productComboBox.setVisible(!isPromotional);
        productComboBox.setManaged(!isPromotional);
    }

    public void setStoreProduct(StoreProductDetailsDTO dto) {
        this.isEditMode = true;
        titleLabel.setText("Edit Store Product");

        upcField.setText(dto.UPC());
        upcField.setDisable(true);
        priceField.setText(String.valueOf(dto.price()));
        quantityField.setText(String.valueOf(dto.quantity()));

        boolean isPromotional = Boolean.TRUE.equals(dto.promotional());
        promotionalCheckBox.setSelected(isPromotional);

        updateFieldVisibility(isPromotional);

        if (isPromotional) {
            upcPromField.setText(dto.UPCprom() != null ? dto.UPCprom() : "");
        } else {
            ProductNameDTO current = new ProductNameDTO(dto.productId(), dto.productName());
            productComboBox.getItems().setAll(current);
            productComboBox.setValue(current);
        }
    }

    @FXML
    public void onSave() {
        try {
            boolean isPromotional = promotionalCheckBox.isSelected();
            String upc = upcField.getText().trim();
            String upcProm = upcPromField.getText().trim();

            if (upc.isBlank()) {
                new Alert(Alert.AlertType.WARNING, "UPC is required").showAndWait();
                return;
            }

            if (isPromotional && upcProm.isBlank()) {
                new Alert(Alert.AlertType.WARNING, "UPC Promo is required for promotional product").showAndWait();
                return;
            }

            Integer productId;
            if (isPromotional) {
                productId = storeProductService.getProductIdByUpc(upcProm);
                if (productId == null) {
                    new Alert(Alert.AlertType.ERROR, "Base product with UPC " + upcProm + " not found").showAndWait();
                    return;
                }
            } else {
                if (productComboBox.getValue() == null) {
                    new Alert(Alert.AlertType.WARNING, "Please select a product").showAndWait();
                    return;
                }
                productId = productComboBox.getValue().id();
            }

            StoreProductCreateDTO dto = StoreProductCreateDTO.builder()
                    .UPC(upc)
                    .UPCprom(isPromotional ? upcProm : null)
                    .productId(productId)
                    .price(Double.parseDouble(priceField.getText().trim()))
                    .quantity(Integer.parseInt(quantityField.getText().trim()))
                    .promotional(isPromotional)
                    .build();

            if (isEditMode) {
                storeProductService.updateStoreProduct(dto);
            } else {
                storeProductService.addStoreProduct(dto);
            }

            closeWindow(formPanel);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid price or quantity format").showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred while saving: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void onCancel() {
        closeWindow(formPanel);
    }
}