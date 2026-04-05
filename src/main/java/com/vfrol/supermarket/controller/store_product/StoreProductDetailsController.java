package com.vfrol.supermarket.controller.store_product;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.config.ViewManager;
import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import com.vfrol.supermarket.service.StoreProductService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StoreProductDetailsController {

    @FXML private VBox detailsPanel;

    @FXML private Label upcLabel;
    @FXML private Label upcPromLabel;
    @FXML private Label productNameLabel;
    @FXML private Label categoryNameLabel;
    @FXML private Label priceLabel;
    @FXML private Label quantityLabel;
    @FXML private Label promotionalLabel;

    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private StoreProductDetailsDTO currentStoreProduct;
    private final StoreProductService storeProductService;
    private final ViewManager viewManager;
    private final SessionManager sessionManager;

    @Inject
    public StoreProductDetailsController(StoreProductService storeProductService,
                                         ViewManager viewManager,
                                         SessionManager sessionManager) {
        this.storeProductService = storeProductService;
        this.viewManager = viewManager;
        this.sessionManager = sessionManager;
    }

    @FXML
    public void initialize() {
        if (!sessionManager.isManager()) {
            editButton.setVisible(false);
            editButton.setManaged(false);
            deleteButton.setVisible(false);
            deleteButton.setManaged(false);
        }
    }

    public void setStoreProductDetails(StoreProductDetailsDTO dto) {
        this.currentStoreProduct = dto;
        upcLabel.setText(dto.UPC());
        upcPromLabel.setText(dto.UPCprom() != null ? dto.UPCprom() : "—");
        productNameLabel.setText(dto.productName());
        categoryNameLabel.setText(dto.categoryName());
        priceLabel.setText(String.format("%.2f", dto.price()));
        quantityLabel.setText(String.valueOf(dto.quantity()));
        promotionalLabel.setText(dto.promotional() != null && dto.promotional() ? "Yes" : "No");
    }

    @FXML
    public void onEdit() {
        viewManager.showDialog(AppView.STORE_PRODUCT_FORM, (StoreProductFormController controller) ->
                controller.setStoreProduct(currentStoreProduct));
        hide();
    }

    @FXML
    public void onDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this store product?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                storeProductService.deleteStoreProduct(currentStoreProduct.UPC());
                hide();
            }
        });
    }

    @FXML
    public void hide() {
        Stage window = (Stage) detailsPanel.getScene().getWindow();
        if (window != null) window.close();
    }
}
