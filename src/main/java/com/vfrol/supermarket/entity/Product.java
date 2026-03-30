package com.vfrol.supermarket.entity;

import lombok.Builder;

@Builder
public record Product(
        int id,
        int categoryId,
        String name,
        String characteristics
) {
}
