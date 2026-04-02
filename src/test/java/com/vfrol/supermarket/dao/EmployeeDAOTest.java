package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.entity.Employee;
import com.vfrol.supermarket.enums.EmployeeRole;
import com.vfrol.supermarket.filter.EmployeeFilter;
import com.vfrol.supermarket.tools.PasswordManager;
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
                .passwordHash(PasswordManager.generatePassword("12345"))
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
    void getPasswordById(){
        employeeDAO.create(createTestEmployee("test_id", "Doe", EmployeeRole.CASHIER));

        var passwordHash = employeeDAO.getPasswordById("test_id");
        assertTrue(passwordHash.isPresent());
        assertTrue(PasswordManager.verifyPassword("12345", passwordHash.get()));
    }

    @Test
    void create() {
        employeeDAO.create(createTestEmployee("test_id", "Doe", EmployeeRole.CASHIER));

        var retrieved = employeeDAO.findById("test_id");
        assertTrue(retrieved.isPresent());
        assertEquals("test_id", retrieved.get().id());
    }

    @Test
    void update() {
        employeeDAO.create(createTestEmployee("test_id", "Doe", EmployeeRole.CASHIER));
        employeeDAO.update(createTestEmployee("test_id", "Doe", EmployeeRole.MANAGER));

        var retrieved = employeeDAO.findById("test_id");
        assertTrue(retrieved.isPresent());
        assertEquals(EmployeeRole.MANAGER, retrieved.get().role());
    }

    @Test
    void delete() {
        employeeDAO.create(createTestEmployee("test_id", "Doe", EmployeeRole.CASHIER));
        employeeDAO.delete("test_id");

        assertFalse(employeeDAO.findById("test_id").isPresent());
    }

    @Test
    void findById() {
        employeeDAO.create(createTestEmployee("test_id", "Doe", EmployeeRole.CASHIER));

        var retrieved = employeeDAO.findById("test_id");
        assertTrue(retrieved.isPresent());
        assertEquals("test_id", retrieved.get().id());
    }

    @Test
    void findById_notFound() {
        assertFalse(employeeDAO.findById("non_existing").isPresent());
    }

    @Test
    void findAll() {
        employeeDAO.create(createTestEmployee("test_id1", "Doe", EmployeeRole.CASHIER));
        employeeDAO.create(createTestEmployee("test_id2", "Smith", EmployeeRole.MANAGER));

        var employees = employeeDAO.findAll();
        assertEquals(2, employees.size());
    }

    @Test
    void findByFilter_bySurname_returnsMatchingEmployees() {
        employeeDAO.create(createTestEmployee("test_id1", "Doe", EmployeeRole.CASHIER));
        employeeDAO.create(createTestEmployee("test_id2", "Smith", EmployeeRole.MANAGER));

        var result = employeeDAO.findByFilter(EmployeeFilter.builder().surname("Doe").build());
        assertEquals(1, result.size());
        assertEquals("test_id1", result.getFirst().id());
    }

    @Test
    void findByFilter_bySurname_noMatch_returnsEmpty() {
        employeeDAO.create(createTestEmployee("test_id1", "Doe", EmployeeRole.CASHIER));

        var result = employeeDAO.findByFilter(EmployeeFilter.builder().surname("Non-existing").build());
        assertTrue(result.isEmpty());
    }

    @Test
    void findByFilter_byRole_returnsMatchingEmployees() {
        employeeDAO.create(createTestEmployee("test_id1", "Doe", EmployeeRole.CASHIER));
        employeeDAO.create(createTestEmployee("test_id2", "Smith", EmployeeRole.MANAGER));

        var result = employeeDAO.findByFilter(EmployeeFilter.builder().role(EmployeeRole.MANAGER).build());
        assertEquals(1, result.size());
        assertEquals("test_id2", result.getFirst().id());
    }

    @Test
    void findByFilter_emptyFilter_returnsAll() {
        employeeDAO.create(createTestEmployee("test_id1", "Doe", EmployeeRole.CASHIER));
        employeeDAO.create(createTestEmployee("test_id2", "Smith", EmployeeRole.MANAGER));

        var result = employeeDAO.findByFilter(EmployeeFilter.builder().build());
        assertEquals(2, result.size());
    }

    @Test
    void findByFilter_bySurnameAndRole_returnsMatchingEmployees() {
        employeeDAO.create(createTestEmployee("test_id1", "Doe", EmployeeRole.CASHIER));
        employeeDAO.create(createTestEmployee("test_id2", "Doe", EmployeeRole.MANAGER));

        var result = employeeDAO.findByFilter(EmployeeFilter.builder().surname("Doe").role(EmployeeRole.CASHIER).build());
        assertEquals(1, result.size());
        assertEquals("test_id1", result.getFirst().id());
    }
}