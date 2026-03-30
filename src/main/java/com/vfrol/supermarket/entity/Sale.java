package com.vfrol.supermarket.entity;

import lombok.Builder;

@Builder
public record Sale(
        String UPC,
        String checkNumber,
        int quantity,
        double price
) {
}