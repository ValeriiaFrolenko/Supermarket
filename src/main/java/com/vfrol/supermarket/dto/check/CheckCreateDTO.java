package com.vfrol.supermarket.dto.check;

import com.vfrol.supermarket.dto.sale.SaleCreateDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record CheckCreateDTO(
        String checkNumber,
        String idEmployee,
        String cardNumber,
        Boolean useDiscount,
        List<SaleCreateDTO> sales
) {}
