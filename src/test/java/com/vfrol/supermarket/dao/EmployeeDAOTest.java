package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.filter.EmployeeFilter;
import com.vfrol.supermarket.tools.PasswordGenerator;
import com.vfrol.supermarket.entity.Employee;
import com.vfrol.supermarket.enums.EmployeeRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeDAOTest extends BaseDAOTest {

    private EmployeeDAO employeeDAO;

    @BeforeEach
    void setUp() {
        employeeDAO = handle.attach(EmployeeDAO.class);
    }

    private Employee createTestEmployee(String id, String surname, EmployeeRole role) {
        return Employee.builder()
                .id(id)
                .passwordHash(PasswordGenerator.generatePassword("12345"))
                .surname(surname)
                .name("John")
                .patronymic("Smith")
                .role(role)
                .salary(30000)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .dateOfStart(LocalDate.of(2020, 1, 1))
                .phoneNumber("+1234567890")
                .city("City")
                .street("Street")
                .zipCode("12345")
                .build();
    }

    @Test
    void create() {
        Employee employee = createTestEmployee("test_id", "Doe", EmployeeRole.CASHIER);
        employeeDAO.create(employee);

        var retrievedEmployee = employeeDAO.findById("test_id");
        assertTrue(retrievedEmployee.isPresent());
        assertEquals(employee.id(), retrievedEmployee.get().id());
    }

    @Test
    void update() {
        Employee employee = createTestEmployee("test_id", "Doe", EmployeeRole.CASHIER);
        employeeDAO.create(employee);

        Employee updatedEmployee = createTestEmployee("test_id", "Doe", EmployeeRole.MANAGER);
        employeeDAO.update(updatedEmployee);

        var retrievedEmployee = employeeDAO.findById("test_id");
        assertTrue(retrievedEmployee.isPresent());
        assertEquals(EmployeeRole.MANAGER, retrievedEmployee.get().role());
    }

    @Test
    void delete() {
        Employee employee = createTestEmployee("test_id", "Doe", EmployeeRole.CASHIER);
        employeeDAO.create(employee);

        employeeDAO.delete("test_id");
        var retrievedEmployee = employeeDAO.findById("test_id");
        assertFalse(retrievedEmployee.isPresent());
    }

    @Test
    void findById() {
        Employee employee = createTestEmployee("test_id", "Doe", EmployeeRole.CASHIER);
        employeeDAO.create(employee);

        var retrievedEmployee = employeeDAO.findById("test_id");
        assertTrue(retrievedEmployee.isPresent());
        assertEquals(employee.id(), retrievedEmployee.get().id());
    }

    @Test
    void findAll() {
        employeeDAO.create(createTestEmployee("test_id1", "Doe", EmployeeRole.CASHIER));
        employeeDAO.create(createTestEmployee("test_id2", "Smith", EmployeeRole.MANAGER));

        var employees = employeeDAO.findAll();
        assertTrue(employees.stream().anyMatch(e -> e.id().equals("test_id1")));
        assertTrue(employees.stream().anyMatch(e -> e.id().equals("test_id2")));
    }

    @Test
    void findByFilter() {
        employeeDAO.create(createTestEmployee("test_id1", "Doe", EmployeeRole.CASHIER));
        employeeDAO.create(createTestEmployee("test_id2", "Doe", EmployeeRole.MANAGER));

        EmployeeFilter filter = EmployeeFilter.builder()
                .surname("Doe")
                .build();

        var employees = employeeDAO.findByFilter(filter);
        assertEquals(2, employees.size(), "Має знайти двох працівників з прізвищем Doe");
        assertTrue(employees.stream().anyMatch(e -> e.id().equals("test_id1")));
        assertTrue(employees.stream().anyMatch(e -> e.id().equals("test_id2")));
    }
}