package com.vfrol.supermarket.dto.analytics;

import lombok.Builder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Builder
public record EmployeePerformanceDTO(
        @ColumnName("id_employee")   String employeeId,
        @ColumnName("cashier_name")  String cashierName,
        @ColumnName("receipt_count") int    receiptCount,
        @ColumnName("total_amount")  double totalAmount
) {}