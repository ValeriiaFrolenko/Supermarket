package com.vfrol.supermarket.dto.sale;

public record SaleCreateDTO(
        String UPC,
        String checkNumber,
        int quantity
){
}