package com.vfrol.supermarket.config;

import com.google.inject.Singleton;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.enums.EmployeeRole;
import lombok.Getter;
import lombok.Setter;

@Singleton
@Getter
@Setter
public class SessionManager {
    private EmployeeDetailsDTO currentUser;

    public boolean isManager() {
        return currentUser != null && currentUser.role() == (EmployeeRole.MANAGER);
    }

    public void logout() {
        this.currentUser = null;
    }
}