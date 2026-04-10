package com.vfrol.supermarket.controller.category;

import com.vfrol.supermarket.controller.base.BaseReportController;
import com.vfrol.supermarket.dto.category.CategoryListDTO;
import com.vfrol.supermarket.tools.excel.BaseExcelExporter;
import com.vfrol.supermarket.tools.excel.CategoryExcelExporter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class CategoryReportController extends BaseReportController<CategoryListDTO> {

    @FXML private TableColumn<CategoryListDTO, Integer> idColumn;
    @FXML private TableColumn<CategoryListDTO, String> nameColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().id()).asObject());
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().name()));
    }

    @Override
    protected BaseExcelExporter<CategoryListDTO> getExporter() {
        return new CategoryExcelExporter();
    }

    @Override
    protected String getDefaultFileName() {
        return "Category_Full_Report.xlsx";
    }

    @Override
    protected String getReportTitle() {
        return "Save Category Report";
    }
}
