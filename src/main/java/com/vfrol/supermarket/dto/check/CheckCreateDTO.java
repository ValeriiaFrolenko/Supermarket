package com.vfrol.supermarket.dto.check;

import com.vfrol.supermarket.dto.sale.SaleCreateDTO;

import java.util.List;

public record CheckCreateDTO(
        String checkNumber,
        String idEmployee,
        String cardNumber,
        double sumTotal,
        double vat,
        List<SaleCreateDTO> sales
) {
}
