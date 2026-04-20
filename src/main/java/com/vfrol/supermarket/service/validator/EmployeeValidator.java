package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.dao.CheckDAO;
import com.vfrol.supermarket.dao.EmployeeDAO;
import com.vfrol.supermarket.dto.employee.EmployeeCreateDTO;
import com.vfrol.supermarket.enums.EmployeeRole;
import com.vfrol.supermarket.exception.ValidationException;

@Singleton
public class EmployeeValidator {

    private final EmployeeDAO employeeDAO;
    private final SessionManager sessionManager;

    @Inject
    public EmployeeValidator(EmployeeDAO employeeDAO, SessionManager sessionManager) {
        this.employeeDAO = employeeDAO;
        this.sessionManager = sessionManager;
    }

    public void validateForDelete(String id) {
        if (id.equals(sessionManager.getCurrentUser().id()))
            throw new ValidationException("You cannot delete your own account.");
        if (employeeDAO.countByRole(EmployeeRole.MANAGER) <= 1
                && employeeDAO.existsByIdAndRole(id, EmployeeRole.MANAGER))
            throw new ValidationException("Cannot delete the last manager. Assign another manager first.");
    }
}