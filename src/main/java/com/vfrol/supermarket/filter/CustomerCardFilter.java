package com.vfrol.supermarket.filter;

import com.vfrol.supermarket.enums.sortby.CustomerCardSortBy;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerCardFilter {
    private String surname;
    private String phoneNumber;
    private Integer discountFrom;
    private Integer discountTo;
    private CustomerCardSortBy sortBy;

    public boolean isEmpty() {
        return getSurname() == null &&
                getPhoneNumber() == null &&
                discountFrom == null &&
                discountTo == null &&
                sortBy == null;
    }

    public String getPhoneNumber() {
        if (phoneNumber == null) return null;
        if(phoneNumber.startsWith("+380")) return phoneNumber;
        if(phoneNumber.startsWith("380")) return "+" + phoneNumber;
        if(phoneNumber.startsWith("0")) return "+38" + phoneNumber;
        return phoneNumber;
    }
}