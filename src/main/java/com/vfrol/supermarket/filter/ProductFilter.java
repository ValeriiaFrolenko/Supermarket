package com.vfrol.supermarket.filter;

import com.vfrol.supermarket.enums.sortby.ProductSortBy;
import lombok.Builder;

@Builder
public record ProductFilter(
        String name,
        String categoryId,
        ProductSortBy sortBy
) {
    public boolean isEmpty() {
        return name == null &&
                categoryId == null &&
                sortBy == null;
    }
}