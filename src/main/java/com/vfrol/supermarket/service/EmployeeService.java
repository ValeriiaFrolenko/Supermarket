package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.EmployeeDAO;
import com.vfrol.supermarket.dto.employee.EmployeeCreateDTO;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.dto.employee.EmployeeListDTO;
import com.vfrol.supermarket.entity.Employee;
import com.vfrol.supermarket.enums.EmployeeRole;
import com.vfrol.supermarket.filter.EmployeeFilter;
import com.vfrol.supermarket.service.validator.EmployeeValidator;
import com.vfrol.supermarket.exception.ValidationException;
import com.vfrol.supermarket.tools.PasswordManager;

import java.time.LocalDate;
import java.util.List;

@Singleton
public class EmployeeService {
    private final EmployeeDAO employeeDAO;
    private final EmployeeValidator employeeValidator;

    private static final String INITIAL_ADMIN_ID = "admin";
    private static final String INITIAL_ADMIN_PASSWORD = "admin";

    @Inject
    public EmployeeService(EmployeeDAO employeeDAO, EmployeeValidator employeeValidator) {
        this.employeeDAO = employeeDAO;
        this.employeeValidator = employeeValidator;
    }

    public void addEmployee(EmployeeCreateDTO dto) {
        String passwordHash = PasswordManager.generatePassword(dto.rawPassword());
        employeeDAO.create(createEmployee(dto, passwordHash));
    }

    public void updateEmployee(EmployeeCreateDTO dto) {
        String finalPasswordHash;
        if (dto.rawPassword() == null || dto.rawPassword().trim().isEmpty()) {
            finalPasswordHash = employeeDAO.getPasswordById(dto.id())
                    .orElseThrow(() -> new ValidationException("Employee record not found in database."));
        } else {
            finalPasswordHash = PasswordManager.generatePassword(dto.rawPassword());
        }
        employeeDAO.update(createEmployee(dto, finalPasswordHash));
    }

    public void deleteEmployee(String id) {
        employeeValidator.validateForDelete(id);
        employeeDAO.delete(id);
    }

    public EmployeeDetailsDTO getEmployeeById(String id) {
        return employeeDAO.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public List<EmployeeListDTO> getAllEmployees() {
        return employeeDAO.findAll();
    }

    public List<EmployeeListDTO> getEmployeesByFilter(EmployeeFilter filter) {
        return employeeDAO.findByFilter(filter);
    }

    public List<EmployeeDetailsDTO> getAllEmployeeDetails() {
        return employeeDAO.findAllDetails();
    }

    public boolean authenticateEmployee(String id, String rawPassword) {
        return employeeDAO.getPasswordById(id)
                .filter(hash -> PasswordManager.verifyPassword(rawPassword, hash))
                .isPresent();
    }

    public boolean isEmpty() {
        return employeeDAO.isEmpty();
    }

    private Employee createEmployee(EmployeeCreateDTO dto, String passwordHash) {
        return Employee.builder()
                .id(dto.id())
                .passwordHash(passwordHash)
                .surname(dto.surname())
                .name(dto.name())
                .patronymic(dto.patronymic())
                .role(dto.role())
                .salary(dto.salary())
                .dateOfBirth(dto.dateOfBirth())
                .dateOfStart(dto.dateOfStart())
                .phoneNumber(dto.phoneNumber())
                .city(dto.city())
                .street(dto.street())
                .zipCode(dto.zipCode())
                .build();
    }

    public void createInitialAdmin() {
        EmployeeCreateDTO createDTO = EmployeeCreateDTO.builder()
                .id(INITIAL_ADMIN_ID)
                .rawPassword(INITIAL_ADMIN_PASSWORD)
                .surname("Admin")
                .name("Admin")
                .role(EmployeeRole.MANAGER)
                .salary(0.0)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .dateOfStart(LocalDate.now())
                .phoneNumber("0000000000")
                .city("City")
                .street("Street")
                .zipCode("00000")
                .build();
        addEmployee(createDTO);
    }
}