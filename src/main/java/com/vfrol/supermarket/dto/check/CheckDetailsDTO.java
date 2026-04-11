package com.vfrol.supermarket.dto.check;

import lombok.Builder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import java.time.LocalDateTime;

@Builder
public record CheckDetailsDTO(
        @ColumnName("check_number") String checkNumber,
        @ColumnName("employee_name") String employeeName,
        @ColumnName("card_number") String cardNumber,
        @ColumnName("customer_name") String customerName,
        @ColumnName("print_date") LocalDateTime dateTime,
        @ColumnName("sum_total") double sumTotal,
        @ColumnName("vat") double vat,

        @ColumnName("base_sum") double baseSum,
        @ColumnName("discount_amount") double discountAmount
) {}