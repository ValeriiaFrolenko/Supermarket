package com.vfrol.supermarket.dto.employee;

import com.vfrol.supermarket.enums.EmployeeRole;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.time.LocalDate;

public record EmployeeDetailsDTO(
        @ColumnName("id_employee") String id,
        @ColumnName("empl_surname") String surname,
        @ColumnName("empl_name") String name,
        @ColumnName("empl_patronymic") String patronymic,
        @ColumnName("empl_role") EmployeeRole role,
        @ColumnName("salary") double salary,
        @ColumnName("date_of_birth") LocalDate dateOfBirth,
        @ColumnName("date_of_start") LocalDate dateOfStart,
        @ColumnName("phone_number") String phoneNumber,
        @ColumnName("city") String city,
        @ColumnName("street") String street,
        @ColumnName("zip_code") String zipCode
) {
}