package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.CustomerCardDAO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardCreateDTO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardListDTO;
import com.vfrol.supermarket.entity.CustomerCard;
import com.vfrol.supermarket.filter.CustomerCardFilter;
import com.vfrol.supermarket.service.validator.CustomerCardValidator;

import java.util.List;

@Singleton
public class CustomerCardService {

    private final CustomerCardDAO customerCardDAO;
    private final CustomerCardValidator customerCardValidator;

    @Inject
    public CustomerCardService(CustomerCardDAO customerCardDAO, CustomerCardValidator customerCardValidator) {
        this.customerCardDAO = customerCardDAO;
        this.customerCardValidator = customerCardValidator;
    }

    public void addCard(CustomerCardCreateDTO dto) {
        customerCardValidator.validateForCreate(dto);
        CustomerCard card = buildEntity(dto);
        customerCardDAO.create(card);
    }

    public void updateCard(CustomerCardCreateDTO dto) {
        customerCardValidator.validateForUpdate(dto);
        CustomerCard card = buildEntity(dto);
        customerCardDAO.update(card);
    }

    public void deleteCard(String cardNumber) {
        customerCardValidator.validateForDelete(cardNumber);
        customerCardDAO.delete(cardNumber);
    }

    public CustomerCardDetailsDTO getCardById(String cardNumber) {
        return customerCardDAO.findById(cardNumber).orElseThrow(() -> new RuntimeException("Customer card not found"));
    }

    public List<CustomerCardListDTO> getAllCards() {
        return customerCardDAO.findAll();
    }

    public List<CustomerCardDetailsDTO> getAllCustomerCardDetails() {
        return customerCardDAO.findAllDetails();
    }

    public List<CustomerCardListDTO> getCardsByFilter(CustomerCardFilter filter) {
        return customerCardDAO.findByFilter(filter);
    }

    private CustomerCard buildEntity(CustomerCardCreateDTO dto) {
        return CustomerCard.builder()
                .cardNumber(dto.cardNumber())
                .surname(dto.surname())
                .name(dto.name())
                .patronymic(dto.patronymic())
                .phoneNumber(dto.phoneNumber())
                .city(dto.city())
                .street(dto.street())
                .zipCode(dto.zipCode())
                .discount(dto.discount())
                .build();
    }
}