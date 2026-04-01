package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDAOTest extends BaseDAOTest {

    private CategoryDAO categoryDAO;

    @BeforeEach
    void setUp() {
        categoryDAO = handle.attach(CategoryDAO.class);
    }

    @Test
    void create() {
        categoryDAO.create(new Category(0, "Beverages"));

        var retrieved = categoryDAO.findById(1);
        assertTrue(retrieved.isPresent());
        assertEquals("Beverages", retrieved.get().name());
    }

    @Test
    void update() {
        categoryDAO.create(new Category(0, "Beverages"));
        categoryDAO.update(new Category(1, "Soft Drinks"));

        var retrieved = categoryDAO.findById(1);
        assertTrue(retrieved.isPresent());
        assertEquals("Soft Drinks", retrieved.get().name());
    }

    @Test
    void delete() {
        categoryDAO.create(new Category(0, "Beverages"));
        categoryDAO.delete(1);

        assertFalse(categoryDAO.findById(1).isPresent());
    }

    @Test
    void findById() {
        categoryDAO.create(new Category(0, "Beverages"));

        var retrieved = categoryDAO.findById(1);
        assertTrue(retrieved.isPresent());
        assertEquals("Beverages", retrieved.get().name());
    }

    @Test
    void findById_notFound() {
        assertFalse(categoryDAO.findById(999).isPresent());
    }

    @Test
    void findAll() {
        categoryDAO.create(new Category(0, "Beverages"));
        categoryDAO.create(new Category(0, "Snacks"));

        var categories = categoryDAO.findAll();
        assertEquals(2, categories.size());
        assertEquals("Beverages", categories.get(0).name());
        assertEquals("Snacks", categories.get(1).name());
    }
}