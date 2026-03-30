package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDAOTest extends BaseDAOTest {

    private CategoryDAO categoryDAO;

    @BeforeEach
    void setUp() throws IOException {
        categoryDAO = handle.attach(CategoryDAO.class);
    }

    @Test
    void create() {
        Category category = new Category(0,"Beverages");
        categoryDAO.create(category);
        Optional<CategoryListDTO> retrieved = categoryDAO.findById(1);
        assertTrue(retrieved.isPresent());
        assertEquals("Beverages", retrieved.get().name());
    }

    @Test
    void update() {
            Category category = new Category(0,"Beverages");
            categoryDAO.create(category);
            Optional<CategoryListDTO> retrieved = categoryDAO.findById(1);
            assertTrue(retrieved.isPresent());
            assertEquals("Beverages", retrieved.get().name());

            Category updatedCategory = new Category(1,"Soft Drinks");
            categoryDAO.update(updatedCategory);
            Optional<CategoryListDTO> updatedRetrieved = categoryDAO.findById(1);
            assertTrue(updatedRetrieved.isPresent());
            assertEquals("Soft Drinks", updatedRetrieved.get().name());
    }

    @Test
    void delete() {
        Category category = new Category(0,"Beverages");
        categoryDAO.create(category);
        Optional<CategoryListDTO> retrieved = categoryDAO.findById(1);
        assertTrue(retrieved.isPresent());
        assertEquals("Beverages", retrieved.get().name());

        categoryDAO.delete(1);
        Optional<CategoryListDTO> deletedRetrieved = categoryDAO.findById(1);
        assertFalse(deletedRetrieved.isPresent());
    }

    @Test
    void findById() {
        Category category = new Category(0,"Beverages");
        categoryDAO.create(category);
        Optional<CategoryListDTO> retrieved = categoryDAO.findById(1);
        assertTrue(retrieved.isPresent());
        assertEquals("Beverages", retrieved.get().name());
    }

    @Test
    void findAll() {
        Category category1 = new Category(0,"Beverages");
        Category category2 = new Category(0,"Snacks");
        categoryDAO.create(category1);
        categoryDAO.create(category2);
        var categories = categoryDAO.findAll();
        assertEquals(2, categories.size());
        assertEquals("Beverages", categories.get(0).name());
        assertEquals("Snacks", categories.get(1).name());
    }
}