package com.vfrol.supermarket.tools.excel;

import com.vfrol.supermarket.dto.analytics.EmployeePerformanceDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class EmployeePerformanceExcelExporter extends BaseExcelExporter<EmployeePerformanceDTO> {

    @Override
    protected String getSheetName() {
        return "Employee Performance";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"Cashier", "Receipts", "Total Amount (UAH)"};
    }

    @Override
    protected void fillRow(EmployeePerformanceDTO item, Row row, ExcelStyles styles) {
        row.createCell(0).setCellValue(item.cashierName());
        row.createCell(1).setCellValue(item.receiptCount());

        Cell amtCell = row.createCell(2);
        amtCell.setCellValue(item.totalAmount());
        amtCell.setCellStyle(styles.money);
    }
}