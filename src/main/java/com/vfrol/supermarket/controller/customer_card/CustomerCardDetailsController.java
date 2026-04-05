package com.vfrol.supermarket.controller.customer_card;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.BaseModalController;
import com.vfrol.supermarket.controller.SecurityUIHelper;
import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import com.vfrol.supermarket.service.CustomerCardService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class CustomerCardDetailsController extends BaseModalController {

    @FXML private VBox detailsPanel;

    @FXML private Label cardNumberLabel;
    @FXML private Label surnameLabel;
    @FXML private Label nameLabel;
    @FXML private Label patronymicLabel;
    @FXML private Label phoneLabel;
    @FXML private Label cityLabel;
    @FXML private Label streetLabel;
    @FXML private Label zipLabel;
    @FXML private Label discountLabel;

    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private CustomerCardDetailsDTO currentCard;
    private final CustomerCardService customerCardService;

    @Inject
    public CustomerCardDetailsController(CustomerCardService customerCardService) {
        this.customerCardService = customerCardService;
    }

    @FXML
    public void initialize() {
        SecurityUIHelper.configureManagerOnlyNodes(sessionManager, deleteButton);
    }

    public void setCustomerCardDetails(CustomerCardDetailsDTO dto) {
        this.currentCard = dto;
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

    @FXML
    public void onEdit() {
        viewManager.showDialog(AppView.CUSTOMER_CARD_FORM, (CustomerCardFormController controller) ->
                controller.setCustomerCard(currentCard));
        hide();
    }

    @FXML
    public void onDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this customer card?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                customerCardService.deleteCard(currentCard.cardNumber());
                hide();
            }
        });
    }

    @FXML
    public void hide() {
        closeWindow(detailsPanel);
    }
}