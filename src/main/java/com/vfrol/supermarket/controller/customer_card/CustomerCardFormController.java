package com.vfrol.supermarket.controller.customer_card;

import com.google.inject.Inject;
import com.vfrol.supermarket.controller.base.BaseFormController;
import com.vfrol.supermarket.controller.ui_validator.CustomerCardFormValidator;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.dto.customer_card.CustomerCardCreateDTO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import com.vfrol.supermarket.service.CustomerCardService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CustomerCardFormController extends BaseFormController<CustomerCardCreateDTO, CustomerCardDetailsDTO> {

    private final CustomerCardService customerCardService;

    @FXML private TextField cardNumberField;
    @FXML private TextField surnameField;
    @FXML private TextField nameField;
    @FXML private TextField patronymicField;
    @FXML private TextField phoneField;
    @FXML private TextField cityField;
    @FXML private TextField streetField;
    @FXML private TextField zipField;
    @FXML private TextField discountField;

    @Inject
    public CustomerCardFormController(CustomerCardService customerCardService) {
        this.customerCardService = customerCardService;
    }

    @Override
    protected String getEntityName() {
        return "Customer Card";
    }

    @Override
    protected void setupValidation() {
        CustomerCardFormValidator cardValidator = new CustomerCardFormValidator(validator);

        cardValidator.validateCardNumber(cardNumberField);
        cardValidator.validateSurname(surnameField);
        cardValidator.validateName(nameField);
        cardValidator.validatePatronymic(patronymicField);
        cardValidator.validatePhone(phoneField);
        cardValidator.validateCity(cityField);
        cardValidator.validateStreet(streetField);
        cardValidator.validateZipCode(zipField);
        cardValidator.validateDiscount(discountField);
    }

    @Override
    protected void populateFields(CustomerCardDetailsDTO dto) {
        cardNumberField.setText(dto.cardNumber());
        cardNumberField.setDisable(true); // Заборона редагування ID
        surnameField.setText(dto.surname());
        nameField.setText(dto.name());
        patronymicField.setText(dto.patronymic() != null ? dto.patronymic() : "");
        phoneField.setText(dto.phoneNumber());
        cityField.setText(dto.city() != null ? dto.city() : "");
        streetField.setText(dto.street() != null ? dto.street() : "");
        zipField.setText(dto.zipCode() != null ? dto.zipCode() : "");
        discountField.setText(String.valueOf(dto.discount()));
    }

    @Override
    protected CustomerCardCreateDTO buildDTO() {
        return CustomerCardCreateDTO.builder()
                .cardNumber(InputHelper.getString(cardNumberField))
                .surname(InputHelper.getString(surnameField))
                .name(InputHelper.getString(nameField))
                .patronymic(InputHelper.getString(patronymicField))
                .phoneNumber(InputHelper.getString(phoneField))
                .city(InputHelper.getString(cityField))
                .street(InputHelper.getString(streetField))
                .zipCode(InputHelper.getString(zipField))
                .discount(InputHelper.getInt(discountField) != null ? InputHelper.getInt(discountField) : 0)
                .build();
    }

    @Override
    protected void saveEntity(CustomerCardCreateDTO dto) {
        if (isEditMode) {
            customerCardService.updateCard(dto);
        } else {
            customerCardService.addCard(dto);
        }
    }
}