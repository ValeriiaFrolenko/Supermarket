package com.vfrol.supermarket.entity;

public record StoreProduct(
        String UPC,
        String UPCprom,
        int productId,
        double price,
        int quantity,
        Boolean promotional
) {
}
