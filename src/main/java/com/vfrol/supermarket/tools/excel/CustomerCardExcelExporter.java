package com.vfrol.supermarket.tools.excel;

import com.vfrol.supermarket.dto.customer_card.CustomerCardListDTO;
import org.apache.poi.ss.usermodel.Row;

public class CustomerCardExcelExporter extends BaseExcelExporter<CustomerCardListDTO> {

    @Override
    protected String getSheetName() {
        return "Customer Cards Report";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                "Card Number", "Surname", "Name", "Phone", "Discount (%)"
        };
    }

    @Override
    protected void fillRow(CustomerCardListDTO item, Row row, ExcelStyles styles) {
        row.createCell(0).setCellValue(item.cardNumber());
        row.createCell(1).setCellValue(item.surname());
        row.createCell(2).setCellValue(item.name());
        row.createCell(3).setCellValue(item.phone());
        row.createCell(4).setCellValue(item.discount());
    }
}