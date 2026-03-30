package com.vfrol.supermarket.entity;

import lombok.Builder;

@Builder
public record StoreProduct(
        String UPC,
        String UPCprom,
        int productId,
        double price,
        int quantity,
        Boolean promotional
) {
}
