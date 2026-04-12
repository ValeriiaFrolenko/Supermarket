package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.ProductDAO;
import com.vfrol.supermarket.dao.SaleDAO;
import com.vfrol.supermarket.dao.StoreProductDAO;
import com.vfrol.supermarket.dto.store_product.StoreProductCreateDTO;

@Singleton
public class StoreProductValidator extends BaseValidator {

    private final StoreProductDAO storeProductDAO;
    private final ProductDAO productDAO;
    private final SaleDAO saleDAO;

    @Inject
    public StoreProductValidator(StoreProductDAO storeProductDAO, ProductDAO productDAO, SaleDAO saleDAO) { // Змінено тут
        this.storeProductDAO = storeProductDAO;
        this.productDAO = productDAO;
        this.saleDAO = saleDAO;
    }

    public void validateForCreate(StoreProductCreateDTO dto) {
        requireNotExists(
                storeProductDAO.findById(dto.UPC()),
                "Store product with UPC '" + dto.UPC() + "' already exists."
        );

        requireExists(productDAO.findById(dto.productId()), "Product does not exist.");

        if (dto.promotional()) {
            validatePromotionalRules(dto);
        }
    }

    public void validateForUpdate(StoreProductCreateDTO dto) {
        requireExists(
                storeProductDAO.findById(dto.UPC()),
                "Store product with UPC '" + dto.UPC() + "' does not exist."
        );

        requireExists(productDAO.findById(dto.productId()), "Product does not exist.");

        if (dto.promotional()) {
            validatePromotionalRules(dto);
        }
    }

    public void validateForDelete(String upc) {
        requireExists(
                storeProductDAO.findById(upc),
                "Store product with UPC '" + upc + "' does not exist."
        );

        if (saleDAO.existsByUPC(upc)) {
            throw new ValidationException(
                    "Cannot delete store product with UPC '" + upc + "' because it has been sold in existing sales."
            );
        }

        if (storeProductDAO.isUsedAsPromoBase(upc)) {
            throw new ValidationException(
                    "Cannot delete store product with UPC '" + upc + "' because it is used as a promotional base for another product."
            );
        }
    }

    private void validatePromotionalRules(StoreProductCreateDTO dto) {
        requireExists(
                storeProductDAO.findById(dto.UPCprom()),
                "Promotional UPC '" + dto.UPCprom() + "' does not exist."
        );
        if (dto.UPC().equals(dto.UPCprom())) {
            throw new ValidationException("Regular and promotional UPC cannot be the same.");
        }
        if (dto.productId() != storeProductDAO.findProductIdByUPC(dto.UPCprom())) {
            throw new ValidationException("Promotional UPC must be associated with the same product.");
        }
    }
}