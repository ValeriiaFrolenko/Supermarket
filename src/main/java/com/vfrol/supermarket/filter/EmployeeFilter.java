package com.vfrol.supermarket.filter;

import com.vfrol.supermarket.enums.EmployeeRole;
import lombok.Builder;

@Builder
public record EmployeeFilter(
        String surname,
        String phoneNumber,
        EmployeeRole role
) {
    public boolean isEmpty() {
        return surname == null &&
                phoneNumber == null &&
                role == null;
    }
}