package com.vfrol.supermarket.tools.excel;

import com.vfrol.supermarket.dto.product.ProductDetailsDTO;
import org.apache.poi.ss.usermodel.Row;

public class ProductExcelExporter extends BaseExcelExporter<ProductDetailsDTO> {

    @Override
    protected String getSheetName() {
        return "Products Report";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                "ID", "Name", "Category", "Characteristics"
        };
    }

    @Override
    protected void fillRow(ProductDetailsDTO item, Row row, ExcelStyles styles) {
        row.createCell(0).setCellValue(item.id());
        row.createCell(1).setCellValue(item.name());
        row.createCell(2).setCellValue(item.categoryName());
        row.createCell(3).setCellValue(item.characteristics());
    }
}