package com.vfrol.supermarket.controller.customer_card;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseDetailsController;
import com.vfrol.supermarket.controller.util.SessionUIHelper;
import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import com.vfrol.supermarket.service.CustomerCardService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CustomerCardDetailsController extends BaseDetailsController<CustomerCardDetailsDTO> {

    private final CustomerCardService customerCardService;

    @FXML private Label cardNumberLabel;
    @FXML private Label surnameLabel;
    @FXML private Label nameLabel;
    @FXML private Label patronymicLabel;
    @FXML private Label phoneLabel;
    @FXML private Label cityLabel;
    @FXML private Label streetLabel;
    @FXML private Label zipLabel;
    @FXML private Label discountLabel;
    @FXML private Button deleteButton;


    @Inject
    public CustomerCardDetailsController(CustomerCardService customerCardService) {
        this.customerCardService = customerCardService;
    }

    @FXML
    public void initialize() {
        SessionUIHelper.configureManagerOnlyNodes(sessionManager, deleteButton);
    }

    @Override
    protected String getEntityName() {
        return "Customer Card";
    }

    @Override
    protected void populateFields(CustomerCardDetailsDTO dto) {
        cardNumberLabel.setText(dto.cardNumber());
        surnameLabel.setText(dto.surname());
        nameLabel.setText(dto.name());
        patronymicLabel.setText(dto.patronymic() != null ? dto.patronymic() : "");
        phoneLabel.setText(dto.phoneNumber());
        cityLabel.setText(dto.city() != null ? dto.city() : "");
        streetLabel.setText(dto.street() != null ? dto.street() : "");
        zipLabel.setText(dto.zipCode() != null ? dto.zipCode() : "");
        discountLabel.setText(dto.discount() + "%");
    }

    @Override
    protected void deleteEntity() {
        customerCardService.deleteCard(currentItem.cardNumber());
    }

    @Override
    protected void navigateToForm() {
        viewManager.showDialog(AppView.CUSTOMER_CARD_FORM, (CustomerCardFormController controller) ->
                controller.setupForEdit(currentItem));
    }
}