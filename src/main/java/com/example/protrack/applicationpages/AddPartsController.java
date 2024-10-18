package com.example.protrack.applicationpages;

import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controller for handling the new part creation UI.
 * Allows managerial users to create new parts
 */
public class AddPartsController {

    // Buttons for adding part and closing the popup
    @FXML
    public Button addPartButton;
    @FXML
    public Button closePopupButton;
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
     * Handles the close action for the popup window, displaying
     * a confirmation dialog before closing the window.
     */
    @FXML
    protected void onClosePopupButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Cancel Part Creation");
        alert.setContentText("Are you sure you want to cancel?");
        alert.setGraphic(null);

        // Define dialog buttons
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
    }
}
