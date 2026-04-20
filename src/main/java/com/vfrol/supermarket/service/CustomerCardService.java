package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.CustomerCardDAO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardCreateDTO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardListDTO;
import com.vfrol.supermarket.entity.CustomerCard;
import com.vfrol.supermarket.filter.CustomerCardFilter;

import java.util.List;

@Singleton
public class CustomerCardService {

    private final CustomerCardDAO customerCardDAO;

    @Inject
    public CustomerCardService(CustomerCardDAO customerCardDAO) {
        this.customerCardDAO = customerCardDAO;
    }

    public void addCard(CustomerCardCreateDTO dto) {
        customerCardDAO.create(buildEntity(dto));
    }

    public void updateCard(CustomerCardCreateDTO dto) {
        customerCardDAO.update(buildEntity(dto));
    }

    public void deleteCard(String cardNumber) {
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