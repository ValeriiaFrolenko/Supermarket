package com.vfrol.supermarket.service.validator;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.config.SessionManager;
import com.vfrol.supermarket.dao.*;
import com.vfrol.supermarket.dto.check.CheckCreateDTO;
import com.vfrol.supermarket.dto.sale.SaleCreateDTO;

import java.util.List;

@Singleton
public class CheckValidator extends BaseValidator {

    private final CheckDAO checkDAO;
    private final SaleDAO saleDAO;
    private final StoreProductDAO storeProductDAO;
    private final EmployeeDAO employeeDAO;
    private final CustomerCardDAO customerCardDAO;
    private final SessionManager sessionManager;

    @Inject
    public CheckValidator(CheckDAO checkDAO,
                          SaleDAO saleDAO,
                          StoreProductDAO storeProductDAO,
                          EmployeeDAO employeeDAO,
                          SessionManager sessionManager,
                          CustomerCardDAO customerCardDAO) {
        this.checkDAO = checkDAO;
        this.saleDAO = saleDAO;
        this.storeProductDAO = storeProductDAO;
        this.employeeDAO = employeeDAO;
        this.sessionManager = sessionManager;
        this.customerCardDAO = customerCardDAO;
    }

    public void validateOnCreate(CheckCreateDTO dto){
        requireNotExists(checkDAO.findById(dto.checkNumber()), "Check with this number already exists");
        validateEmployee(dto);
        validateCustomer(dto);
        validateSales(dto);
    }

    public void validateOnDelete(String checkNumber) {
        requireExists(checkDAO.findById(checkNumber), "Check not found");
    }

    private void validateSales(CheckCreateDTO dto) {
        List<SaleCreateDTO> saleCreateDTOS = dto.sales();
        if (saleCreateDTOS == null || saleCreateDTOS.isEmpty()) {
            throw new ValidationException("Check must contain at least one sale");
        }
        for (SaleCreateDTO saleDTO : saleCreateDTOS) {
            requireExists(storeProductDAO.findById(saleDTO.UPC()), "Store product with UPC '" + saleDTO.UPC() + "' does not exist");
            if (saleDTO.quantity()>storeProductDAO.getQuantityByUPC(saleDTO.UPC())) {
                throw new ValidationException("Not enough quantity for product with UPC '" + saleDTO.UPC() + "' \n" +
                        "Available quantity: " + storeProductDAO.getQuantityByUPC(saleDTO.UPC()) + ", requested quantity: " + saleDTO.quantity());
            }
            if (!saleDTO.checkNumber().equals(dto.checkNumber())) {
                throw new ValidationException("Sale check number '" + saleDTO.checkNumber() + "' does not match the check number '" + dto.checkNumber() + "' in the request");
            }
            if (saleDAO.existsByCheckNumberAndUPC(saleDTO.checkNumber(), saleDTO.UPC())) {
                throw new ValidationException("Sale with check number '" + saleDTO.checkNumber() + "' and UPC '" + saleDTO.UPC() + "' already exists");
            }
        }
    }

    private void validateCustomer(CheckCreateDTO dto) {
        if (dto.cardNumber() != null) {
            requireExists(customerCardDAO.findById(dto.cardNumber()), "Customer card not found");
        }
    }

    private void validateEmployee(CheckCreateDTO dto) {
        requireExists(employeeDAO.findById(dto.idEmployee()), "Employee not found");
        if (sessionManager.isManager()){
            throw new ValidationException("Employee with MANAGER role cannot create checks");
        }
        if (!(dto.idEmployee()).equals(sessionManager.getCurrentUser().id())){
            throw new ValidationException("Employees can only create checks for themselves");
        }
    }
}
