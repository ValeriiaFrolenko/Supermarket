package com.vfrol.supermarket.tools.excel;

import com.vfrol.supermarket.dto.analytics.SalesAnalyticsDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class SalesAnalyticsExcelExporter extends BaseExcelExporter<SalesAnalyticsDTO> {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    protected String getSheetName() {
        return "Sales Analytics";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                "Product Name", "Qty Sold", "Total Amount (UAH)",
                "Receipt #", "Cashier", "Date & Time"
        };
    }

    @Override
    protected void fillRow(SalesAnalyticsDTO item, Row row, ExcelStyles styles) {
        row.createCell(0).setCellValue(item.productName());

        row.createCell(1).setCellValue(item.quantitySold());

        Cell amtCell = row.createCell(2);
        amtCell.setCellValue(item.totalAmount());
        amtCell.setCellStyle(styles.money);

        row.createCell(3).setCellValue(item.checkNumber());
        row.createCell(4).setCellValue(item.cashierName());
        row.createCell(5).setCellValue(
                item.dateTime() != null ? item.dateTime().format(DATE_FORMATTER) : "");
    }

    @Override
    protected void writeFooter(Sheet sheet, ExcelStyles styles,
                               List<SalesAnalyticsDTO> data, int nextRowNum) {
        long   totalQty    = data.stream().mapToLong(SalesAnalyticsDTO::quantitySold).sum();
        double totalAmount = data.stream().mapToDouble(SalesAnalyticsDTO::totalAmount).sum();

        sheet.createRow(nextRowNum);

        Row totalsRow = sheet.createRow(nextRowNum + 1);

        Cell labelCell = totalsRow.createCell(0);
        labelCell.setCellValue("TOTALS");
        labelCell.setCellStyle(styles.header);

        Cell qtyCell = totalsRow.createCell(1);
        qtyCell.setCellValue(totalQty);
        qtyCell.setCellStyle(styles.header);

        Cell amtCell = totalsRow.createCell(2);
        amtCell.setCellValue(totalAmount);
        amtCell.setCellStyle(styles.money);
    }
}