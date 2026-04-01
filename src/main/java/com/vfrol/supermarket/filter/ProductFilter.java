package com.vfrol.supermarket.filter;

import com.vfrol.supermarket.enums.sortby.ProductSortBy;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductFilter {
    private String name;
    private Integer categoryId;
    private ProductSortBy sortBy;

    public boolean isEmpty() {
        return name == null &&
                categoryId == null &&
                sortBy == null;
    }
}