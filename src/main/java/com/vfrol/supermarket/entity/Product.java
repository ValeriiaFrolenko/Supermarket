package com.vfrol.supermarket.entity;

public record Product(
        int id,
        int categoryId,
        String name,
        String characteristics
) {
}
