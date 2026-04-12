package com.vfrol.supermarket.controller.store_product;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseFormController;
import com.vfrol.supermarket.controller.ui_validator.StoreProductFormValidator;
import com.vfrol.supermarket.controller.util.AlertHelper;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.controller.util.SearchableComboBoxHelper;
import com.vfrol.supermarket.dto.product.ProductNameDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductCreateDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import com.vfrol.supermarket.service.ProductService;
import com.vfrol.supermarket.service.StoreProductService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class StoreProductFormController extends BaseFormController<StoreProductCreateDTO, StoreProductDetailsDTO> {

    private final StoreProductService storeProductService;
    private final ProductService productService;

    @FXML private TextField upcField;
    @FXML private TextField upcPromField;
    @FXML private ComboBox<ProductNameDTO> productComboBox;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private CheckBox promotionalCheckBox;

    @Inject
    public StoreProductFormController(StoreProductService storeProductService,
                                      ProductService productService) {
        this.storeProductService = storeProductService;
        this.productService = productService;
    }

    @FXML
    @Override
    public void initialize() {
        super.initialize();

        SearchableComboBoxHelper.configure(
                productComboBox,
                productService::getAllProductNames,
                productService::getProductByName,
                ProductNameDTO::name
        );

        promotionalCheckBox.selectedProperty().addListener((_, _, isPromotional) ->
                updateFieldVisibility(isPromotional));
        updateFieldVisibility(false);
    }

    @Override
    protected String getEntityName() {
        return "Store Product";
    }

    @Override
    protected void setupValidation() {
        StoreProductFormValidator storeProductValidator =
                new StoreProductFormValidator(validator, promotionalCheckBox.selectedProperty());

        storeProductValidator.validateUPC(upcField);
        storeProductValidator.validateUPCProm(upcPromField);
        storeProductValidator.validateProduct(productComboBox);
        storeProductValidator.validatePrice(priceField);
        storeProductValidator.validateQuantity(quantityField);
    }

    @Override
    protected void populateFields(StoreProductDetailsDTO dto) {
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

    @Override
    protected StoreProductCreateDTO buildDTO() {
        boolean isPromotional = promotionalCheckBox.isSelected();
        String upcProm = isPromotional ? InputHelper.getString(upcPromField) : null;

        int productId;
        if (isPromotional) {
            productId = storeProductService.getProductIdByUpc(upcProm);
        } else {
            productId = productComboBox.getValue().id();
        }

        return StoreProductCreateDTO.builder()
                .UPC(InputHelper.getString(upcField))
                .UPCprom(upcProm)
                .productId(productId)
                .price(InputHelper.getDouble(priceField))
                .quantity(InputHelper.getInt(quantityField))
                .promotional(isPromotional)
                .build();
    }

    @Override
    protected void saveEntity(StoreProductCreateDTO dto) {
        if (isEditMode) {
            storeProductService.updateStoreProduct(dto);
        } else {
            storeProductService.addStoreProduct(dto);
        }
    }


    private void updateFieldVisibility(boolean isPromotional) {
        upcPromField.setVisible(isPromotional);
        upcPromField.setManaged(isPromotional);

        productComboBox.setVisible(!isPromotional);
        productComboBox.setManaged(!isPromotional);
    }
}