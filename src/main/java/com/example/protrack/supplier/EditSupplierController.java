package com.example.protrack.supplier;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditSupplierController {

    @FXML
    private Label supplierIdLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Label billingAddressLabel;

    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField billingAddressField;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    private Supplier currentSupplier;

    // Initialize the controller
    @FXML
    private void initialize() {
        // Set the status combo box items
        statusCombo.getItems().addAll("Active", "Inactive");
    }

    // Toggle edit mode for the supplier details
    @FXML
    private void toggleEdit() {
        boolean isEditing = nameField.isVisible();
        nameField.setVisible(!isEditing);
        emailField.setVisible(!isEditing);
        phoneNumberField.setVisible(!isEditing);
        billingAddressField.setVisible(!isEditing);
        saveButton.setVisible(!isEditing);
        editButton.setVisible(isEditing);

        if (!isEditing) {
            // Display current values
            nameField.setText(currentSupplier.getName());
            emailField.setText(currentSupplier.getEmail());
            phoneNumberField.setText(currentSupplier.getPhoneNumber());
            billingAddressField.setText(currentSupplier.getBillingAddress());
        }
    }

    // Save the edited supplier details
    @FXML
    private void saveSupplier() {
        currentSupplier.setName(nameField.getText());
        currentSupplier.setEmail(emailField.getText());
        currentSupplier.setPhoneNumber(phoneNumberField.getText());
        currentSupplier.setBillingAddress(billingAddressField.getText());

        // Call the DAO to update the supplier in the database
        SupplierDAO supplierDAO = new SupplierDAO();
        supplierDAO.updateSupplier(currentSupplier);

        // Refresh the supplier list in the main table
        // You may need to call a method from SupplierController to refresh its table

        toggleEdit(); // Switch back to view mode
    }

    // Delete the current supplier
    @FXML
    private void deleteSupplier() {
        SupplierDAO supplierDAO = new SupplierDAO();
        supplierDAO.deleteSupplier(currentSupplier.getSupplierId());

        // Refresh the supplier list in the main table
        // You may need to call a method from SupplierController to refresh its table
    }

    // Method to set the current supplier being edited
    public void setCurrentSupplier(Supplier supplier) {
        this.currentSupplier = supplier;
        supplierIdLabel.setText(String.valueOf(supplier.getSupplierId()));
        nameLabel.setText(supplier.getName());
        emailLabel.setText(supplier.getEmail());
        phoneNumberLabel.setText(supplier.getPhoneNumber());
        billingAddressLabel.setText(supplier.getBillingAddress());
    }
}
