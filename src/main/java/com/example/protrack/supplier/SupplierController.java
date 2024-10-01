package com.example.protrack.supplier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.util.List;

public class SupplierController {

    @FXML
    private TableView<Supplier> suppliersTableView;

    @FXML
    private TableColumn<Supplier, Integer> supplierIdColumn;

    @FXML
    private TableColumn<Supplier, String> nameColumn;

    @FXML
    private TableColumn<Supplier, String> emailColumn;

    @FXML
    private TableColumn<Supplier, String> phoneNumberColumn;

    @FXML
    private TableColumn<Supplier, String> billingAddressColumn;

    @FXML
    private Button addSupplierButton;

    @FXML
    private TextField searchSupplierIdField;

    @FXML
    private TextField searchSupplierNameField;

    private ObservableList<Supplier> supplierList;

    private SupplierDAO supplierDAO;

    // Initialize the controller
    @FXML
    private void initialize() {
        // Set up the TableView columns with the corresponding property values
        supplierIdColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        billingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("billingAddress"));

        // Initialize the ObservableList and set it to the TableView
        supplierList = FXCollections.observableArrayList();
        suppliersTableView.setItems(supplierList);

        // Load and display the initial list of suppliers
        refreshTable();
    }

    // Refresh the suppliers table with data from the database
    public void refreshTable() {
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        supplierList.setAll(suppliers);
    }

    // Open the add supplier popup
    @FXML
    private void addSupplierPopup() {
        // Implementation for opening add supplier popup
    }

    // Method to initialize the SupplierDAO (to be called from another part of the application)
    public void setSupplierDAO(SupplierDAO supplierDAO) {
        this.supplierDAO = supplierDAO;
    }
}
