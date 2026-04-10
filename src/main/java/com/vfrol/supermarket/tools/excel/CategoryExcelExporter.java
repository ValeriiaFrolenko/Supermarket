package com.vfrol.supermarket.tools.excel;

import com.vfrol.supermarket.dto.category.CategoryListDTO;
import org.apache.poi.ss.usermodel.Row;

public class CategoryExcelExporter extends BaseExcelExporter<CategoryListDTO> {
    @Override
    protected String getSheetName() {
        return "Categories Report";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                "ID", "Name"
        };
    }

    @Override
    protected void fillRow(CategoryListDTO item, Row row, ExcelStyles styles) {
        row.createCell(0).setCellValue(item.id());
        row.createCell(1).setCellValue(item.name());
    }
}
