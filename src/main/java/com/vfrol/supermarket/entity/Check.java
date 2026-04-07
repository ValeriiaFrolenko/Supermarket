package com.vfrol.supermarket.entity;

import lombok.Builder;

@Builder
public record Check(
        String checkNumber,
        String idEmployee,
        String cardNumber,
        double sumTotal,
        double vat
) {
}
