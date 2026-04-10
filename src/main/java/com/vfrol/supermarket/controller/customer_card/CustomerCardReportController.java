package com.vfrol.supermarket.controller.customer_card;

import com.vfrol.supermarket.controller.base.BaseReportController;
import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import com.vfrol.supermarket.tools.excel.BaseExcelExporter;
import com.vfrol.supermarket.tools.excel.CustomerCardExcelExporter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

public class CustomerCardReportController extends BaseReportController<CustomerCardDetailsDTO> {

    @FXML private TableColumn<CustomerCardDetailsDTO, String> cardNumberColumn;
    @FXML private TableColumn<CustomerCardDetailsDTO, String> surnameColumn;
    @FXML private TableColumn<CustomerCardDetailsDTO, String> nameColumn;
    @FXML private TableColumn<CustomerCardDetailsDTO, String> patronymicColumn;
    @FXML private TableColumn<CustomerCardDetailsDTO, String> phoneColumn;
    @FXML private TableColumn<CustomerCardDetailsDTO, String> cityColumn;
    @FXML private TableColumn<CustomerCardDetailsDTO, String> streetColumn;
    @FXML private TableColumn<CustomerCardDetailsDTO, String> zipColumn;
    @FXML private TableColumn<CustomerCardDetailsDTO, Integer> discountColumn;

    @FXML
    public void initialize() {
        cardNumberColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().cardNumber()));
        surnameColumn.setCellValueFactory(cell    -> new SimpleStringProperty(cell.getValue().surname()));
        nameColumn.setCellValueFactory(cell       -> new SimpleStringProperty(cell.getValue().name()));
        patronymicColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().patronymic() != null ? cell.getValue().patronymic() : ""));
        phoneColumn.setCellValueFactory(cell      -> new SimpleStringProperty(cell.getValue().phoneNumber()));
        cityColumn.setCellValueFactory(cell       -> new SimpleStringProperty(cell.getValue().city() != null ? cell.getValue().city() : ""));
        streetColumn.setCellValueFactory(cell     -> new SimpleStringProperty(cell.getValue().street() != null ? cell.getValue().street() : ""));
        zipColumn.setCellValueFactory(cell        -> new SimpleStringProperty(cell.getValue().zipCode() != null ? cell.getValue().zipCode() : ""));
        discountColumn.setCellValueFactory(cell   -> new SimpleIntegerProperty(cell.getValue().discount()).asObject());
    }

    @Override
    protected BaseExcelExporter<CustomerCardDetailsDTO> getExporter() {
        return new CustomerCardExcelExporter();
    }

    @Override
    protected String getDefaultFileName() {
        return "Customer_Cards_Full_Report.xlsx";
    }

    @Override
    protected String getReportTitle() {
        return "Save Customer Card Report";
    }
}