package com.vfrol.supermarket.controller.customer_card;

import com.google.inject.Inject;
import com.vfrol.supermarket.dto.customer_card.CustomerCardCreateDTO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import com.vfrol.supermarket.service.CustomerCardService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerCardFormController {

    @FXML private VBox formPanel;
    @FXML private Label label;
    @FXML private TextField cardNumberField;
    @FXML private TextField surnameField;
    @FXML private TextField nameField;
    @FXML private TextField patronymicField;
    @FXML private TextField phoneField;
    @FXML private TextField cityField;
    @FXML private TextField streetField;
    @FXML private TextField zipField;
    @FXML private TextField discountField;

    private final CustomerCardService customerCardService;
    private boolean isEditMode = false;

    @Inject
    public CustomerCardFormController(CustomerCardService customerCardService) {
        this.customerCardService = customerCardService;
    }

    @FXML
    public void initialize() {
        label.setText("Add Customer Card");
    }

    public void setCustomerCard(CustomerCardDetailsDTO dto) {
        this.isEditMode = true;
        label.setText("Edit Customer Card");
        cardNumberField.setText(dto.cardNumber());
        cardNumberField.setDisable(true);
        surnameField.setText(dto.surname());
        nameField.setText(dto.name());
        patronymicField.setText(dto.patronymic() != null ? dto.patronymic() : "");
        phoneField.setText(dto.phoneNumber());
        cityField.setText(dto.city() != null ? dto.city() : "");
        streetField.setText(dto.street() != null ? dto.street() : "");
        zipField.setText(dto.zipCode() != null ? dto.zipCode() : "");
        discountField.setText(String.valueOf(dto.discount()));
    }

    @FXML
    public void onSave() {
        try {
            int discount = Integer.parseInt(discountField.getText().trim());
            if (discount < 0 || discount > 100) {
                new Alert(Alert.AlertType.WARNING, "Discount must be between 0 and 100.").showAndWait();
                return;
            }

            CustomerCardCreateDTO dto = new CustomerCardCreateDTO(
                    cardNumberField.getText().trim(),
                    surnameField.getText().trim(),
                    nameField.getText().trim(),
                    patronymicField.getText().isBlank() ? null : patronymicField.getText().trim(),
                    phoneField.getText().trim(),
                    cityField.getText().isBlank() ? null : cityField.getText().trim(),
                    streetField.getText().isBlank() ? null : streetField.getText().trim(),
                    zipField.getText().isBlank() ? null : zipField.getText().trim(),
                    discount
            );

            if (isEditMode) {
                customerCardService.updateCard(dto);
            } else {
                customerCardService.addCard(dto);
            }
            closeWindow();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid discount format. Please enter a whole number.").showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "An error occurred while saving: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage window = (Stage) formPanel.getScene().getWindow();
        if (window != null) {
            window.close();
        }
    }
}
