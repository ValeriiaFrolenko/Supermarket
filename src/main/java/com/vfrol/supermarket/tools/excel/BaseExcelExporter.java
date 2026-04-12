package com.vfrol.supermarket.tools.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public abstract class BaseExcelExporter<T> {

    protected abstract String   getSheetName();
    protected abstract String[] getColumns();
    protected abstract void     fillRow(T item, Row row, ExcelStyles styles);

    protected void writeFooter(Sheet sheet, ExcelStyles styles, List<T> data, int nextRowNum) {
        // default: no footer
    }

    public void export(List<T> data, File file) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(getSheetName());
            ExcelStyles styles = new ExcelStyles(workbook);

            writeHeader(sheet, styles);

            int rowNum = 1;
            for (T item : data) {
                fillRow(item, sheet.createRow(rowNum++), styles);
            }

            writeFooter(sheet, styles, data, rowNum);

            for (int i = 0; i < getColumns().length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to export Excel file: " + e.getMessage(), e);
        }
    }

    private void writeHeader(Sheet sheet, ExcelStyles styles) {
        String[] columns = getColumns();
        Row header = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(styles.header);
        }
    }

    protected static class ExcelStyles {
        public final CellStyle header;
        public final CellStyle date;
        public final CellStyle money;

        ExcelStyles(Workbook workbook) {
            Font bold = workbook.createFont();
            bold.setBold(true);

            header = workbook.createCellStyle();
            header.setFont(bold);

            date = workbook.createCellStyle();
            date.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd"));

            money = workbook.createCellStyle();
            money.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        }
    }
}