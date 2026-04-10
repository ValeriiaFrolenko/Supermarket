package com.vfrol.supermarket.filter;

import com.vfrol.supermarket.enums.EmployeeRole;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmployeeFilter {
    private String surname;
    private String name;
    private String phoneNumber;
    private EmployeeRole role;

    public boolean isEmpty() {
        return getSurname() == null &&
                getName() == null &&
                getPhoneNumber() == null &&
                role == null;
    }

    public String getPhoneNumber() {
        if (phoneNumber == null) return null;
        if(phoneNumber.startsWith("+380")) return phoneNumber;
        if(phoneNumber.startsWith("380")) return "+" + phoneNumber;
        if(phoneNumber.startsWith("0")) return "+38" + phoneNumber;
        return phoneNumber;
    }
}