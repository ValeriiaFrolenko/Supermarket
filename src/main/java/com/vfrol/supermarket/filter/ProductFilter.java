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
        return getName() == null &&
                categoryId == null &&
                sortBy == null;
    }

    public String getName() {
        if (name == null) return null;
        if (!name.matches("[a-zA-Zа-яА-ЯіІїЇєЄ0-9'\\- ]+")) return null;
        return name;
    }
}