package com.vfrol.supermarket.controller.product;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.config.ViewManager;
import com.vfrol.supermarket.dto.product.ProductDetailsDTO;
import com.vfrol.supermarket.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductDetailsController {

    @FXML private VBox detailsPanel;
    @FXML private Label nameLabel;
    @FXML private Label categoryLabel;
    @FXML private Label characteristicsLabel;

    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private ProductDetailsDTO currentProduct;
    private final ProductService productService;
    private final ViewManager viewManager;
    private final SessionManager sessionManager;

    @Inject
    public ProductDetailsController(ProductService productService, ViewManager viewManager, SessionManager sessionManager) {
        this.productService = productService;
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

    public void setProductDetails(ProductDetailsDTO dto) {
        this.currentProduct = dto;
        nameLabel.setText(dto.name());
        categoryLabel.setText(dto.categoryName());
        characteristicsLabel.setText(dto.characteristics());
    }

    @FXML
    public void onEdit() {
        viewManager.showDialog(AppView.PRODUCT_FORM, (ProductFormController controller) ->
                controller.setProduct(currentProduct));
        hide();
    }

    @FXML
    public void onDelete() {
        productService.deleteProduct(currentProduct.id());
        hide();
    }

    @FXML
    public void hide() {
        Stage window = (Stage) detailsPanel.getScene().getWindow();
        if (window != null) window.close();
    }
}