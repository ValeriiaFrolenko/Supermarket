package com.vfrol.supermarket.dto.customer_card;

public record CustomerCardCreateDTO(
        String cardNumber,
        String surname,
        String name,
        String patronymic,
        String phoneNumber,
        String city,
        String street,
        String zipCode,
        int discount
) {
}