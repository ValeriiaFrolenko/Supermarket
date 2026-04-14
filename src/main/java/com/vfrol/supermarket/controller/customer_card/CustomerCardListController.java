package com.vfrol.supermarket.controller.customer_card;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.base.BaseListController;
import com.vfrol.supermarket.controller.util.AsyncRunner;
import com.vfrol.supermarket.controller.util.Debouncer;
import com.vfrol.supermarket.controller.util.InputHelper;
import com.vfrol.supermarket.controller.util.SessionUIHelper;
import com.vfrol.supermarket.dto.customer_card.CustomerCardDetailsDTO;
import com.vfrol.supermarket.dto.customer_card.CustomerCardListDTO;
import com.vfrol.supermarket.enums.sortby.CustomerCardSortBy;
import com.vfrol.supermarket.filter.CustomerCardFilter;
import com.vfrol.supermarket.service.CustomerCardService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

public class CustomerCardListController extends BaseListController<CustomerCardListDTO> {

    private final CustomerCardService customerCardService;
    private final Debouncer searchDebouncer = new Debouncer(300);
    private ObservableList<CustomerCardListDTO> customerCardData;

    @FXML private TextField searchField;
    @FXML private TableView<CustomerCardListDTO> customerCardTable;
    @FXML private TableColumn<CustomerCardListDTO, String> cardNumberColumn;
    @FXML private TableColumn<CustomerCardListDTO, String> surnameColumn;
    @FXML private TableColumn<CustomerCardListDTO, String> nameColumn;
    @FXML private TableColumn<CustomerCardListDTO, Number> discountColumn;
    @FXML private TableColumn<CustomerCardListDTO, String> phoneColumn;
    @FXML private Button exportButton;

    @FXML private VBox filterPanel;
    @FXML private TextField phoneFilterField;
    @FXML private TextField discountFromField;
    @FXML private TextField discountToField;
    @FXML private ComboBox<CustomerCardSortBy> sortByComboBox;

    @Inject
    public CustomerCardListController(CustomerCardService customerCardService) {
        this.customerCardService = customerCardService;
    }

    @FXML
    public void initialize() {
        SessionUIHelper.configureManagerOnlyNodes(sessionManager, exportButton);
        initializeTable();
        initializeFilters();
        loadAllCustomerCards();
    }

    private void initializeTable() {
        customerCardData = FXCollections.observableArrayList();

        cardNumberColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().cardNumber()));
        surnameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().surname()));
        nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().name()));
        discountColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().discount()));
        phoneColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().phone()));

        customerCardTable.setItems(customerCardData);
        setupTableDoubleClick();
    }

    private void initializeFilters() {
        searchField.textProperty().addListener((_,_,_) ->
                searchDebouncer.debounce(this::applyFilter));
        phoneFilterField.textProperty().addListener((_,_,_) ->
                searchDebouncer.debounce(this::applyFilter));

        sortByComboBox.getItems().addAll(CustomerCardSortBy.values());
    }

    @Override
    protected TableView<CustomerCardListDTO> getTableView() {
        return customerCardTable;
    }

    @Override
    protected void showDetails(CustomerCardListDTO item) {
        AsyncRunner.runAsync(
                () -> customerCardService.getCardById(item.cardNumber()),
                details -> {
                    navigateToDetails(details);
                    loadAllCustomerCards();
                },
                customerCardTable
        );
    }

    @FXML
    public void onAddCustomerCardClick() {
        navigateToForm();
        loadAllCustomerCards();
    }

    @FXML
    public void onExportClick() {
        AsyncRunner.runAsync(
                customerCardService::getAllCustomerCardDetails,
                this::navigateToReport,
                getRootNode()
        );
    }

    @FXML
    public void onToggleFilterClick() {
        toggleFilterPanel(filterPanel);
    }

    @FXML
    public void applyFilter() {
        CustomerCardFilter filter = buildFilter();
        setProgressIndicator();

        if (filter.isEmpty()) {
            AsyncRunner.runAsync(
                    customerCardService::getAllCards,
                    this::updateTableData,
                    customerCardTable
            );
        } else {
            AsyncRunner.runAsync(
                    () -> customerCardService.getCardsByFilter(filter),
                    this::updateTableData,
                    customerCardTable
            );
        }
    }

    @FXML
    public void onClearFilterClick() {
        searchField.clear();
        phoneFilterField.clear();
        discountFromField.clear();
        discountToField.clear();
        sortByComboBox.setValue(null);
        applyFilter();
    }

    private CustomerCardFilter buildFilter() {
        return CustomerCardFilter.builder()
                .surname(InputHelper.getString(searchField))
                .phoneNumber(InputHelper.getString(phoneFilterField))
                .discountFrom(InputHelper.getInt(discountFromField))
                .discountTo(InputHelper.getInt(discountToField))
                .sortBy(sortByComboBox.getValue())
                .build();
    }

    private void loadAllCustomerCards() {
        setProgressIndicator();
        AsyncRunner.runAsync(
                customerCardService::getAllCards,
                this::updateTableData,
                customerCardTable
        );
    }

    private void updateTableData(List<CustomerCardListDTO> data) {
        customerCardData.setAll(data);
        removeProgressIndicator();
    }

    private void navigateToDetails(CustomerCardDetailsDTO details) {
        viewManager.showDialog(AppView.CUSTOMER_CARD_DETAILS,
                (CustomerCardDetailsController controller) ->
                        controller.setDetails(details));
    }

    private void navigateToForm() {
        viewManager.showDialog(AppView.CUSTOMER_CARD_FORM);
    }

    private void navigateToReport(List<CustomerCardDetailsDTO> cards) {
        viewManager.showDialog(AppView.CUSTOMER_CARD_REPORT,
                (CustomerCardReportController controller) ->
                        controller.setData(cards));
    }
}