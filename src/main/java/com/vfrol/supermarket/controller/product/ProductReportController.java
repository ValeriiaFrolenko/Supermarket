package com.vfrol.supermarket.controller.product;

import com.vfrol.supermarket.controller.base.BaseReportController;
import com.vfrol.supermarket.dto.product.ProductDetailsDTO;
import com.vfrol.supermarket.tools.excel.BaseExcelExporter;
import com.vfrol.supermarket.tools.excel.ProductExcelExporter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class ProductReportController extends BaseReportController<ProductDetailsDTO> {

    @FXML private TableColumn<ProductDetailsDTO, Integer> idColumn;
    @FXML private TableColumn<ProductDetailsDTO, String> nameColumn;
    @FXML private TableColumn<ProductDetailsDTO, String> categoryColumn;
    @FXML private TableColumn<ProductDetailsDTO, String> manufacturerColumn;
    @FXML private TableColumn<ProductDetailsDTO, String> characteristicsColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().id()).asObject());
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().name()));
        categoryColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().categoryName()));
        manufacturerColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().manufacturer()));
        characteristicsColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().characteristics()));
    }

    @Override
    protected BaseExcelExporter<ProductDetailsDTO> getExporter() {
        return new ProductExcelExporter();
    }

    @Override
    protected String getDefaultFileName() {
        return "Products_Full_Report.xlsx";
    }

    @Override
    protected String getReportTitle() {
        return "Save Product Report";
    }
}