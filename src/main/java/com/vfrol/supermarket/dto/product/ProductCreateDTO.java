package com.vfrol.supermarket.dto.product;

public record ProductCreateDTO(
        int id,
        int categoryId,
        String name,
        String characteristics
) {
}