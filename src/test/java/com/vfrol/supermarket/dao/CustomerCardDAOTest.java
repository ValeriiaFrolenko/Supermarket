package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.entity.CustomerCard;
import com.vfrol.supermarket.filter.CustomerCardFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerCardDAOTest extends BaseDAOTest {

    private CustomerCardDAO customerCardDAO;

    @BeforeEach
    void setUp() {
        customerCardDAO = handle.attach(CustomerCardDAO.class);
    }

    private CustomerCard createTestCard(String number, String surname, int discount) {
        return CustomerCard.builder()
                .cardNumber(number)
                .surname(surname)
                .name("Alex")
                .patronymic("Alexandrovich")
                .phoneNumber("+38000000000")
                .city("Kyiv")
                .street("Main St")
                .zipCode("00000")
                .discount(discount)
                .build();
    }

    @Test
    void create() {
        customerCardDAO.create(createTestCard("CARD001", "Petrov", 10));

        var retrieved = customerCardDAO.findById("CARD001");
        assertTrue(retrieved.isPresent());
        assertEquals("Petrov", retrieved.get().surname());
    }

    @Test
    void update() {
        customerCardDAO.create(createTestCard("CARD001", "Petrov", 10));
        customerCardDAO.update(createTestCard("CARD001", "Sidorov", 15));

        var retrieved = customerCardDAO.findById("CARD001");
        assertTrue(retrieved.isPresent());
        assertEquals("Sidorov", retrieved.get().surname());
        assertEquals(15, retrieved.get().discount());
    }

    @Test
    void delete() {
        customerCardDAO.create(createTestCard("CARD001", "Petrov", 10));
        customerCardDAO.delete("CARD001");

        assertFalse(customerCardDAO.findById("CARD001").isPresent());
    }

    @Test
    void findById() {
        customerCardDAO.create(createTestCard("CARD001", "Petrov", 10));

        var retrieved = customerCardDAO.findById("CARD001");
        assertTrue(retrieved.isPresent());
        assertEquals("Petrov", retrieved.get().surname());
    }

    @Test
    void findById_notFound() {
        assertFalse(customerCardDAO.findById("CARD001").isPresent());
    }

    @Test
    void findAll() {
        customerCardDAO.create(createTestCard("C1", "Petrov", 10));
        customerCardDAO.create(createTestCard("C2", "Sidorov", 20));

        var cards = customerCardDAO.findAll();
        assertEquals(2, cards.size());
    }

    @Test
    void findByFilter_byDiscountRange() {
        customerCardDAO.create(createTestCard("C1", "A", 5));
        customerCardDAO.create(createTestCard("C2", "B", 10));
        customerCardDAO.create(createTestCard("C3", "C", 20));

        var result = customerCardDAO.findByFilter(CustomerCardFilter.builder()
                .discountFrom(10)
                .discountTo(25)
                .build());

        assertEquals(2, result.size());
    }

    @Test
    void findByFilter_bySurnameAndDiscountRange_returnsMatchingCards() {
        customerCardDAO.create(createTestCard("C1", "Petrov", 10));
        customerCardDAO.create(createTestCard("C2", "Sidorov", 10));

        var result = customerCardDAO.findByFilter(CustomerCardFilter.builder()
                .surname("Petrov")
                .discountFrom(5)
                .discountTo(15)
                .build());

        assertEquals(1, result.size());
        assertEquals("C1", result.getFirst().cardNumber());
    }
}