package com.vfrol.supermarket.dto.employee;

import com.vfrol.supermarket.enums.EmployeeRole;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EmployeeCreateDTO(
        String id,
        String rawPassword,
        String surname,
        String name,
        String patronymic,
        EmployeeRole role,
        double salary,
        LocalDate dateOfBirth,
        LocalDate dateOfStart,
        String phoneNumber,
        String city,
        String street,
        String zipCode
) {
}
