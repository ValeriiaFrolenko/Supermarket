package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.StoreProductDAO;
import com.vfrol.supermarket.dto.store_product.StoreProductCreateDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductListDTO;
import com.vfrol.supermarket.entity.StoreProduct;
import com.vfrol.supermarket.filter.StoreProductFilter;

import java.util.List;

@Singleton
public class StoreProductService {

    private final StoreProductDAO storeProductDAO;

    @Inject
    public StoreProductService(StoreProductDAO storeProductDAO) {
        this.storeProductDAO = storeProductDAO;
    }

    public void addStoreProduct(StoreProductCreateDTO dto) {
        storeProductDAO.create(buildEntity(dto));
    }

    public void updateStoreProduct(StoreProductCreateDTO dto) {
        storeProductDAO.update(buildEntity(dto));
    }

    public void deleteStoreProduct(String upc) {
        storeProductDAO.delete(upc);
    }

    public StoreProductDetailsDTO getStoreProductByUpc(String upc) {
        return storeProductDAO.findById(upc)
                .orElseThrow(() -> new RuntimeException("Store product not found: " + upc));
    }

    public List<StoreProductListDTO> getAllStoreProducts() {
        return storeProductDAO.findAll();
    }

    public List<StoreProductDetailsDTO> getAllStoreProductDetails() {
        return storeProductDAO.findAllDetails();
    }

    public List<StoreProductListDTO> getStoreProductsByFilter(StoreProductFilter filter) {
        return storeProductDAO.findByFilter(filter);
    }

    public Integer getProductIdByUpc(String upc) {
        return storeProductDAO.findProductIdByUPC(upc);
    }

    private StoreProduct buildEntity(StoreProductCreateDTO dto) {
        return StoreProduct.builder()
                .UPC(dto.UPC())
                .UPCprom(dto.UPCprom())
                .productId(dto.productId())
                .price(dto.price())
                .quantity(dto.quantity())
                .promotional(dto.promotional())
                .build();
    }
}