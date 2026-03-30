package com.vfrol.supermarket.entity;

import com.vfrol.supermarket.enums.EmployeeRole;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Employee(
        String id,
        String passwordHash,
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
