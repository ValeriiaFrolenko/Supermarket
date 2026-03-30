package com.vfrol.supermarket.dto.check;

import lombok.Builder;
import org.jdbi.v3.core.mapper.reflect.ColumnName;
import java.time.LocalDateTime;

@Builder
public record CheckListDTO(
        @ColumnName("check_number") String checkNumber,
        @ColumnName("employee_name") String employeeName,
        @ColumnName("print_date") LocalDateTime dateTime,
        @ColumnName("sum_total") double sumTotal
) {
}