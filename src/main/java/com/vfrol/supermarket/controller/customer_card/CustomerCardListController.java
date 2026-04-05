package com.vfrol.supermarket.controller.customer_card;

import com.google.inject.Inject;
import com.vfrol.supermarket.config.AppView;
import com.vfrol.supermarket.controller.BaseListController;
import com.vfrol.supermarket.controller.SecurityUIHelper;
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

    @FXML private TextField searchField;
    @FXML private Button addButton;

    @FXML private TableView<CustomerCardListDTO> customerCardTable;
    @FXML private TableColumn<CustomerCardListDTO, String> cardNumberColumn;
    @FXML private TableColumn<CustomerCardListDTO, String> surnameColumn;
    @FXML private TableColumn<CustomerCardListDTO, String> nameColumn;
    @FXML private TableColumn<CustomerCardListDTO, Number> discountColumn;
    @FXML private TableColumn<CustomerCardListDTO, String> phoneColumn;

    @FXML private VBox filterPanel;
    @FXML private TextField phoneFilterField;
    @FXML private TextField discountFromField;
    @FXML private TextField discountToField;
    @FXML private ComboBox<CustomerCardSortBy> sortByComboBox;

    private ObservableList<CustomerCardListDTO> customerCardData;

    @Inject
    public CustomerCardListController(CustomerCardService customerCardService) {
        this.customerCardService = customerCardService;
    }

    @Override
    protected TableView<CustomerCardListDTO> getTableView() {
        return customerCardTable;
    }

    @Override
    protected void showDetails(CustomerCardListDTO item) {
        showCustomerCardDetails(item.cardNumber());
    }

    @FXML
    public void initialize() {
        initializeTable();
        sortByComboBox.getItems().addAll(CustomerCardSortBy.values());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());
        SecurityUIHelper.configureManagerOnlyNodes(sessionManager, addButton);
        loadCustomerCards();
    }

    private void initializeTable() {
        customerCardData = FXCollections.observableArrayList();

        cardNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().cardNumber()));
        surnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().surname()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name()));
        discountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().discount()));
        phoneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().phone()));

        customerCardTable.setItems(customerCardData);
        setupTableDoubleClick();
    }

    @FXML
    public void onToggleFilterClick() {
        toggleFilterPanel(filterPanel);
    }

    @FXML
    public void applyFilter() {
        String searchText = searchField.getText();
        String surname = (searchText != null && !searchText.isBlank()) ? searchText.trim() : null;

        String phoneText = phoneFilterField.getText();
        String phone = (phoneText != null && !phoneText.isBlank()) ? phoneText.trim() : null;

        Integer discountFrom = null;
        Integer discountTo = null;
        try {
            String fromText = discountFromField.getText();
            if (fromText != null && !fromText.isBlank()) discountFrom = Integer.parseInt(fromText.trim());
        } catch (NumberFormatException ignored) {}
        try {
            String toText = discountToField.getText();
            if (toText != null && !toText.isBlank()) discountTo = Integer.parseInt(toText.trim());
        } catch (NumberFormatException ignored) {}

        CustomerCardFilter filter = CustomerCardFilter.builder()
                .surname(surname)
                .phoneNumber(phone)
                .discountFrom(discountFrom)
                .discountTo(discountTo)
                .sortBy(sortByComboBox.getValue())
                .build();

        List<CustomerCardListDTO> filtered = customerCardService.getCardsByFilter(filter);
        customerCardData.clear();
        customerCardData.addAll(filtered);
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

    private void loadCustomerCards() {
        List<CustomerCardListDTO> cards = customerCardService.getAllCards();
        customerCardData.clear();
        customerCardData.addAll(cards);
    }

    private void showCustomerCardDetails(String cardNumber) {
        CustomerCardDetailsDTO details = customerCardService.getCardById(cardNumber);
        if (details != null) {
            viewManager.showDialog(AppView.CUSTOMER_CARD_DETAILS, (CustomerCardDetailsController controller) -> {
                controller.setCustomerCardDetails(details);
            });
        }
        refreshCustomerCards();
    }

    public void refreshCustomerCards() {
        loadCustomerCards();
    }

    @FXML
    public void onAddCustomerCardClick() {
        viewManager.showDialog(AppView.CUSTOMER_CARD_FORM);
        refreshCustomerCards();
    }
}