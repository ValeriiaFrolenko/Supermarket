package com.vfrol.supermarket.controller.customer_card;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.config.ViewManager;
import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import com.vfrol.supermarket.service.CustomerCardService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerCardDetailsController {

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
    private final ViewManager viewManager;
    private final SessionManager sessionManager;

    @Inject
    public CustomerCardDetailsController(CustomerCardService customerCardService,
                                         ViewManager viewManager,
                                         SessionManager sessionManager) {
        this.customerCardService = customerCardService;
        this.viewManager = viewManager;
        this.sessionManager = sessionManager;
    }

    @FXML
    public void initialize() {
        configureForRole();
    }

    private void configureForRole() {
        boolean isManager = sessionManager.isManager();

        editButton.setVisible(true);
        editButton.setManaged(true);

        deleteButton.setVisible(isManager);
        deleteButton.setManaged(isManager);
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
        Stage window = (Stage) detailsPanel.getScene().getWindow();
        if (window != null) {
            window.close();
        }
    }
}
