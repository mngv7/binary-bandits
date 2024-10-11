package com.example.protrack.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCustomerController {

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
    private ComboBox<String> statusCombo;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private CustomerDAOImplementation customerDAOImplementation;

    public void initialize() {
        // Set default values or clear fields
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneNumberField.clear();
        billingAddressField.clear();
        shippingAddressField.clear();

        statusCombo.setValue("Active");
    }

    @FXML
    private void saveCustomer() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String billingAddress = billingAddressField.getText();
        String shippingAddress = shippingAddressField.getText();
        String status = statusCombo.getValue();

        // validation
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing Required Fields");
            alert.setContentText("Please complete missing form fields.");
            alert.showAndWait();
            return;
        }

        // Create a new customer object
        Customer newCustomer = new Customer(
                0,
                firstName,
                lastName,
                email,
                phoneNumber,
                billingAddress,
                shippingAddress,
                status
        );

        // Save customer using DAO
        customerDAOImplementation = new CustomerDAOImplementation();
        customerDAOImplementation.addCustomer(newCustomer);

        // Close the window
        closePopup();
    }

    @FXML
    private void closePopup() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}