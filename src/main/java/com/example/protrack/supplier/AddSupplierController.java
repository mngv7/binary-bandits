package com.example.protrack.supplier;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * AddSupplierController manages the user interface for adding new suppliers.
 */
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

    /**
     * Initializes the controller automatically after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialization code can go here, if needed
    }

    /**
     * Saves the supplier information entered by the user.
     */
    @FXML
    private void saveSupplier() {
        String name = supplierNameField.getText();
        String email = emailField.getText();
        String phone = phoneNumberField.getText();
        String billingAddress = billingAddressField.getText();
        String shippingAddress = shippingAddressField.getText();
        Double leadTime;
        String leadTimeText = leadTimeField.getText();

        // Validate leadTimeField input to ensure it's a double
        try {
            leadTime = Double.parseDouble(leadTimeText);
        } catch (NumberFormatException e) {
            // Display an alert if input is not a valid double
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please check all fields are valid.");
            alert.setGraphic(null);

            ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
            alert.getButtonTypes().setAll(confirmBtn);

            Button confirmButton = (Button) alert.getDialogPane().lookupButton(confirmBtn);
            confirmButton.setStyle("-fx-background-color: #390b91; -fx-text-fill: white; -fx-style: bold;");

            alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/protrack/stylesheet.css").toExternalForm());

            alert.showAndWait();
            return; // Exit the method if validation fails
        }

        // Calls DAO to save the supplier
        Supplier supplier = new Supplier(0, name, email, phone, billingAddress, shippingAddress, leadTime);
        SupplierDAOImplementation supplierDAOImplementation = new SupplierDAOImplementation();
        supplierDAOImplementation.addSupplier(supplier);

        // Closes popup after saving
        closePopup();
    }

    /**
     * Closes the popup window. This method is called after a supplier is saved.
     */
    @FXML
    private void closePopup() {
        Stage stage = (Stage) supplierNameField.getScene().getWindow();
        stage.close();
    }
}
