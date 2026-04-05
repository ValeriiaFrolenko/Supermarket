package com.vfrol.supermarket.dto.product;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public record ProductNameDTO(
        @ColumnName("id_product") int id,
        @ColumnName("product_name") String name
) {
    @Override
    public String toString() { return name; }
}