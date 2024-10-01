package com.example.protrack.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class CustomerController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField billingAddressField;
    @FXML
    private TextField shippingAddressField;
    @FXML
    private ComboBox<String> statusComboBox;
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

    @FXML
    private void handleAddCustomer() {
        // Gather data from fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String billingAddress = billingAddressField.getText();
        String shippingAddress = shippingAddressField.getText();
        String status = statusComboBox.getValue();

        // Create a new customer object and add to the database
        Customer newCustomer = new Customer(null, firstName, lastName, email, phoneNumber, billingAddress, shippingAddress, status);
        if (customerDAO.addCustomer(newCustomer)) {
            loadCustomers(); // Reload customers to update the table
            clearFields(); // Clear input fields after adding
        }
    }

    @FXML
    private void handleUpdateCustomer() {
        // Implementation for updating a selected customer
    }

    @FXML
    private void handleDeleteCustomer() {
        // Implementation for deleting a selected customer
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneNumberField.clear();
        billingAddressField.clear();
        shippingAddressField.clear();
        statusComboBox.setValue(null);
    }
}