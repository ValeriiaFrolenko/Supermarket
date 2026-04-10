package com.vfrol.supermarket.filter;

import com.vfrol.supermarket.enums.sortby.StoreProductSortBy;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoreProductFilter {
    private String upc;
    private String productName;
    private Integer categoryId;
    private Boolean promotional;
    private StoreProductSortBy sortBy;

    public boolean isEmpty() {
        return getUpc() == null &&
                getProductName() == null &&
                categoryId == null &&
                promotional == null &&
                sortBy == null;
    }
}