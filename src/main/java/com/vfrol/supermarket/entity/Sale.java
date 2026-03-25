package com.vfrol.supermarket.entity;

public record Sale(
        String UPC,
        String checkNumber,
        int quantity,
        double price
) {
}