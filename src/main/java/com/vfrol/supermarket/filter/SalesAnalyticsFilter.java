package com.vfrol.supermarket.filter;

import com.vfrol.supermarket.enums.sortby.SalesAnalyticsSortBy;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class SalesAnalyticsFilter {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Integer productId;
    private String employeeId;
    private SalesAnalyticsSortBy sortBy;
}