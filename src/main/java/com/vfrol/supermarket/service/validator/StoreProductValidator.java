package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.StoreProductDAO;
import com.vfrol.supermarket.dto.store_product.StoreProductCreateDTO;
import com.vfrol.supermarket.dto.store_product.StoreProductPromoInfoDTO;
import com.vfrol.supermarket.exception.ValidationException;

@Singleton
public class StoreProductValidator {

    private final StoreProductDAO storeProductDAO;

    @Inject
    public StoreProductValidator(StoreProductDAO storeProductDAO) {
        this.storeProductDAO = storeProductDAO;
    }

    public void validateForCreate(StoreProductCreateDTO dto) {
        if (dto.promotional()) {
            validatePromotionalRules(dto);
        } else {
            validateNonPromotionalRules(dto);
        }
    }

    public void validateForUpdate(StoreProductCreateDTO dto) {
        if (dto.promotional()) {
            validatePromotionalRules(dto);
        } else {
            validateNonPromotionalRules(dto);
        }
    }

    public void validateForDelete(String upc) {
        storeProductDAO.findPromoInfo(upc)
                .filter(StoreProductPromoInfoDTO::usedAsPromoBase)
                .ifPresent(_ -> {
                    throw new ValidationException(
                            "Cannot delete store product with UPC '" + upc + "' because it is used as a promotional base for another product.");
                });
    }

    private void validatePromotionalRules(StoreProductCreateDTO dto) {
        if (dto.UPCprom() == null)
            throw new ValidationException("Promotional product must have a promotional UPC.");
        if (dto.UPC().equals(dto.UPCprom()))
            throw new ValidationException("Regular and promotional UPC cannot be the same.");
        StoreProductPromoInfoDTO promInfo = storeProductDAO.findPromoInfo(dto.UPCprom())
                .orElseThrow(() -> new ValidationException("Promotional UPC '" + dto.UPCprom() + "' does not exist."));
        if (dto.productId() != promInfo.productId())
            throw new ValidationException("Promotional UPC must be associated with the same product.");
        if (promInfo.usedAsPromoBase())
            throw new ValidationException("Promotional UPC '" + dto.UPCprom() + "' cannot be used as a base for another promotion.");
    }

    private void validateNonPromotionalRules(StoreProductCreateDTO dto) {
        if (dto.UPCprom() != null)
            throw new ValidationException("Non-promotional product must not have a promotional UPC.");
        if (storeProductDAO.existsByProductIdNotProm(dto.productId()))
            throw new ValidationException("Only one non-promotional store product is allowed per product.");
    }
}