package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.StoreProductDAO;
import com.vfrol.supermarket.dto.store_product.StoreProductCreateDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductListDTO;
import com.vfrol.supermarket.entity.StoreProduct;
import com.vfrol.supermarket.filter.StoreProductFilter;
import com.vfrol.supermarket.service.validator.StoreProductValidator;
import com.vfrol.supermarket.service.validator.ValidationException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class StoreProductService {

    private final StoreProductDAO storeProductDAO;
    private final StoreProductValidator storeProductValidator;

    @Inject
    public StoreProductService(StoreProductDAO storeProductDAO, StoreProductValidator storeProductValidator) {
        this.storeProductDAO = storeProductDAO;
        this.storeProductValidator = storeProductValidator;
    }

    public void addStoreProduct(StoreProductCreateDTO dto) {
        storeProductValidator.validateForCreate(dto);
        double sellingPrice = calculateSellingPrice(dto);
        storeProductDAO.create(buildEntity(dto, sellingPrice));
    }

    public void updateStoreProduct(StoreProductCreateDTO dto) {
        storeProductValidator.validateForUpdate(dto);
        double sellingPrice = calculateSellingPrice(dto);
        storeProductDAO.update(buildEntity(dto, sellingPrice));
    }

    public void deleteStoreProduct(String upc) {
        storeProductValidator.validateForDelete(upc);
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

    public Set<String> getBlockedUPCs() {
        return new HashSet<>(storeProductDAO.findBlockedUPCs());
    }

    public Set<String> getBlockedUPCsExcluding(String upc) {
        Set<String> blocked = getBlockedUPCs();
        blocked.remove(upc);
        return blocked;
    }

    public Set<String> getOutOfStockUPCs() {
        return new HashSet<>(storeProductDAO.findOutOfStockUPCs());
    }

    private StoreProduct buildEntity(StoreProductCreateDTO dto, Double sellingPrice) {
        return StoreProduct.builder()
                .UPC(dto.UPC())
                .UPCprom(dto.UPCprom())
                .productId(dto.productId())
                .price(sellingPrice)
                .quantity(dto.quantity())
                .promotional(dto.promotional())
                .build();
    }

    private double calculateSellingPrice(StoreProductCreateDTO dto) {
        if (dto.promotional()) {
            return dto.price() * (1.0 - (dto.discount() / 100.0));
        }
        return dto.price();
    }
}