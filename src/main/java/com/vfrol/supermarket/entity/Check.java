package com.vfrol.supermarket.entity;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Check(
        String checkNumber,
        String idEmployee,
        String cardNumber,
        double sumTotal,
        double vat
) {
}
