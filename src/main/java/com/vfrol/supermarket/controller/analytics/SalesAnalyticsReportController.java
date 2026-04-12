package com.vfrol.supermarket.controller.analytics;

import com.vfrol.supermarket.controller.base.BaseReportController;
import com.vfrol.supermarket.dto.analytics.SalesAnalyticsDTO;
import com.vfrol.supermarket.tools.excel.BaseExcelExporter;
import com.vfrol.supermarket.tools.excel.SalesAnalyticsExcelExporter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SalesAnalyticsReportController extends BaseReportController<SalesAnalyticsDTO> {

    @FXML private TableColumn<SalesAnalyticsDTO, String> productNameColumn;
    @FXML private TableColumn<SalesAnalyticsDTO, Number> quantitySoldColumn;
    @FXML private TableColumn<SalesAnalyticsDTO, Number> totalAmountColumn;
    @FXML private TableColumn<SalesAnalyticsDTO, String> checkNumberColumn;
    @FXML private TableColumn<SalesAnalyticsDTO, String> cashierNameColumn;
    @FXML private TableColumn<SalesAnalyticsDTO, String> dateTimeColumn;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    public void initialize() {
        productNameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().productName()));
        quantitySoldColumn.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().quantitySold()));
        totalAmountColumn.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().totalAmount()));
        checkNumberColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().checkNumber() != null ? c.getValue().checkNumber() : ""));
        cashierNameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().cashierName() != null ? c.getValue().cashierName() : ""));
        dateTimeColumn.setCellValueFactory(c -> {
            var dt = c.getValue().dateTime();
            return new SimpleStringProperty(dt != null ? dt.format(DATE_FORMATTER) : "");
        });
    }

    @Override
    public void setData(List<SalesAnalyticsDTO> data) {
        List<SalesAnalyticsDTO> tableRows = new ArrayList<>(data);

        long totalQty = data.stream().mapToLong(SalesAnalyticsDTO::quantitySold).sum();
        double totalAmount = data.stream().mapToDouble(SalesAnalyticsDTO::totalAmount).sum();

        tableRows.add(SalesAnalyticsDTO.builder().productName("").build());
        tableRows.add(SalesAnalyticsDTO.builder()
                .productName("TOTALS")
                .quantitySold((int) totalQty)
                .totalAmount(totalAmount)
                .build());

        super.setData(tableRows);
    }

    @Override protected BaseExcelExporter<SalesAnalyticsDTO> getExporter() { return new SalesAnalyticsExcelExporter(); }
    @Override protected String getDefaultFileName() { return "Sales_Analytics_Report.xlsx"; }
    @Override protected String getReportTitle() { return "Save Sales Analytics Report"; }
}