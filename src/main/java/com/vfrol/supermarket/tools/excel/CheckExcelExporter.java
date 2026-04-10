package com.vfrol.supermarket.tools.excel;

import com.vfrol.supermarket.dto.check.CheckDetailsDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.time.format.DateTimeFormatter;

public class CheckExcelExporter extends BaseExcelExporter<CheckDetailsDTO> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected String getSheetName() {
        return "Checks Full Report";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                "Check Number", "Cashier", "Card Number", "Customer Name", "Date & Time", "Total Sum (UAH)", "VAT (UAH)"
        };
    }

    @Override
    protected void fillRow(CheckDetailsDTO item, Row row, ExcelStyles styles) {
        row.createCell(0).setCellValue(item.checkNumber());
        row.createCell(1).setCellValue(item.employeeName());
        row.createCell(2).setCellValue(item.cardNumber() != null ? item.cardNumber() : "");
        row.createCell(3).setCellValue(item.customerName() != null ? item.customerName() : "");

        String formattedDate = item.dateTime() != null ? item.dateTime().format(DATE_FORMATTER) : "";
        row.createCell(4).setCellValue(formattedDate);

        Cell totalCell = row.createCell(5);
        totalCell.setCellValue(item.sumTotal());
        totalCell.setCellStyle(styles.money);

        Cell vatCell = row.createCell(6);
        vatCell.setCellValue(item.vat());
        vatCell.setCellStyle(styles.money);
    }
}