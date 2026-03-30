package com.vfrol.supermarket.dto.sale;

import lombok.Builder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Builder
public record SaleListDTO(
        @ColumnName("UPC") String UPC,
        @ColumnName("product_name") String productName,
        @ColumnName("product_number") int quantity,
        @ColumnName("selling_price") double unitPrice,
        @ColumnName("total_price") double totalPrice
) {
}