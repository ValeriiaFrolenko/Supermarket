package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.StoreProductDAO;
import com.vfrol.supermarket.dto.store_product.StoreProductCreateDTO;
import com.vfrol.supermarket.service.ProductService;
import com.vfrol.supermarket.service.SaleService;

@Singleton
public class StoreProductValidator extends BaseValidator {

    private final StoreProductDAO storeProductDAO;
    private final ProductService productService;
    private final SaleService saleService;

    @Inject
    public StoreProductValidator(StoreProductDAO storeProductDAO, ProductService productService, SaleService saleService) {
        this.storeProductDAO = storeProductDAO;
        this.productService = productService;
        this.saleService = saleService;
    }

    public void validateForCreate(StoreProductCreateDTO dto) {
        requireNotExists(
                storeProductDAO.findById(dto.UPC()),
                "Store product with UPC '" + dto.UPC() + "' already exists."
        );

        productService.getProductById(dto.productId());

        if (dto.promotional()) {
            validatePromotionalRules(dto);
        }
    }

    public void validateForUpdate(StoreProductCreateDTO dto) {
        requireExists(
                storeProductDAO.findById(dto.UPC()),
                "Store product with UPC '" + dto.UPC() + "' does not exist."
        );

        productService.getProductById(dto.productId());

        if (dto.promotional()) {
            validatePromotionalRules(dto);
        }
    }

    public void validateForDelete(String upc) {
        requireExists(
                storeProductDAO.findById(upc),
                "Store product with UPC '" + upc + "' does not exist."
        );

        if (saleService.existsByUPC(upc)) {
            throw new ValidationException(
                    "Cannot delete store product with UPC '" + upc + "' because it has been sold in existing sales."
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