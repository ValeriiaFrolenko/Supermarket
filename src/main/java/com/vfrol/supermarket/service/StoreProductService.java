package com.vfrol.supermarket.service;

import com.vfrol.supermarket.dao.StoreProductDAO;
import com.vfrol.supermarket.dto.store_product.StoreProductCreateDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductListDTO;
import com.vfrol.supermarket.entity.StoreProduct;
import com.vfrol.supermarket.filter.StoreProductFilter;

import java.util.List;

public class StoreProductService {
    private final StoreProductDAO storeProductDAO;

    public StoreProductService(StoreProductDAO storeProductDAO) {
        this.storeProductDAO = storeProductDAO;
    }

    public void addStoreProduct(StoreProductCreateDTO dto) {
        StoreProduct storeProduct = buildEntity(dto);
        storeProductDAO.create(storeProduct);
    }

    public void updateStoreProduct(StoreProductCreateDTO dto) {
        StoreProduct storeProduct = buildEntity(dto);
        storeProductDAO.update(storeProduct);
    }

    public void deleteStoreProduct(String upc) {
        storeProductDAO.delete(upc);
    }

    public StoreProductDetailsDTO getStoreProductByUpc(String upc) {
        return storeProductDAO.findById(upc).orElseThrow(() -> new RuntimeException("Store product not found"));
    }

    public List<StoreProductListDTO> getAllStoreProducts() {
        return storeProductDAO.findAll();
    }

    public List<StoreProductListDTO> getStoreProductsByFilter(StoreProductFilter filter) {
        return storeProductDAO.findByFilter(filter);
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

    public Integer getProductIdByUpc(String upcProm) {
        return storeProductDAO.findProductIdByUPC(upcProm);
    }
}
