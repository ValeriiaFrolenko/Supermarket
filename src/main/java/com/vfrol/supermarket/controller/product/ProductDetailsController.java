package com.vfrol.supermarket.controller.product;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.BaseModalController;
import com.vfrol.supermarket.controller.SecurityUIHelper;
import com.vfrol.supermarket.dto.product.ProductDetailsDTO;
import com.vfrol.supermarket.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ProductDetailsController extends BaseModalController {

    @FXML private VBox detailsPanel;
    @FXML private Label nameLabel;
    @FXML private Label categoryLabel;
    @FXML private Label characteristicsLabel;

    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private ProductDetailsDTO currentProduct;
    private final ProductService productService;

    @Inject
    public ProductDetailsController(ProductService productService) {
        this.productService = productService;
    }

    @FXML
    public void initialize() {
        SecurityUIHelper.configureManagerOnlyNodes(sessionManager, editButton, deleteButton);
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
        closeWindow(detailsPanel);
    }
}