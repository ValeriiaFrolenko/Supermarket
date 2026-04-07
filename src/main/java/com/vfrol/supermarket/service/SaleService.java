package com.vfrol.supermarket.service;

import com.google.inject.Inject;
import com.vfrol.supermarket.dao.SaleDAO;
import com.vfrol.supermarket.dto.sale.SaleListDTO;

import java.util.List;

public class SaleService {

    private final SaleDAO saleDAO;

    @Inject
    public SaleService(SaleDAO saleDAO) {
        this.saleDAO = saleDAO;
    }

    public List<SaleListDTO> getSalesByCheckNumber(String checkNumber) {
        return saleDAO.findByCheckNumber(checkNumber);
    }

    public List<SaleListDTO> getSalesByUPC(String UPC){
        return saleDAO.findByUPC(UPC);
    }
}
