package com.vfrol.supermarket.filter;

import com.vfrol.supermarket.enums.sortby.CustomerCardSortBy;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerCardFilter{
    private String surname;
    private String phoneNumber;
    private Integer discountFrom;
    private Integer discountTo;
    private CustomerCardSortBy sortBy;

    public boolean isEmpty() {
        return surname == null &&
                phoneNumber == null &&
                discountFrom == null &&
                discountTo == null &&
                sortBy == null;
    }
}