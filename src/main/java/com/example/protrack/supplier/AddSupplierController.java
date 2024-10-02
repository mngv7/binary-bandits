package com.example.protrack.supplier;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddSupplierController {

    @FXML
    private TextField supplierNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField billingAddressField;
    @FXML
    private TextField shippingAddressField;
    @FXML
    private TextField leadTimeField;

    @FXML
    private void initialize() { }

    @FXML
    private void saveSupplier() {
        // Implement the logic to save the supplier to the database
        String name = supplierNameField.getText();
        String email = emailField.getText();
        String phone = phoneNumberField.getText();
        String billingAddress = billingAddressField.getText();
        //String shippingAddress = shippingAddressField.getText();
        //Double leadTime = leadTimeField.getText();

        // Call the DAO to save the supplier (implement DAO logic accordingly)
        //Supplier supplier = new Supplier(0, name, email, phone, billingAddress, shippingAddress, leadTime);
        //SupplierDAO supplierDAO = new SupplierDAO();
        //supplierDAO.addSupplier(supplier);

        // Close the popup after saving
        closePopup();
    }

    @FXML
    private void closePopup() {
        Stage stage = (Stage) supplierNameField.getScene().getWindow();
        stage.close();
    }
}
