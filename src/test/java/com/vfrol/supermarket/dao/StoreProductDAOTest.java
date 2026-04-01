package com.vfrol.supermarket.dao;

import com.vfrol.supermarket.entity.Category;
import com.vfrol.supermarket.entity.Product;
import com.vfrol.supermarket.entity.StoreProduct;
import com.vfrol.supermarket.filter.StoreProductFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoreProductDAOTest extends BaseDAOTest {

    private StoreProductDAO storeProductDAO;

    @BeforeEach
    void setUp() {
        storeProductDAO = handle.attach(StoreProductDAO.class);

        handle.attach(CategoryDAO.class).create(new Category(0, "Food"));

        handle.attach(ProductDAO.class).create(Product.builder()
                .id(1)
                .categoryId(1)
                .name("Milk")
                .characteristics("1L")
                .build());
    }

    private StoreProduct createTestStoreProduct(String upc, boolean promotional) {
        return StoreProduct.builder()
                .UPC(upc)
                .productId(1)
                .price(35.50)
                .quantity(100)
                .promotional(promotional)
                .build();
    }

    @Test
    void create() {
        storeProductDAO.create(createTestStoreProduct("UPC001", false));

        var retrieved = storeProductDAO.findById("UPC001");
        assertTrue(retrieved.isPresent());
        assertEquals("UPC001", retrieved.get().UPC());
    }

    @Test
    void update() {
        storeProductDAO.create(createTestStoreProduct("UPC001", false));

        storeProductDAO.update(StoreProduct.builder()
                .UPC("UPC001")
                .productId(1)
                .price(40.0)
                .quantity(150)
                .promotional(true)
                .build());

        var retrieved = storeProductDAO.findById("UPC001");
        assertTrue(retrieved.isPresent());
        assertEquals(40.0, retrieved.get().price());
        assertTrue(retrieved.get().promotional());
    }

    @Test
    void delete() {
        storeProductDAO.create(createTestStoreProduct("UPC001", false));
        storeProductDAO.delete("UPC001");

        assertFalse(storeProductDAO.findById("UPC001").isPresent());
    }

    @Test
    void findById() {
        storeProductDAO.create(createTestStoreProduct("UPC001", false));

        var retrieved = storeProductDAO.findById("UPC001");
        assertTrue(retrieved.isPresent());
        assertEquals("UPC001", retrieved.get().UPC());
    }

    @Test
    void findById_notFound() {
        assertFalse(storeProductDAO.findById("NON_EXISTING").isPresent());
    }

    @Test
    void findByFilter_promotionalOnly() {
        storeProductDAO.create(createTestStoreProduct("UPC1", true));
        storeProductDAO.create(createTestStoreProduct("UPC2", false));

        var result = storeProductDAO.findByFilter(StoreProductFilter.builder().promotional(true).build());
        assertEquals(1, result.size());
        assertTrue(result.getFirst().promotional());
    }
}