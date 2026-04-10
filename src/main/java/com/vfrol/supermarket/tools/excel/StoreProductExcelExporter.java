package com.vfrol.supermarket.tools.excel;

import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class StoreProductExcelExporter extends BaseExcelExporter<StoreProductDetailsDTO> {

    @Override
    protected String getSheetName() {
        return "Store Products Full Report";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{
                "UPC", "UPC Promo", "Product ID", "Product Name", "Category", "Price (UAH)", "Quantity", "Promotional"
        };
    }

    @Override
    protected void fillRow(StoreProductDetailsDTO item, Row row, ExcelStyles styles) {
        row.createCell(0).setCellValue(item.UPC());
        row.createCell(1).setCellValue(item.UPCprom() != null ? item.UPCprom() : "");
        row.createCell(2).setCellValue(item.productId());
        row.createCell(3).setCellValue(item.productName());
        row.createCell(4).setCellValue(item.categoryName());

        Cell priceCell = row.createCell(5);
        priceCell.setCellValue(item.price());
        priceCell.setCellStyle(styles.money);

        row.createCell(6).setCellValue(item.quantity());

        boolean isPromotional = item.promotional() != null && item.promotional();
        row.createCell(7).setCellValue(isPromotional ? "Yes" : "No");
    }
}