package com.vfrol.supermarket.controller.product;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseDetailsController;
import com.vfrol.supermarket.controller.util.SessionUIHelper;
import com.vfrol.supermarket.dto.product.ProductDetailsDTO;
import com.vfrol.supermarket.service.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ProductDetailsController extends BaseDetailsController<ProductDetailsDTO> {

    private final ProductService productService;

    @FXML private Label nameLabel;
    @FXML private Label categoryLabel;
    @FXML private Label manufacturerLabel;
    @FXML private Label characteristicsLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @Inject
    public ProductDetailsController(ProductService productService) {
        this.productService = productService;
    }

    @FXML
    public void initialize() {
        SessionUIHelper.configureManagerOnlyNodes(sessionManager, editButton, deleteButton);
    }

    @Override
    protected String getEntityName() {
        return "Product";
    }

    @Override
    protected void populateFields(ProductDetailsDTO dto) {
        nameLabel.setText(dto.name());
        categoryLabel.setText(dto.categoryName());
        manufacturerLabel.setText(dto.manufacturer());
        characteristicsLabel.setText(dto.characteristics());
    }

    @Override
    protected void deleteEntity() {
        productService.deleteProduct(currentItem.id());
    }

    @Override
    protected void navigateToForm() {
        viewManager.showDialog(AppView.PRODUCT_FORM, (ProductFormController controller) ->
                controller.setupForEdit(currentItem));
    }
}