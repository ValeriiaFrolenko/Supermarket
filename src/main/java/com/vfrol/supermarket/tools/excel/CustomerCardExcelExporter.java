package com.vfrol.supermarket.tools.excel;

import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import org.apache.poi.ss.usermodel.Row;

public class CustomerCardExcelExporter extends BaseExcelExporter<CustomerCardDetailsDTO> {

    @Override
    protected String getSheetName() {
        return "Customer Cards Full Report";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                "Card Number", "Surname", "Name", "Patronymic", "Phone", "City", "Street", "Zip Code", "Discount (%)"
        };
    }

    @Override
    protected void fillRow(CustomerCardDetailsDTO item, Row row, ExcelStyles styles) {
        row.createCell(0).setCellValue(item.cardNumber());
        row.createCell(1).setCellValue(item.surname());
        row.createCell(2).setCellValue(item.name());
        row.createCell(3).setCellValue(item.patronymic() != null ? item.patronymic() : "");
        row.createCell(4).setCellValue(item.phoneNumber());
        row.createCell(5).setCellValue(item.city() != null ? item.city() : "");
        row.createCell(6).setCellValue(item.street() != null ? item.street() : "");
        row.createCell(7).setCellValue(item.zipCode() != null ? item.zipCode() : "");
        row.createCell(8).setCellValue(item.discount());
    }
}