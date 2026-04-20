package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.dao.*;
import com.vfrol.supermarket.dto.check.CheckCreateDTO;
import com.vfrol.supermarket.dto.sale.SaleCreateDTO;
import com.vfrol.supermarket.exception.ValidationException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class CheckValidator {

    private final StoreProductDAO storeProductDAO;
    private final SessionManager sessionManager;

    @Inject
    public CheckValidator(StoreProductDAO storeProductDAO, SessionManager sessionManager) {
        this.storeProductDAO = storeProductDAO;
        this.sessionManager = sessionManager;
    }

    public void validateOnCreate(CheckCreateDTO dto) {
        if (sessionManager.isManager())
            throw new ValidationException("Employee with MANAGER role cannot create checks");
        if (!dto.idEmployee().equals(sessionManager.getCurrentUser().id()))
            throw new ValidationException("Employees can only create checks for themselves");
        validateSales(dto);
    }

    private void validateSales(CheckCreateDTO dto) {
        List<SaleCreateDTO> sales = dto.sales();
        if (sales == null || sales.isEmpty())
            throw new ValidationException("Check must contain at least one sale");
        Set<String> seenUPCs = new HashSet<>();
        for (SaleCreateDTO saleDTO : sales) {
            if (!seenUPCs.add(saleDTO.UPC()))
                throw new ValidationException("Duplicate UPC '" + saleDTO.UPC() + "' in the same check");
            int availableQty = storeProductDAO.getQuantityByUPC(saleDTO.UPC());
            if (saleDTO.quantity() > availableQty)
                throw new ValidationException("Not enough quantity for product with UPC '" + saleDTO.UPC() + "' \n" +
                        "Available quantity: " + availableQty + ", requested quantity: " + saleDTO.quantity());
            if (!saleDTO.checkNumber().equals(dto.checkNumber()))
                throw new ValidationException("Sale check number '" + saleDTO.checkNumber() + "' does not match check number '" + dto.checkNumber() + "'");
        }
    }
}