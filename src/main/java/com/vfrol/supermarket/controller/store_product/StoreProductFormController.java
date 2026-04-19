package com.vfrol.supermarket.controller.store_product;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseFormController;
import com.vfrol.supermarket.controller.ui_validator.StoreProductFormValidator;
import com.vfrol.supermarket.controller.util.Debouncer;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.controller.util.SearchableComboBoxHelper;
import com.vfrol.supermarket.dto.product.ProductNameDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductCreateDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductListDTO;
import com.vfrol.supermarket.service.ProductService;
import com.vfrol.supermarket.service.StoreProductService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.controlsfx.control.SearchableComboBox;

import java.util.Locale;

public class StoreProductFormController extends BaseFormController<StoreProductCreateDTO, StoreProductDetailsDTO> {

    private final StoreProductService storeProductService;
    private final ProductService productService;
    private final Debouncer priceDebouncer = new Debouncer(400);

    @FXML private TextField upcField;
    @FXML private SearchableComboBox<StoreProductListDTO> upcPromComboBox;
    @FXML private SearchableComboBox<ProductNameDTO> productComboBox;
    @FXML private TextField priceField;
    @FXML private TextField discountField;
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

        SearchableComboBoxHelper.configureForForm(
                productComboBox,
                productService::getAllProductNames,
                ProductNameDTO::name,
                ProductNameDTO::id
        );

        SearchableComboBoxHelper.configureForForm(
                upcPromComboBox,
                storeProductService::getAllStoreProducts,
                sp -> sp.UPC() + " (" + sp.productName() + ")",
                StoreProductListDTO::UPC
        );

        discountField.setText("20");

        discountField.textProperty().addListener((_, _, _) -> priceDebouncer.debounce(this::recalculatePrice));
        upcPromComboBox.valueProperty().addListener((_, _, _) -> recalculatePrice());

        promotionalCheckBox.selectedProperty().addListener((_, _, isPromotional) -> {
            updateFieldVisibility(isPromotional);
            if (isPromotional) {
                recalculatePrice();
            }
        });

        updateFieldVisibility(false);
    }

    private void recalculatePrice() {
        if (!promotionalCheckBox.isSelected()) return;

        StoreProductListDTO baseProduct = upcPromComboBox.getValue();
        if (baseProduct == null) {
            priceField.setText("");
            return;
        }

        Double discount = InputHelper.getDouble(discountField);
        if (discount == null) discount = 20.0;

        double calculatedPrice = baseProduct.price() * (1 - discount / 100.0);
        priceField.setText(String.format(Locale.US, "%.4f", calculatedPrice));
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
        storeProductValidator.validateUPCProm(upcPromComboBox);
        storeProductValidator.validateProduct(productComboBox);
        storeProductValidator.validatePrice(priceField);
        storeProductValidator.validateDiscount(discountField);
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
            if (dto.UPCprom() != null) {

                StoreProductDetailsDTO baseDetails = storeProductService.getStoreProductByUpc(dto.UPCprom());
                StoreProductListDTO currentBase = StoreProductListDTO.builder()
                        .UPC(baseDetails.UPC())
                        .productName(baseDetails.productName())
                        .price(baseDetails.price())
                        .build();
                upcPromComboBox.getItems().setAll(currentBase);
                upcPromComboBox.setValue(currentBase);
            }
            discountField.setText(dto.discount() != null ? String.valueOf(dto.discount()) : "20.0");
        } else {
            ProductNameDTO current = new ProductNameDTO(dto.productId(), dto.productName());
            productComboBox.getItems().setAll(current);
            productComboBox.setValue(current);
        }
    }

    @Override
    protected StoreProductCreateDTO buildDTO() {
        boolean isPromotional = promotionalCheckBox.isSelected();
        String upcProm = (isPromotional && upcPromComboBox.getValue() != null) ? upcPromComboBox.getValue().UPC() : null;

        int productId;
        if (isPromotional) {
            productId = storeProductService.getProductIdByUpc(upcProm);
        } else {
            productId = productComboBox.getValue().id();
        }

        Double finalPrice;
        if (isPromotional && upcPromComboBox.getValue() != null) {
            finalPrice = upcPromComboBox.getValue().price();
        } else {
            finalPrice = InputHelper.getDouble(priceField);
        }
        if (finalPrice == null) finalPrice = 0.0;

        return StoreProductCreateDTO.builder()
                .UPC(InputHelper.getString(upcField))
                .UPCprom(upcProm)
                .productId(productId)
                .price(finalPrice)
                .quantity(InputHelper.getInt(quantityField))
                .promotional(isPromotional)
                .discount(isPromotional ? InputHelper.getDouble(discountField) : null)
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
        upcPromComboBox.setVisible(isPromotional);
        upcPromComboBox.setManaged(isPromotional);

        productComboBox.setVisible(!isPromotional);
        productComboBox.setManaged(!isPromotional);

        priceField.setDisable(isPromotional);

        discountField.setVisible(isPromotional);
        discountField.setManaged(isPromotional);
    }
}