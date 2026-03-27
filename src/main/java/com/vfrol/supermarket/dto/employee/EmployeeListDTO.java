package com.vfrol.supermarket.dto.employee;

import com.vfrol.supermarket.enums.EmployeeRole;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

public record EmployeeListDTO(
        @ColumnName("id_employee") String id,
        @ColumnName("empl_surname") String surname,
        @ColumnName("empl_name") String name,
        @ColumnName("empl_role") EmployeeRole role
) {
}