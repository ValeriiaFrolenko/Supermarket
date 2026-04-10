package com.vfrol.supermarket.filter;

import java.time.LocalDate;

import com.vfrol.supermarket.enums.sortby.CheckSortBy;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CheckFilter {
    private String checkNumber;
    private String cashierSurname;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private CheckSortBy sortBy;

    public boolean isEmpty() {
        return getCheckNumber() == null &&
                getCashierSurname() == null &&
                dateFrom == null &&
                dateTo == null &&
                sortBy == null;
    }
}