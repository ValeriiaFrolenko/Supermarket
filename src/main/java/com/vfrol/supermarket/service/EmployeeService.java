package com.vfrol.supermarket.service;

import com.vfrol.supermarket.dao.EmployeeDAO;
import com.vfrol.supermarket.dto.employee.EmployeeCreateDTO;
import com.vfrol.supermarket.dto.employee.EmployeeDetailsDTO;
import com.vfrol.supermarket.dto.employee.EmployeeListDTO;
import com.vfrol.supermarket.entity.Employee;
import com.vfrol.supermarket.filter.EmployeeFilter;
import com.vfrol.supermarket.tools.PasswordManager;

public class EmployeeService {
    private final EmployeeDAO employeeDAO;
    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public void addEmployee(EmployeeCreateDTO employeeCreateDTO) {
        var employee = createEmployee(employeeCreateDTO);
        employeeDAO.create(employee);
    }

    public void updateEmployee(EmployeeCreateDTO employeeCreateDTO) {
        var employee = createEmployee(employeeCreateDTO);
        employeeDAO.update(employee);
    }

    public void deleteEmployee(String id) {
        employeeDAO.delete(id);
    }

    public EmployeeDetailsDTO getEmployeeById(String id) {
        return employeeDAO.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    public Iterable<EmployeeListDTO> getAllEmployees() {
        return employeeDAO.findAll();
    }

    public Iterable<EmployeeListDTO> getEmployeesByFilter(EmployeeFilter filter) {
        return employeeDAO.findByFilter(filter);
    }

    public boolean authenticateEmployee(String id, String rawPassword) {
        var passwordHash = employeeDAO.getPasswordById(id);
        return passwordHash.filter(string -> PasswordManager.verifyPassword(rawPassword, string)).isPresent();
    }

    private Employee createEmployee(EmployeeCreateDTO employeeCreateDTO) {
        return Employee.builder()
                .id(employeeCreateDTO.id())
                .passwordHash(PasswordManager.generatePassword(employeeCreateDTO.rawPassword()))
                .surname(employeeCreateDTO.surname())
                .name(employeeCreateDTO.name())
                .patronymic(employeeCreateDTO.patronymic())
                .role(employeeCreateDTO.role())
                .salary(employeeCreateDTO.salary())
                .dateOfBirth(employeeCreateDTO.dateOfBirth())
                .dateOfStart(employeeCreateDTO.dateOfStart())
                .phoneNumber(employeeCreateDTO.phoneNumber())
                .city(employeeCreateDTO.city())
                .street(employeeCreateDTO.street())
                .zipCode(employeeCreateDTO.zipCode())
                .build();
    }
}
