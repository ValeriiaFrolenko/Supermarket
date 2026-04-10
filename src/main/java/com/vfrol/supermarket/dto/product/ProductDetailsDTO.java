package com.vfrol.supermarket.dto.product;

import lombok.Builder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Builder
public record ProductDetailsDTO(
        @ColumnName("id_product") int id,
        @ColumnName("product_name") String name,
        @ColumnName("category_number") int categoryId,
        @ColumnName("category_name") String categoryName,
        @ColumnName("characteristics") String characteristics
) {
}