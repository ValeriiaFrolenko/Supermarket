package com.vfrol.supermarket.entity;

import java.time.LocalDateTime;

public record Check(
        String checkNumber,
        String idEmployee,
        String cardNumber,
        LocalDateTime dateTime,
        double sumTotal,
        double vat
) {
}
