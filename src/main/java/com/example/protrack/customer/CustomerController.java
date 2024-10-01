package com.example.protrack.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class CustomerController {

    @FXML
    private TableView<Customer> customersTableView;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> firstNameColumn;
    @FXML
    private TableColumn<Customer, String> lastNameColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> phoneNumberColumn;
    @FXML
    private TableColumn<Customer, String> billingAddressColumn;
    @FXML
    private TableColumn<Customer, String> shippingAddressColumn;
    @FXML
    private TableColumn<Customer, String> statusColumn;

    private CustomerDAO customerDAO;
    private ObservableList<Customer> tableCustomers;

    public CustomerController() {
        customerDAO = new CustomerDAO();
        tableCustomers = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCustomers();
    }

    private void setupTableColumns() {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        billingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("billingAddress"));
        shippingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("shippingAddress"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadCustomers() {
        List<Customer> customers = customerDAO.getAllCustomers();
        tableCustomers.clear();
        tableCustomers.addAll(customers);
        customersTableView.setItems(tableCustomers);
    }
}