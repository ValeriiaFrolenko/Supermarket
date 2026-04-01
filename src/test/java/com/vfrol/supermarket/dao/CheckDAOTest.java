package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.entity.Check;
import com.vfrol.supermarket.entity.Employee;
import com.vfrol.supermarket.enums.EmployeeRole;
import com.vfrol.supermarket.filter.CheckFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CheckDAOTest extends BaseDAOTest {

    private CheckDAO checkDAO;

    @BeforeEach
    void setUp() {
        checkDAO = handle.attach(CheckDAO.class);

        handle.attach(EmployeeDAO.class).create(Employee.builder()
                .id("cashier1")
                .passwordHash("hash")
                .surname("Ivanov")
                .name("Ivan")
                .patronymic("Ivanovich")
                .role(EmployeeRole.CASHIER)
                .salary(20000.0)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .dateOfStart(LocalDate.of(2020, 1, 1))
                .phoneNumber("+380991234567")
                .city("Kyiv")
                .street("Khreshchatyk")
                .zipCode("01001")
                .build());
    }

    private Check createTestCheck(String number) {
        return Check.builder()
                .checkNumber(number)
                .idEmployee("cashier1")
                .dateTime(LocalDateTime.now())
                .sumTotal(100.0)
                .vat(20.0)
                .build();
    }

    @Test
    void create() {
        checkDAO.create(createTestCheck("000001"));

        var retrieved = checkDAO.findById("000001");
        assertTrue(retrieved.isPresent());
        assertEquals("000001", retrieved.get().checkNumber());
    }

    @Test
    void update() {
        checkDAO.create(createTestCheck("000001"));

        Check updated = Check.builder()
                .checkNumber("000001")
                .idEmployee("cashier1")
                .dateTime(LocalDateTime.now())
                .sumTotal(500.0)
                .vat(20.0)
                .build();
        checkDAO.update(updated);

        var retrieved = checkDAO.findById("000001");
        assertTrue(retrieved.isPresent());
        assertEquals(500.0, retrieved.get().sumTotal());
    }

    @Test
    void delete() {
        checkDAO.create(createTestCheck("000001"));
        checkDAO.delete("000001");

        assertFalse(checkDAO.findById("000001").isPresent());
    }

    @Test
    void findById_notFound() {
        assertFalse(checkDAO.findById("000001").isPresent());
    }

    @Test
    void findAll() {
        checkDAO.create(createTestCheck("000001"));
        checkDAO.create(createTestCheck("000002"));

        var checks = checkDAO.findAll();
        assertEquals(2, checks.size());
    }

    @Test
    void findByFilter_byCheckNumber_returnsMatch() {
        checkDAO.create(createTestCheck("123456"));
        checkDAO.create(createTestCheck("789000"));

        var result = checkDAO.findByFilter(CheckFilter.builder().checkNumber("123").build());
        assertEquals(1, result.size());
        assertEquals("123456", result.getFirst().checkNumber());
    }

    @Test
    void findByFilter_byCheckNumberAndCashierSurname_returnsMatch() {
        checkDAO.create(createTestCheck("123456"));
        checkDAO.create(createTestCheck("123789"));

        var result = checkDAO.findByFilter(CheckFilter.builder()
                .checkNumber("123")
                .cashierSurname("Ivanov")
                .build());

        assertEquals(2, result.size());
    }
}