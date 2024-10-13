package com.example.protrack.supplier;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * EditSupplierController manages the user interface for editing existing suppliers.
 */
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

    /**
     * Initializes the controller automatically after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialization code can go here, if needed
    }

    /**
     * Toggles the edit mode for the supplier details.
     */
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

    /**
     * Saves the updated supplier information entered by the user.
     */
    @FXML
    private void saveSupplier() {
        currentSupplier.setName(nameField.getText());
        currentSupplier.setEmail(emailField.getText());
        currentSupplier.setPhoneNumber(phoneNumberField.getText());
        currentSupplier.setBillingAddress(billingAddressField.getText());
        currentSupplier.setShippingAddress(shippingAddressField.getText());

        // Validate lead time input
        Double leadTime;
        try {
            leadTime = Double.parseDouble(leadTimeField.getText());
        } catch (NumberFormatException e) {
            // Display an alert if input is not a valid double
            showAlert("Invalid Input", "Lead time must be a valid number.");
            return; // Exit the method if validation fails
        }
        currentSupplier.setLeadTime(leadTime);

        SupplierDAOImplementation supplierDAOImplementation = new SupplierDAOImplementation();
        supplierDAOImplementation.updateSupplier(currentSupplier);

        // Update the labels to display the new values
        nameLabel.setText(currentSupplier.getName());
        emailLabel.setText(currentSupplier.getEmail());
        phoneNumberLabel.setText(currentSupplier.getPhoneNumber());
        billingAddressLabel.setText(currentSupplier.getBillingAddress());
        shippingAddressLabel.setText(currentSupplier.getShippingAddress());
        leadTimeLabel.setText(currentSupplier.getLeadTime().toString());

        // Toggle to view mode
        toggleEdit();
    }

    /**
     * Displays a confirmation alert for deleting the supplier.
     */
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

        // Style buttons
        styleConfirmationButtons(alert, confirmBtn, backBtn);

        // Load the CSS file
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/protrack/stylesheet.css").toExternalForm());

        // Show confirmation dialog and close if confirmed
        alert.showAndWait().ifPresent(response -> {
            if (response == confirmBtn) {
                SupplierDAOImplementation supplierDAOImplementation = new SupplierDAOImplementation();
                supplierDAOImplementation.deleteSupplier(currentSupplier.getSupplierId());
                closePopup(); // Close the popup after deletion
            }
        });
    }

    /**
     * Sets the current supplier to be edited and updates the display labels.
     *
     * @param supplier The supplier to be edited.
     */
    public void setSupplier(Supplier supplier) {
        this.currentSupplier = supplier;
        supplierIdLabel.setText(String.valueOf(supplier.getSupplierId()));
        nameLabel.setText(supplier.getName());
        emailLabel.setText(supplier.getEmail());
        phoneNumberLabel.setText(supplier.getPhoneNumber());
        billingAddressLabel.setText(supplier.getBillingAddress());
        shippingAddressLabel.setText(supplier.getShippingAddress());
        leadTimeLabel.setText(currentSupplier.getLeadTime().toString());
    }

    /**
     * Closes the popup window, confirming if changes should be discarded.
     */
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

            // Style buttons
            styleConfirmationButtons(alert, confirmBtn, backBtn);

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

    /**
     * Shows an alert with a given title and message.
     *
     * @param title   The title of the alert.
     * @param message The message content of the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.setGraphic(null);

        ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
        alert.getButtonTypes().setAll(confirmBtn);

        Button confirmButton = (Button) alert.getDialogPane().lookupButton(confirmBtn);
        confirmButton.setStyle("-fx-background-color: #390b91; -fx-text-fill: white; -fx-style: bold;");

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/protrack/stylesheet.css").toExternalForm());
        alert.showAndWait();
    }

    /**
     * Styles confirmation buttons for alert dialogs.
     *
     * @param alert       The alert to style.
     * @param confirmBtn  The confirmation button type.
     * @param backBtn     The back button type.
     */
    private void styleConfirmationButtons(Alert alert, ButtonType confirmBtn, ButtonType backBtn) {
        Button confirmButton = (Button) alert.getDialogPane().lookupButton(confirmBtn);
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(backBtn);

        confirmButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-style: bold;");
        cancelButton.setStyle("-fx-background-color: #ccccff; -fx-text-fill: white; -fx-style: bold;");
    }
}
