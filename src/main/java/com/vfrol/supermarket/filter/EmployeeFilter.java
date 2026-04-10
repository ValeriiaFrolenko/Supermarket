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
        String normalized = phoneNumber.replaceAll("[^+0-9]", "");
        if (normalized.isEmpty()) return null;
        if (normalized.startsWith("+380")) return normalized;
        if (normalized.startsWith("380"))  return "+" + normalized;
        if (normalized.startsWith("0"))    return "+38" + normalized;
        return null;
    }
}