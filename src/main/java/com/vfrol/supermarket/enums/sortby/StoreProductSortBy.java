package com.vfrol.supermarket.enums.sortby;

public enum StoreProductSortBy implements SortBy{
    NAME("product_name"),
    QUANTITY("product_number"),
    PRICE("selling_price");

    private final String column;

    StoreProductSortBy(String column) {
        this.column = column;
    }

    @Override
    public String column() {
        return column;
    }
}