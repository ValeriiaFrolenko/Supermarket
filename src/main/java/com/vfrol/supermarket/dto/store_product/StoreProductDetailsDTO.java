package com.vfrol.supermarket.dto.store_product;

import lombok.Builder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Builder
public record StoreProductDetailsDTO(
        @ColumnName("UPC") String UPC,
        @ColumnName("UPC_prom") String UPCprom,
        @ColumnName("id_product") int productId,
        @ColumnName("product_name") String productName,
        @ColumnName("category_name") String categoryName,
        @ColumnName("selling_price") double price,
        @ColumnName("products_number") int quantity,
        @ColumnName("promotional_product") Boolean promotional
) {
}
