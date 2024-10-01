package com.example.protrack.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EditCustomerController {

    @FXML
    private Button closePopupButton;

    @FXML
    private Label customerIdLabel;

    @FXML
    private Label firstNameLabel;
    @FXML
    private TextField firstNameField;

    @FXML
    private Label lastNameLabel;
    @FXML
    private TextField lastNameField;

    @FXML
    private Label emailLabel;
    @FXML
    private TextField emailField;

    @FXML
    private Label phoneNumberLabel;
    @FXML
    private TextField phoneNumberField;

    @FXML
    private Label billingAddressLabel;
    @FXML
    private TextField billingAddressField;

    @FXML
    private Label shippingAddressLabel;
    @FXML
    private TextField shippingAddressField;
    @FXML
    private Label statusLabel;
    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    private boolean edit = false;

    private Customer customer;
    private CustomerDAO customerDAO;

    public void initialize() {
        // Hide editable fields initially
        firstNameField.setVisible(false);
        lastNameField.setVisible(false);
        emailField.setVisible(false);
        phoneNumberField.setVisible(false);
        billingAddressField.setVisible(false);
        shippingAddressField.setVisible(false);
        saveButton.setVisible(false);
        statusCombo.setVisible(false);

        // Initialize labels with customer data
        if (customer != null) {
            populateLabels();
        }
    }

    private void populateLabels() {
        customerIdLabel.setText(String.valueOf(customer.getCustomerId()));
        firstNameLabel.setText(customer.getFirstName());
        lastNameLabel.setText(customer.getLastName());
        emailLabel.setText(customer.getEmail());
        phoneNumberLabel.setText(customer.getPhoneNumber());
        billingAddressLabel.setText(customer.getBillingAddress());
        shippingAddressLabel.setText(customer.getShippingAddress());
        statusLabel.setText(customer.getStatus());
    }

    private void resetFieldsToOriginal() {
        editButton.setText("Edit");
        saveButton.setVisible(false);

        firstNameField.setVisible(false);
        lastNameField.setVisible(false);
        emailField.setVisible(false);
        phoneNumberField.setVisible(false);
        billingAddressField.setVisible(false);
        shippingAddressField.setVisible(false);
        statusCombo.setVisible(false);

        // Reset original values back to the labels
        populateLabels();
    }

    // This method will be called from another controller
    public void setCustomer(Customer customer) {
        this.customer = customer;

        // Populate view with customer fields
        populateLabels();
    }

    @FXML
    private void toggleEdit() {
        edit = !edit;

        if (edit) {
            // Switch to Edit Mode
            editButton.setText("Cancel");
            saveButton.setVisible(true);

            // Show editable fields
            firstNameField.setVisible(true);
            lastNameField.setVisible(true);
            emailField.setVisible(true);
            phoneNumberField.setVisible(true);
            billingAddressField.setVisible(true);
            shippingAddressField.setVisible(true);
            statusCombo.setVisible(true);

            // Populate fields with current values
            firstNameField.setText(firstNameLabel.getText());
            lastNameField.setText(lastNameLabel.getText());
            emailField.setText(emailLabel.getText());
            phoneNumberField.setText(phoneNumberLabel.getText());
            billingAddressField.setText(billingAddressLabel.getText());
            shippingAddressField.setText(shippingAddressLabel.getText());
            statusCombo.setValue(statusLabel.getText());

        } else {
            // Switch to View Mode and reset fields
            resetFieldsToOriginal();
        }
    }

    public void saveCustomer() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String billingAddress = billingAddressField.getText();
        String shippingAddress = shippingAddressField.getText();
        String status = statusCombo.getValue();


        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || status.isEmpty()) {
            System.out.println("Validation failed");
            return;
        }

        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhoneNumber(phoneNumber);
        customer.setBillingAddress(billingAddress);
        customer.setShippingAddress(shippingAddress);
        customer.setStatus(status);

        customerDAO = new CustomerDAO();
        customerDAO.updateCustomer(customer);

        resetFieldsToOriginal();
    }

    @FXML
    public void deleteCustomer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Delete Customer");
        alert.setContentText("Are you sure you want to delete this customer?");
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
                customerDAO = new CustomerDAO();
                customerDAO.deleteCustomer(customer.getCustomerId());
                closePopup(); // Close the popup after deletion
            }
        });
    }

    @FXML
    public void closePopup() {
        if (editButton.getText().equals("Cancel")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText("Close Customer View");
            alert.setContentText("Are you sure you want to cancel editing the customer?");
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