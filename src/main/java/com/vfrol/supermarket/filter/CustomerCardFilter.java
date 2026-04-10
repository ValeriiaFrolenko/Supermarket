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
        String normalized = phoneNumber.replaceAll("[^+0-9]", "");
        if (normalized.isEmpty()) return null;
        if (normalized.startsWith("+380")) return normalized;
        if (normalized.startsWith("380"))  return "+" + normalized;
        if (normalized.startsWith("0"))    return "+38" + normalized;
        return null;
    }
}