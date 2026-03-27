package com.vfrol.supermarket.filter;

import com.vfrol.supermarket.enums.sortby.CustomerCardSortBy;
import lombok.Builder;

@Builder
public record CustomerCardFilter(
        String surname,
        String phoneNumber,
        Integer discountFrom,
        Integer discountTo,
        CustomerCardSortBy sortBy
) {
    public boolean isEmpty() {
        return surname == null &&
                phoneNumber == null &&
                discountFrom == null &&
                discountTo == null &&
                sortBy == null;
    }
}