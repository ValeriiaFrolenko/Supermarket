package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.ProductDAO;
import com.vfrol.supermarket.dao.SaleDAO;
import com.vfrol.supermarket.dao.StoreProductDAO;
import com.vfrol.supermarket.dto.store_product.StoreProductCreateDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductPromoInfoDTO;

@Singleton
public class StoreProductValidator {

    private final StoreProductDAO storeProductDAO;
    private final ProductDAO productDAO;
    private final SaleDAO saleDAO;

    @Inject
    public StoreProductValidator(StoreProductDAO storeProductDAO, ProductDAO productDAO, SaleDAO saleDAO) {
        this.storeProductDAO = storeProductDAO;
        this.productDAO = productDAO;
        this.saleDAO = saleDAO;
    }

    public void validateForCreate(StoreProductCreateDTO dto) {
        if (storeProductDAO.existsByUPC(dto.UPC())) {
            throw new ValidationException("Store product with UPC '" + dto.UPC() + "' already exists.");
        }
        if (!productDAO.existsById(dto.productId())) {
            throw new ValidationException("Product with ID '" + dto.productId() + "' does not exist.");
        }
        if (dto.promotional()) {
            validatePromotionalRules(dto);
        } else {
            validateNonPromotionalRules(dto);
        }
    }

    public void validateForUpdate(StoreProductCreateDTO dto) {
        if (!storeProductDAO.existsByUPC(dto.UPC())) {
            throw new ValidationException("Store product with UPC '" + dto.UPC() + "' does not exist.");
        }
        if (!productDAO.existsById(dto.productId())) {
            throw new ValidationException("Product with ID '" + dto.productId() + "' does not exist.");
        }
        if (dto.promotional()) {
            validatePromotionalRules(dto);
        } else {
            validateNonPromotionalRules(dto);
        }
    }

    public void validateForDelete(String upc) {
        StoreProductPromoInfoDTO info = storeProductDAO.findPromoInfo(upc)
                .orElseThrow(() -> new ValidationException("Store product with UPC '" + upc + "' does not exist."));

        if (saleDAO.existsByUPC(upc)) {
            throw new ValidationException(
                    "Cannot delete store product with UPC '" + upc + "' because it has been sold in existing sales."
            );
        }
        if (info.usedAsPromoBase()) {
            throw new ValidationException(
                    "Cannot delete store product with UPC '" + upc + "' because it is used as a promotional base for another product."
            );
        }
    }

    private void validatePromotionalRules(StoreProductCreateDTO dto) {
        if (dto.UPCprom() == null) {
            throw new ValidationException("Promotional product must have a promotional UPC.");
        }
        if (dto.UPC().equals(dto.UPCprom())) {
            throw new ValidationException("Regular and promotional UPC cannot be the same.");
        }

        StoreProductPromoInfoDTO promInfo = storeProductDAO.findPromoInfo(dto.UPCprom())
                .orElseThrow(() -> new ValidationException("Promotional UPC '" + dto.UPCprom() + "' does not exist."));

        if (!(dto.productId() == (promInfo.productId()))) {
            throw new ValidationException("Promotional UPC must be associated with the same product.");
        }
        if (promInfo.usedAsPromoBase()) {
            throw new ValidationException("Promotional UPC '" + dto.UPCprom() + "' cannot be used as a base for another promotion.");
        }
    }

    private void validateNonPromotionalRules(StoreProductCreateDTO dto) {
        if (dto.UPCprom() != null) {
            throw new ValidationException("Non-promotional product must not have a promotional UPC.");
        }
        if (storeProductDAO.existsByProductIdNotProm(dto.productId())) {
            throw new ValidationException("Only one non-promotional store product is allowed per product.");
        }
    }
}