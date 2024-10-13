package com.example.protrack.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for adding a new customer.
 * This class handles the UI and logic for saving customer information to the database.
 */
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

    /**
     * Initializes the controller class.
     * Clears form fields and sets default values.
     */
    public void initialize() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneNumberField.clear();
        billingAddressField.clear();
        shippingAddressField.clear();

        statusCombo.setValue("Active");
    }

    /**
     * Handles the save action when the save button is clicked.
     * Provides form validation before creating a new customer object
     * and then saves it using the DAO. Closes the window upon success.
     */
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

    /**
     * Closes the popup when the cancel button is clicked.
     */
    @FXML
    private void closePopup() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}