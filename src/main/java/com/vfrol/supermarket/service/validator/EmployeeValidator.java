package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.dao.CheckDAO;
import com.vfrol.supermarket.dao.EmployeeDAO;
import com.vfrol.supermarket.dto.employee.EmployeeCreateDTO;
import com.vfrol.supermarket.enums.EmployeeRole;

@Singleton
public class EmployeeValidator extends BaseValidator {

    private final EmployeeDAO employeeDAO;
    private final SessionManager sessionManager;
    private final CheckDAO checkDAO;

    @Inject
    public EmployeeValidator(EmployeeDAO employeeDAO, SessionManager sessionManager, CheckDAO checkDAO) {
        this.employeeDAO = employeeDAO;
        this.sessionManager = sessionManager;
        this.checkDAO = checkDAO;
    }

    public void validateForCreate(EmployeeCreateDTO dto) {
        requireNotExists(
                employeeDAO.findById(dto.id()),
                "Employee with ID '" + dto.id() + "' already exists."
        );
    }

    public void validateForUpdate(EmployeeCreateDTO dto) {
        requireExists(
                employeeDAO.findById(dto.id()),
                "Employee with ID '" + dto.id() + "' does not exist."
        );
    }

    public void validateForDelete(String id) {
        requireExists(
                employeeDAO.findById(id),
                "Employee with ID '" + id + "' does not exist."
        );

        if (id.equals(sessionManager.getCurrentUser().id())) {
            throw new ValidationException("You cannot delete your own account.");
        }

        if (employeeDAO.countByRole(EmployeeRole.MANAGER) <= 1
                && employeeDAO.findById(id)
                .filter(e -> e.role() == EmployeeRole.MANAGER)
                .isPresent()) {
            throw new ValidationException(
                    "Cannot delete the last manager. Assign another manager first."
            );
        }

        if (checkDAO.existsByEmployeeId(id)) {
            throw new ValidationException(
                    "Cannot delete employee with ID '" + id + "' because they are associated with existing checks."
            );
        }
    }
}