package com.vfrol.supermarket.entity;

import com.vfrol.supermarket.enums.EmployeeRole;

import java.time.LocalDate;

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
