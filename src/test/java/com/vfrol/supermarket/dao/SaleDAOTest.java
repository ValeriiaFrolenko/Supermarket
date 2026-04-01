package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.entity.*;
import com.vfrol.supermarket.enums.EmployeeRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SaleDAOTest extends BaseDAOTest {

    private SaleDAO saleDAO;

    @BeforeEach
    void setUp() {
        saleDAO = handle.attach(SaleDAO.class);

        handle.attach(EmployeeDAO.class).create(Employee.builder()
                .id("e1")
                .passwordHash("hash")
                .surname("S")
                .name("N")
                .patronymic("P")
                .role(EmployeeRole.CASHIER)
                .salary(100.0)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .dateOfStart(LocalDate.of(2020, 1, 1))
                .phoneNumber("+38000000000")
                .city("City")
                .street("Street")
                .zipCode("00000")
                .build());

        handle.attach(CheckDAO.class).create(Check.builder()
                .checkNumber("CH1")
                .idEmployee("e1")
                .dateTime(LocalDateTime.now())
                .sumTotal(0)
                .vat(0)
                .build());

        handle.attach(CategoryDAO.class).create(new Category(0, "Cat"));

        handle.attach(ProductDAO.class).create(Product.builder()
                .id(1)
                .categoryId(1)
                .name("P1")
                .characteristics("Char")
                .build());

        handle.attach(StoreProductDAO.class).create(StoreProduct.builder()
                .UPC("UPC1")
                .productId(1)
                .price(10.0)
                .quantity(50)
                .promotional(false)
                .build());
    }

    private Sale createTestSale(String upc, String checkNumber, int quantity, double price) {
        return Sale.builder()
                .UPC(upc)
                .checkNumber(checkNumber)
                .quantity(quantity)
                .price(price)
                .build();
    }

    @Test
    void create() {
        saleDAO.create(createTestSale("UPC1", "CH1", 2, 10.0));

        var sales = saleDAO.findByCheckNumber("CH1");
        assertEquals(1, sales.size());
        assertEquals("UPC1", sales.getFirst().UPC());
        assertEquals("P1", sales.getFirst().productName());
    }

    @Test
    void update() {
        saleDAO.create(createTestSale("UPC1", "CH1", 1, 10.0));
        saleDAO.update(createTestSale("UPC1", "CH1", 5, 10.0));

        var sales = saleDAO.findByCheckNumber("CH1");
        assertFalse(sales.isEmpty());
        assertEquals(5, sales.getFirst().quantity());
    }

    @Test
    void delete() {
        saleDAO.create(createTestSale("UPC1", "CH1", 1, 10.0));
        saleDAO.delete("UPC1", "CH1");

        assertTrue(saleDAO.findByCheckNumber("CH1").isEmpty());
    }

    @Test
    void findByUPC() {
        saleDAO.create(createTestSale("UPC1", "CH1", 2, 10.0));

        var sales = saleDAO.findByUPC("UPC1");
        assertEquals(1, sales.size());
        assertEquals("UPC1", sales.getFirst().UPC());
    }

    @Test
    void deleteByCheckNumber() {
        saleDAO.create(createTestSale("UPC1", "CH1", 1, 10.0));
        saleDAO.deleteByCheckNumber("CH1");

        assertTrue(saleDAO.findByCheckNumber("CH1").isEmpty());
    }
}