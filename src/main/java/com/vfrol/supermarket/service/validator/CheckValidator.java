package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.dao.*;
import com.vfrol.supermarket.dto.check.CheckCreateDTO;
import com.vfrol.supermarket.dto.sale.SaleCreateDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Singleton
public class CheckValidator {

    private final CheckDAO checkDAO;
    private final StoreProductDAO storeProductDAO;
    private final EmployeeDAO employeeDAO;
    private final CustomerCardDAO customerCardDAO;
    private final SessionManager sessionManager;

    @Inject
    public CheckValidator(CheckDAO checkDAO,
                          StoreProductDAO storeProductDAO,
                          EmployeeDAO employeeDAO,
                          SessionManager sessionManager,
                          CustomerCardDAO customerCardDAO) {
        this.checkDAO = checkDAO;
        this.storeProductDAO = storeProductDAO;
        this.employeeDAO = employeeDAO;
        this.sessionManager = sessionManager;
        this.customerCardDAO = customerCardDAO;
    }

    public void validateOnCreate(CheckCreateDTO dto) {
        if (checkDAO.existsByCheckNumber(dto.checkNumber())) {
            throw new ValidationException("Check with number '" + dto.checkNumber() + "' already exists");
        }
        validateEmployee(dto);
        validateCustomer(dto);
        validateSales(dto);
    }

    public void validateOnDelete(String checkNumber) {
        if (!checkDAO.existsByCheckNumber(checkNumber)) {
            throw new ValidationException("Check with number '" + checkNumber + "' does not exist");
        }
    }

    private void validateSales(CheckCreateDTO dto) {
        List<SaleCreateDTO> saleCreateDTOS = dto.sales();
        if (saleCreateDTOS == null || saleCreateDTOS.isEmpty()) {
            throw new ValidationException("Check must contain at least one sale");
        }
        Set<String> seenUPCs = new HashSet<>();
        for (SaleCreateDTO saleDTO : saleCreateDTOS) {
            if (!seenUPCs.add(saleDTO.UPC())) {
                throw new ValidationException("Duplicate UPC '" + saleDTO.UPC() + "' in the same check");
            }

            int availableQty = storeProductDAO.getQuantityByUPC(saleDTO.UPC())
                    .orElseThrow(() -> new ValidationException("Store product with UPC '" + saleDTO.UPC() + "' does not exist"));

            if (saleDTO.quantity() > availableQty) {
                throw new ValidationException("Not enough quantity for product with UPC '" + saleDTO.UPC() + "' \n" +
                        "Available quantity: " + availableQty + ", requested quantity: " + saleDTO.quantity());
            }

            if (!saleDTO.checkNumber().equals(dto.checkNumber())) {
                throw new ValidationException("Sale check number '" + saleDTO.checkNumber() + "' does not match the check number '" + dto.checkNumber() + "' in the request");
            }
        }
    }

    private void validateCustomer(CheckCreateDTO dto) {
        if (dto.cardNumber() != null) {
            if (!customerCardDAO.existsByCardNumber(dto.cardNumber())) {
                throw new ValidationException("Customer card with number '" + dto.cardNumber() + "' does not exist");
            }
        }
    }

    private void validateEmployee(CheckCreateDTO dto) {
        if (!employeeDAO.existsById(dto.idEmployee())) {
            throw new ValidationException("Employee with id '" + dto.idEmployee() + "' does not exist");
        }
        if (sessionManager.isManager()) {
            throw new ValidationException("Employee with MANAGER role cannot create checks");
        }
        if (!dto.idEmployee().equals(sessionManager.getCurrentUser().id())) {
            throw new ValidationException("Employees can only create checks for themselves");
        }
    }
}