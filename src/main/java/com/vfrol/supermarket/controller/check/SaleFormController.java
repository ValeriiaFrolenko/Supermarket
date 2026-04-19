package com.vfrol.supermarket.controller.check;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseModalController;
import com.vfrol.supermarket.controller.ui_validator.SaleFormValidator;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.controller.util.SearchableComboBoxHelper;
import com.vfrol.supermarket.dto.store_product.StoreProductListDTO;
import com.vfrol.supermarket.service.StoreProductService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.Setter;
import net.synedra.validatorfx.Validator;
import org.controlsfx.control.SearchableComboBox;

import java.util.function.Consumer;

public class SaleFormController extends BaseModalController {

    private final StoreProductService storeProductService;
    protected final Validator validator = new Validator();

    @FXML private VBox formPanel;
    @FXML private SearchableComboBox<StoreProductListDTO> storeProductComboBox;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;
    @FXML private Button addButton;


    @Setter
    private Consumer<CheckFormController.SaleItemModel> saveCallback;

    @Inject
    public SaleFormController(StoreProductService storeProductService) {
        this.storeProductService = storeProductService;
    }

    @FXML
    public void initialize() {
        setupValidation();
        configureComboBox();

        priceField.setDisable(true);


        storeProductComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                priceField.setText(String.format("%.2f", newVal.price()).replace(",", "."));
            } else {
                priceField.setText("0.00");
            }
        });
    }

    private void setupValidation() {
        SaleFormValidator saleValidator = new SaleFormValidator(validator);
        saleValidator.validateProduct(storeProductComboBox);
        saleValidator.validateQuantity(quantityField);
        if (addButton != null) {
            addButton.disableProperty().bind(validator.containsErrorsProperty());
        }
    }

    private void configureComboBox() {
        SearchableComboBoxHelper.configureForForm(
                storeProductComboBox,
                storeProductService::getAllStoreProducts,
                sp -> sp.productName() + " (" + sp.UPC() + ")",
                StoreProductListDTO::UPC
        );
    }

    @FXML
    public void onAdd() {
        StoreProductListDTO selectedProduct = storeProductComboBox.getValue();
        int qty = InputHelper.getInt(quantityField);

        if (saveCallback != null) {
            CheckFormController.SaleItemModel saleItem = new CheckFormController.SaleItemModel(
                    selectedProduct.UPC(),
                    selectedProduct.productName(),
                    selectedProduct.price(),
                    qty
            );
            saveCallback.accept(saleItem);
        }
        closeWindow(formPanel);
    }

    @FXML
    public void onCancel() {
        closeWindow(formPanel);
    }
}