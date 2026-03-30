package com.vfrol.supermarket.dto.store_product;
import lombok.Builder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Builder
public record StoreProductListDTO(
        @ColumnName("UPC") String UPC,
        @ColumnName("product_name") String productName,
        @ColumnName("selling_price") double price,
        @ColumnName("products_number") int quantity,
        @ColumnName("promotional_product") Boolean promotional
) {}