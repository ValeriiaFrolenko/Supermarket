package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.entity.Category;
import com.vfrol.supermarket.entity.Product;
import com.vfrol.supermarket.filter.ProductFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest extends BaseDAOTest {

    private ProductDAO productDAO;

    @BeforeEach
    void setUp() {
        productDAO = handle.attach(ProductDAO.class);
        handle.attach(CategoryDAO.class).create(new Category(0, "Test category"));
    }

    private Product createTestProduct(String name) {
        return Product.builder()
                .id(1)
                .categoryId(1)
                .name(name)
                .characteristics("Test Characteristics")
                .build();
    }

    @Test
    void create() {
        productDAO.create(createTestProduct("Test Product"));

        var retrieved = productDAO.findById(1);
        assertTrue(retrieved.isPresent());
        assertEquals("Test Product", retrieved.get().name());
    }

    @Test
    void update() {
        productDAO.create(createTestProduct("Test Product"));
        productDAO.update(createTestProduct("Updated Product"));

        var retrieved = productDAO.findById(1);
        assertTrue(retrieved.isPresent());
        assertEquals("Updated Product", retrieved.get().name());
    }

    @Test
    void delete() {
        productDAO.create(createTestProduct("Test Product"));
        productDAO.delete(1);

        assertFalse(productDAO.findById(1).isPresent());
    }

    @Test
    void findById() {
        productDAO.create(createTestProduct("Test Product"));

        var retrieved = productDAO.findById(1);
        assertTrue(retrieved.isPresent());
        assertEquals("Test Product", retrieved.get().name());
    }

    @Test
    void findById_notFound() {
        assertFalse(productDAO.findById(999).isPresent());
    }

    @Test
    void findAll() {
        productDAO.create(createTestProduct("Test Product1"));
        productDAO.create(createTestProduct("Test Product2"));

        var products = productDAO.findAll();
        assertEquals(2, products.size());
        assertEquals("Test Product1", products.get(0).name());
        assertEquals("Test Product2", products.get(1).name());
    }

    @Test
    void findByFilter_byName_returnsMatchingProducts() {
        productDAO.create(createTestProduct("Test Product"));

        var result = productDAO.findByFilter(ProductFilter.builder().name("Test").build());
        assertEquals(1, result.size());
        assertEquals("Test Product", result.getFirst().name());
    }

    @Test
    void findByFilter_byName_noMatch_returnsEmpty() {
        productDAO.create(createTestProduct("Test Product"));

        var result = productDAO.findByFilter(ProductFilter.builder().name("Non-existing").build());
        assertTrue(result.isEmpty());
    }

    @Test
    void findByFilter_byCategoryId_returnsMatchingProducts() {
        productDAO.create(createTestProduct("Test Product"));

        var result = productDAO.findByFilter(ProductFilter.builder().categoryId(1).build());
        assertEquals(1, result.size());
    }

    @Test
    void findByFilter_emptyFilter_returnsAll() {
        productDAO.create(createTestProduct("Test Product 1"));
        productDAO.create(createTestProduct("Test Product 2"));

        var result = productDAO.findByFilter(ProductFilter.builder().build());
        assertEquals(2, result.size());
    }
}