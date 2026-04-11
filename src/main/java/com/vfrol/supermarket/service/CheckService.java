package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vfrol.supermarket.dao.CheckDAO;
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

import java.util.List;

@Singleton
public class CheckService {

    private final TransactionManager transactionManager;
    private final CheckDAO checkDAO;

    @Inject
    public CheckService(TransactionManager transactionManager, CheckDAO checkDAO) {
        this.transactionManager = transactionManager;
        this.checkDAO = checkDAO;
    }

    public void createCheck(CheckCreateDTO dto) {
        transactionManager.useTransaction(handle -> {
            CheckDAO checkDAO = handle.attach(CheckDAO.class);
            SaleDAO saleDAO = handle.attach(SaleDAO.class);
            StoreProductDAO storeProductDAO = handle.attach(StoreProductDAO.class);
            List<SaleCreateDTO> sales = dto.sales();

            double sumTotal = 0.0;
            for (SaleCreateDTO saleDTO : sales) {
                sumTotal += saleDTO.price() * saleDTO.quantity();
            }

            Check check = Check.builder()
                    .checkNumber(dto.checkNumber())
                    .idEmployee(dto.idEmployee())
                    .cardNumber(dto.cardNumber())
                    .sumTotal(sumTotal)
                    .vat(dto.vat())
                    .build();
            checkDAO.create(check);

            for (SaleCreateDTO saleDTO : sales) {
                createSale(saleDAO, storeProductDAO, saleDTO);
            }
        });
    }

    private void createSale(SaleDAO saleDAO, StoreProductDAO storeProductDAO, SaleCreateDTO saleDTO) {
        Sale sale = Sale.builder()
                .UPC(saleDTO.UPC())
                .checkNumber(saleDTO.checkNumber())
                .quantity(saleDTO.quantity())
                .price(saleDTO.price())
                .build();
        saleDAO.create(sale);
        storeProductDAO.sellStoreProduct(saleDTO.UPC(), saleDTO.quantity());
    }

    public void deleteCheckByNumber(String checkNumber) {
        transactionManager.useTransaction(handle -> {
            SaleDAO saleDAO = handle.attach(SaleDAO.class);
            CheckDAO checkDAO = handle.attach(CheckDAO.class);

            saleDAO.deleteByCheckNumber(checkNumber);
            checkDAO.delete(checkNumber);
        });
    }

    public CheckDetailsDTO getCheckById(String checkNumber){
        return checkDAO.findById(checkNumber).orElseThrow(() -> new RuntimeException("Check not found"));
    }

    public List<CheckListDTO> getAllChecks(){
        return checkDAO.findAll();
    }

    public List<CheckListDTO> getCheckByFilter(CheckFilter filter){
        return checkDAO.findByFilter(filter);
    }

    public boolean existsByCardNumber(String cardNumber) {
        return checkDAO.existsByCardNumber(cardNumber);
    }
}