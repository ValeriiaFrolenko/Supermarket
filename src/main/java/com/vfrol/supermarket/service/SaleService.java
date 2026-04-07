package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.vfrol.supermarket.dao.SaleDAO;
import com.vfrol.supermarket.dto.sale.SaleCreateDTO;
import com.vfrol.supermarket.dto.sale.SaleListDTO;
import com.vfrol.supermarket.entity.Sale;

import java.util.List;

public class SaleService {

    private final SaleDAO saleDAO;

    @Inject
    public SaleService(SaleDAO saleDAO) {
        this.saleDAO = saleDAO;
    }

    public void createSale(SaleCreateDTO dto){
        Sale sale = Sale.builder()
                .UPC(dto.UPC())
                .checkNumber(dto.checkNumber())
                .quantity(dto.quantity())
                .price(dto.price())
                .build();
        saleDAO.create(sale);
    }

    public void deleteSaleByCheckNumber(String checkNumber) {
        saleDAO.deleteByCheckNumber(checkNumber);
    }

    public void updateSale(SaleCreateDTO dto){
        Sale sale = Sale.builder()
                .UPC(dto.UPC())
                .checkNumber(dto.checkNumber())
                .quantity(dto.quantity())
                .price(dto.price())
                .build();
        saleDAO.update(sale);
    }

    public SaleListDTO getSaleByCheckNumber(String checkNumber) {
        return saleDAO.findByCheckNumber(checkNumber).orElseThrow(() -> new RuntimeException("Sale not found"));
    }

    public List<SaleListDTO> getSalesByUPC(String UPC){
        return saleDAO.findByUPC(UPC);
    }
}
