package com.vfrol.supermarket.controller.store_product;

import com.vfrol.supermarket.controller.base.BaseReportController;
import com.vfrol.supermarket.dto.store_product.StoreProductDetailsDTO;
import com.vfrol.supermarket.tools.excel.BaseExcelExporter;
import com.vfrol.supermarket.tools.excel.StoreProductExcelExporter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class StoreProductReportController extends BaseReportController<StoreProductDetailsDTO> {

    @FXML private TableColumn<StoreProductDetailsDTO, String>  upcColumn;
    @FXML private TableColumn<StoreProductDetailsDTO, String>  upcPromColumn;
    @FXML private TableColumn<StoreProductDetailsDTO, String>  productNameColumn;
    @FXML private TableColumn<StoreProductDetailsDTO, String>  categoryColumn;
    @FXML private TableColumn<StoreProductDetailsDTO, Number>  priceColumn;
    @FXML private TableColumn<StoreProductDetailsDTO, Number>  quantityColumn;
    @FXML private TableColumn<StoreProductDetailsDTO, String>  promotionalColumn;

    @FXML
    public void initialize() {
        upcColumn.setCellValueFactory(cell        -> new SimpleStringProperty(cell.getValue().UPC()));
        upcPromColumn.setCellValueFactory(cell    -> new SimpleStringProperty(
                cell.getValue().UPCprom() != null ? cell.getValue().UPCprom() : ""));
        productNameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().productName()));
        categoryColumn.setCellValueFactory(cell   -> new SimpleStringProperty(cell.getValue().categoryName()));
        priceColumn.setCellValueFactory(cell      -> new SimpleDoubleProperty(cell.getValue().price()));
        quantityColumn.setCellValueFactory(cell   -> new SimpleIntegerProperty(cell.getValue().quantity()));
        promotionalColumn.setCellValueFactory(cell -> new SimpleStringProperty(
                Boolean.TRUE.equals(cell.getValue().promotional()) ? "Yes" : "No"));
    }

    @Override
    protected BaseExcelExporter<StoreProductDetailsDTO> getExporter() {
        return new StoreProductExcelExporter();
    }

    @Override
    protected String getDefaultFileName() {
        return "Store_Products_Full_Report.xlsx";
    }

    @Override
    protected String getReportTitle() {
        return "Save Store Product Report";
    }
}