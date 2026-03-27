package com.vfrol.supermarket.dto.store_product;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public record StoreProductDetailsDTO(
        @ColumnName("UPC") String UPC,
        @ColumnName("UPC_prom") String UPCprom,
        @ColumnName("product_name") String productName,
        @ColumnName("selling_price") double price,
        @ColumnName("products_number") int quantity,
        @ColumnName("promotional_product") Boolean promotional
) {
}