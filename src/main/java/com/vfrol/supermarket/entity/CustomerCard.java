package com.vfrol.supermarket.entity;

import lombok.Builder;

@Builder
public record CustomerCard(
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
