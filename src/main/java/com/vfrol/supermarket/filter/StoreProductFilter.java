package com.vfrol.supermarket.filter;

import com.vfrol.supermarket.enums.sortby.StoreProductSortBy;
import lombok.Builder;

@Builder
public record StoreProductFilter(
        String upc,
        String productName,
        String categoryId,
        Boolean promotional,
        StoreProductSortBy sortBy
) {
    public boolean isEmpty() {
        return upc == null &&
                productName == null &&
                categoryId == null &&
                promotional == null &&
                sortBy == null;
    }
}