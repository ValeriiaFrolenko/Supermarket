package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.CheckDAO;
import com.vfrol.supermarket.dao.CustomerCardDAO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardCreateDTO;

@Singleton
public class CustomerCardValidator{

    private final CustomerCardDAO customerCardDAO;
    private final CheckDAO checkDAO;

    @Inject
    public CustomerCardValidator(CustomerCardDAO customerCardDAO, CheckDAO checkDAO) {
        this.customerCardDAO = customerCardDAO;
        this.checkDAO = checkDAO;
    }

    public void validateForCreate(CustomerCardCreateDTO dto) {
        if (customerCardDAO.existsByCardNumber(dto.cardNumber())) {
            throw new ValidationException("Customer card with number '" + dto.cardNumber() + "' already exists.");
        }
    }

    public void validateForUpdate(CustomerCardCreateDTO dto) {
        if (!customerCardDAO.existsByCardNumber(dto.cardNumber())) {
            throw new ValidationException("Customer card with number '" + dto.cardNumber() + "' does not exist.");
        }
    }

    public void validateForDelete(String cardNumber) {
       if (!customerCardDAO.existsByCardNumber(cardNumber)) {
            throw new ValidationException("Customer card with number '" + cardNumber + "' does not exist.");
        }

        if (checkDAO.existsByCardNumber(cardNumber)) {
            throw new ValidationException(
                    "Cannot delete customer card '" + cardNumber + "' because it has been used in existing checks."
            );
        }
    }
}