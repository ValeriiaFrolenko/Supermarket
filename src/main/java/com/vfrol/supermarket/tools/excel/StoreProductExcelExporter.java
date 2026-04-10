package com.vfrol.supermarket.tools.excel;

import com.vfrol.supermarket.dto.store_product.StoreProductListDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class StoreProductExcelExporter extends BaseExcelExporter<StoreProductListDTO> {

    @Override
    protected String getSheetName() {
        return "Store Products Report";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                "UPC", "Product Name", "Price (UAH)", "Quantity", "Promotional"
        };
    }

    @Override
    protected void fillRow(StoreProductListDTO item, Row row, ExcelStyles styles) {
        row.createCell(0).setCellValue(item.UPC());
        row.createCell(1).setCellValue(item.productName());

        Cell priceCell = row.createCell(2);
        priceCell.setCellValue(item.price());
        priceCell.setCellStyle(styles.money);

        row.createCell(3).setCellValue(item.quantity());

        boolean isPromotional = item.promotional() != null && item.promotional();
        row.createCell(4).setCellValue(isPromotional ? "Yes" : "No");
    }
}