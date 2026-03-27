package com.vfrol.supermarket.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.vfrol.supermarket.enums.sortby.CheckSortBy;
import lombok.Builder;

@Builder
public record CheckFilter(
        String checkNumber,
        String cashierSurname,
        LocalDate dateFrom,
        LocalDate dateTo,
        CheckSortBy sortBy
) {
    public boolean isEmpty() {
        return  checkNumber == null &&
                cashierSurname == null &&
                dateFrom == null &&
                dateTo == null &&
                sortBy == null;
    }
}