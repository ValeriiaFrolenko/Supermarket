package com.vfrol.supermarket.tools.excel;

import com.vfrol.supermarket.dto.check.CheckListDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.time.format.DateTimeFormatter;

public class CheckExcelExporter extends BaseExcelExporter<CheckListDTO> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected String getSheetName() {
        return "Checks Report";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                "Check Number", "Cashier", "Date & Time", "Total Sum (UAH)"
        };
    }

    @Override
    protected void fillRow(CheckListDTO item, Row row, ExcelStyles styles) {
        row.createCell(0).setCellValue(item.checkNumber());
        row.createCell(1).setCellValue(item.employeeName());

        String formattedDate = item.dateTime() != null ? item.dateTime().format(DATE_FORMATTER) : "";
        row.createCell(2).setCellValue(formattedDate);

        Cell totalCell = row.createCell(3);
        totalCell.setCellValue(item.sumTotal());
        totalCell.setCellStyle(styles.money);
    }
}