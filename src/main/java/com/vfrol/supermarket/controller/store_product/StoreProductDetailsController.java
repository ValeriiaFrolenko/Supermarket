package com.vfrol.supermarket.controller.store_product;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseDetailsController;
import com.vfrol.supermarket.controller.util.SessionUIHelper;
import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import com.vfrol.supermarket.service.StoreProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class StoreProductDetailsController extends BaseDetailsController<StoreProductDetailsDTO> {

    private final StoreProductService storeProductService;

    @FXML private Label upcLabel;
    @FXML private Label upcPromLabel;
    @FXML private Label productNameLabel;
    @FXML private Label categoryNameLabel;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private Label promotionalLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @Inject
    public StoreProductDetailsController(StoreProductService storeProductService) {
        this.storeProductService = storeProductService;
    }

    @FXML
    public void initialize() {
        SessionUIHelper.configureManagerOnlyNodes(sessionManager, editButton, deleteButton);
    }

    @Override
    protected String getEntityName() {
        return "Store Product";
    }

    @Override
    protected void populateFields(StoreProductDetailsDTO dto) {
        upcLabel.setText(dto.UPC());
        upcPromLabel.setText(dto.UPCprom() != null ? dto.UPCprom() : "—");
        productNameLabel.setText(dto.productName());
        categoryNameLabel.setText(dto.categoryName());
        priceLabel.setText(String.format("%.2f", dto.price()));
        quantityLabel.setText(String.valueOf(dto.quantity()));
        promotionalLabel.setText(Boolean.TRUE.equals(dto.promotional()) ? "Yes" : "No");
    }

    @Override
    protected void deleteEntity() {
        storeProductService.deleteStoreProduct(currentItem.UPC());
    }

    @Override
    protected void navigateToForm() {
        viewManager.showDialog(AppView.STORE_PRODUCT_FORM,
                (StoreProductFormController controller) ->
                        controller.setupForEdit(currentItem));
    }
}