package com.vfrol.supermarket.filter;

import com.vfrol.supermarket.enums.EmployeeRole;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmployeeFilter{
    private String surname;
    private String phoneNumber;
    private EmployeeRole role;

    public boolean isEmpty() {
        return surname == null &&
                phoneNumber == null &&
                role == null;
    }
}