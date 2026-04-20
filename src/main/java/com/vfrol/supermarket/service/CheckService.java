package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.CheckDAO;
import com.vfrol.supermarket.dao.CustomerCardDAO;
import com.vfrol.supermarket.dao.SaleDAO;
import com.vfrol.supermarket.config.TransactionManager;
import com.vfrol.supermarket.dao.StoreProductDAO;
import com.vfrol.supermarket.dto.check.CheckCreateDTO;
import com.vfrol.supermarket.dto.check.CheckDetailsDTO;
import com.vfrol.supermarket.dto.check.CheckListDTO;
import com.vfrol.supermarket.dto.sale.SaleCreateDTO;
import com.vfrol.supermarket.entity.Check;
import com.vfrol.supermarket.entity.Sale;
import com.vfrol.supermarket.filter.CheckFilter;
import com.vfrol.supermarket.service.validator.CheckValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class CheckService {

    private final TransactionManager transactionManager;
    private final CheckDAO checkDAO;
    private final CheckValidator checkValidator;

    @Inject
    public CheckService(TransactionManager transactionManager, CheckDAO checkDAO, CheckValidator checkValidator) {
        this.transactionManager = transactionManager;
        this.checkDAO = checkDAO;
        this.checkValidator = checkValidator;
    }

    public void createCheck(CheckCreateDTO dto) {
        checkValidator.validateOnCreate(dto);
        transactionManager.useTransaction(handle -> {
            CheckDAO txCheckDAO = handle.attach(CheckDAO.class);
            SaleDAO txSaleDAO = handle.attach(SaleDAO.class);
            StoreProductDAO txStoreProductDAO = handle.attach(StoreProductDAO.class);
            List<SaleCreateDTO> sales = dto.sales();

            double sumTotal = 0.0;
            Map<String, Double> actualPrices = new HashMap<>();

            for (SaleCreateDTO saleDTO : sales) {
                double actualPrice = txStoreProductDAO.findPriceByUPC(saleDTO.UPC())
                        .orElseThrow(() -> new RuntimeException("Store product not found: " + saleDTO.UPC()));
                actualPrices.put(saleDTO.UPC(), actualPrice);
                sumTotal += actualPrice * saleDTO.quantity();
            }

            if (dto.cardNumber() != null && !dto.cardNumber().isBlank()) {
                CustomerCardDAO cardDAO = handle.attach(CustomerCardDAO.class);
                var cardOpt = cardDAO.findById(dto.cardNumber());
                if (cardOpt.isPresent() && dto.useDiscount()) {
                    double discountPercent = cardOpt.get().discount();
                    sumTotal = sumTotal * (1.0 - (discountPercent / 100.0));
                }
            }

            double vat = sumTotal * 0.20;

            Check check = Check.builder()
                    .checkNumber(dto.checkNumber())
                    .idEmployee(dto.idEmployee())
                    .cardNumber(dto.cardNumber())
                    .sumTotal(sumTotal)
                    .vat(vat)
                    .build();

            txCheckDAO.create(check);
            for (SaleCreateDTO saleDTO : sales) {
                createSale(txSaleDAO, txStoreProductDAO, saleDTO, actualPrices.get(saleDTO.UPC()));
            }
        });
    }

    private void createSale(SaleDAO saleDAO, StoreProductDAO storeProductDAO, SaleCreateDTO saleDTO, double actualPrice) {
        Sale sale = Sale.builder()
                .UPC(saleDTO.UPC())
                .checkNumber(saleDTO.checkNumber())
                .quantity(saleDTO.quantity())
                .price(actualPrice)
                .build();
        saleDAO.create(sale);
        storeProductDAO.sellStoreProduct(saleDTO.UPC(), saleDTO.quantity());
    }

    public void deleteCheckByNumber(String checkNumber) {
        transactionManager.useTransaction(handle -> {
            SaleDAO txSaleDAO = handle.attach(SaleDAO.class);
            CheckDAO txCheckDAO = handle.attach(CheckDAO.class);
            txSaleDAO.deleteByCheckNumber(checkNumber);
            txCheckDAO.delete(checkNumber);
        });
    }

    public CheckDetailsDTO getCheckById(String checkNumber) {
        return checkDAO.findById(checkNumber).orElseThrow(() -> new RuntimeException("Check not found"));
    }

    public List<CheckListDTO> getAllChecks() {
        return checkDAO.findAll();
    }

    public List<CheckDetailsDTO> getAllCheckDetails() {
        return checkDAO.findAllDetails();
    }

    public List<CheckListDTO> getCheckByFilter(CheckFilter filter) {
        return checkDAO.findByFilter(filter);
    }
}