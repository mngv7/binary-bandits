package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class AddPartsController {

    // FXML fields for part details
    @FXML
    private TextField partIdField;
    @FXML
    private TextField partNameField;
    @FXML
    private TextField partDescField;
    @FXML
    private TextField partSupplierIdField;
    @FXML
    private TextField partCostField;

    // Buttons for adding part and closing the popup
    @FXML
    public Button addPartButton;
    @FXML
    public Button closePopupButton;

    /**
     * Initializes the controller class. This method is automatically called after the FXML file
     * has been loaded. Sets up a BooleanBinding to disable the "Add Part" button if any required
     * fields are empty.
     */
    public void initialize() {
        // Create a binding to check if any field is empty
        BooleanBinding emptyFields = Bindings.createBooleanBinding(() ->
                        partIdField.getText().trim().isEmpty() ||
                                partNameField.getText().trim().isEmpty() ||
                                partDescField.getText().trim().isEmpty() ||
                                partSupplierIdField.getText().trim().isEmpty() ||
                                partCostField.getText().trim().isEmpty(),
                partIdField.textProperty(),
                partNameField.textProperty(),
                partDescField.textProperty(),
                partSupplierIdField.textProperty(),
                partCostField.textProperty()
        );
        // Disable the "Add Part" button if any fields are empty
        addPartButton.disableProperty().bind(emptyFields);
    }

    /**
     * Event handler for the "Add Part" button.
     * Parses the input fields to create a new Parts object and adds it to the database.
     */
    @FXML
    protected void onAddPartButton() {
        // Create a PartsDAO to handle database operations
        PartsDAO partsDAO = new PartsDAO();

        try {
            // Parse the input fields
            int partId = Integer.parseInt(partIdField.getText());
            String partName = partNameField.getText();
            String partDesc = partDescField.getText();
            int partSupplierId = Integer.parseInt(partSupplierIdField.getText());
            double partCost = Double.parseDouble(partCostField.getText());

            // Create a new part and add it to the database
            partsDAO.newPart(new Parts(partId, partName, partDesc, partSupplierId, partCost));

            // Clear the input fields after successful addition
            partIdField.setText("");
            partNameField.setText("");
            partDescField.setText("");
            partSupplierIdField.setText("");
            partCostField.setText("");

        } catch (NumberFormatException e) {
            // Handle invalid input for numerical fields
            System.out.println("Invalid part ID. Please enter a valid number.");
        }
    }

    /**
     * Event handler for the "Close Popup" button.
     * Displays a confirmation dialog asking the user if they want to cancel part creation.
     */
    @FXML
    protected void onClosePopupButton() {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Cancel Part Creation");
        alert.setContentText("Are you sure you want to cancel?");
        alert.setGraphic(null);

        // Apply custom stylesheet to the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        String stylesheet = Objects.requireNonNull(Main.class.getResource("cancelAlert.css")).toExternalForm();
        dialogPane.getStyleClass().add("cancelDialog");
        dialogPane.getStylesheets().add(stylesheet);

        // Define the confirm and back buttons
        ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
        ButtonType backBtn = new ButtonType("Back", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(confirmBtn, backBtn);

        // Get the current stage (popup window)
        Stage stage = (Stage) closePopupButton.getScene().getWindow();

        // Set button data for confirm and back buttons
        Node confirmButton = dialogPane.lookupButton(confirmBtn);
        ButtonBar.setButtonData(confirmButton, ButtonBar.ButtonData.LEFT);
        confirmButton.setId("confirmBtn");
        Node backButton = dialogPane.lookupButton(backBtn);
        ButtonBar.setButtonData(backButton, ButtonBar.ButtonData.RIGHT);
        backButton.setId("backBtn");

        // Show the alert and handle the user's response
        alert.showAndWait();
        if (alert.getResult().getButtonData() == ButtonBar.ButtonData.YES) {
            // Close the stage if user confirms cancellation
            alert.close();
            stage.close();
        } else if (alert.getResult().getButtonData() == ButtonBar.ButtonData.NO) {
            // Close the alert if user decides to go back
            alert.close();
        }
    }
}
