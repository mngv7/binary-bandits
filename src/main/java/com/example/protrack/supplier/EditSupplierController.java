package com.example.protrack.supplier;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private Label shippingAddressLabel;

    @FXML
    private Label leadTimeLabel;

    @FXML
    private TextField nameField;

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
    private Button editButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button closePopupButton;

    @FXML
    private Button deleteButton;

    private Supplier currentSupplier;

    @FXML
    private void initialize() { }

    @FXML
    private void toggleEdit() {
        boolean isEditing = nameField.isVisible();
        nameField.setVisible(!isEditing);
        emailField.setVisible(!isEditing);
        phoneNumberField.setVisible(!isEditing);
        billingAddressField.setVisible(!isEditing);
        shippingAddressField.setVisible(!isEditing);
        leadTimeField.setVisible(!isEditing);
        saveButton.setVisible(!isEditing);
        editButton.setText("Edit");

        if (!isEditing) {
            // Populate fields with current supplier values
            nameField.setText(currentSupplier.getName());
            emailField.setText(currentSupplier.getEmail());
            phoneNumberField.setText(currentSupplier.getPhoneNumber());
            billingAddressField.setText(currentSupplier.getBillingAddress());
            shippingAddressField.setText(currentSupplier.getShippingAddress());
            leadTimeField.setText(currentSupplier.getLeadTime().toString());
            editButton.setText("Cancel");
        }
    }

    @FXML
    private void saveSupplier() {
        currentSupplier.setName(nameField.getText());
        currentSupplier.setEmail(emailField.getText());
        currentSupplier.setPhoneNumber(phoneNumberField.getText());
        currentSupplier.setBillingAddress(billingAddressField.getText());
        currentSupplier.setShippingAddress(shippingAddressField.getText());
        currentSupplier.setLeadTime(Double.parseDouble(leadTimeField.getText()));

        SupplierDAO supplierDAO = new SupplierDAO();
        supplierDAO.updateSupplier(currentSupplier);

        // Update the labels to display the new values
        nameLabel.setText(currentSupplier.getName());
        emailLabel.setText(currentSupplier.getEmail());
        phoneNumberLabel.setText(currentSupplier.getPhoneNumber());
        billingAddressLabel.setText(currentSupplier.getBillingAddress());
        shippingAddressLabel.setText(currentSupplier.getShippingAddress());
        leadTimeLabel.setText(currentSupplier.getLeadTime().toString());

        // toggles view mode
        toggleEdit();
    }

    @FXML
    public void deleteSupplier() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Delete Supplier");
        alert.setContentText("Are you sure you want to delete this supplier?");
        alert.setGraphic(null);

        ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
        ButtonType backBtn = new ButtonType("Back", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(confirmBtn, backBtn);

        Button confirmButton = (Button) alert.getDialogPane().lookupButton(confirmBtn);
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(backBtn);

        confirmButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-style: bold;");
        cancelButton.setStyle("-fx-background-color: #ccccff; -fx-text-fill: white; -fx-style: bold");

        // Load the CSS file
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/protrack/stylesheet.css").toExternalForm());

        alert.showAndWait().ifPresent(response -> {
            if (response == confirmBtn) {
                SupplierDAO supplierDAO = new SupplierDAO();  // Use SupplierDAO instead of CustomerDAO
                supplierDAO.deleteSupplier(currentSupplier.getSupplierId());  // Use supplier.getSupplierId() instead of customer.getCustomerId()
                closePopup(); // Close the popup after deletion
            }
        });
    }

    public void setSupplier(Supplier supplier) {
        this.currentSupplier = supplier;
        supplierIdLabel.setText(String.valueOf(supplier.getSupplierId()));
        nameLabel.setText(supplier.getName());
        emailLabel.setText(supplier.getEmail());
        phoneNumberLabel.setText(supplier.getPhoneNumber());
        billingAddressLabel.setText(supplier.getBillingAddress());
        shippingAddressLabel.setText(supplier.getShippingAddress());
        leadTimeLabel.setText(supplier.getLeadTime().toString());
    }

    @FXML
    public void closePopup() {
        if (editButton.getText().equals("Cancel")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText("Close Supplier View");
            alert.setContentText("Are you sure you want to cancel editing the supplier?");
            alert.setGraphic(null);

            ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
            ButtonType backBtn = new ButtonType("Back", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(confirmBtn, backBtn);

            Button confirmButton = (Button) alert.getDialogPane().lookupButton(confirmBtn);
            Button cancelButton = (Button) alert.getDialogPane().lookupButton(backBtn);

            confirmButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-style: bold;");
            cancelButton.setStyle("-fx-background-color: #ccccff; -fx-text-fill: white; -fx-style: bold");

            // Load the CSS file
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/protrack/stylesheet.css").toExternalForm());

            // Show confirmation dialog and close if confirmed
            alert.showAndWait().ifPresent(result -> {
                if (result == confirmBtn) {
                    ((Stage) closePopupButton.getScene().getWindow()).close();
                }
            });
        } else {
            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.close();
        }
    }
}