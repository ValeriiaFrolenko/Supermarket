package com.vfrol.supermarket.dto.product;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public record ProductDetailsDTO(
        @ColumnName("id_product") int id,
        @ColumnName("product_name") String name,
        @ColumnName("category_name") String categoryName,
        @ColumnName("characteristics") String characteristics
) {
}