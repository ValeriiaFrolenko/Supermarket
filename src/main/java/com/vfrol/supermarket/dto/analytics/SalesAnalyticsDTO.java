package com.vfrol.supermarket.dto.analytics;

import lombok.Builder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.time.LocalDateTime;

@Builder
public record SalesAnalyticsDTO(
        @ColumnName("product_name")  String        productName,
        @ColumnName("quantity_sold") int           quantitySold,
        @ColumnName("total_amount")  double        totalAmount,
        @ColumnName("check_number")  String        checkNumber,
        @ColumnName("cashier_name")  String        cashierName,
        @ColumnName("print_date")    LocalDateTime dateTime
) {}