package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.parts.*;
import com.example.protrack.requests.Requests;
import com.example.protrack.requests.RequestsDAO;
import com.example.protrack.warehouseutil.LocationsAndContentsDAO;
import com.example.protrack.warehouseutil.Workstation;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class CreatePartsRequestController {

    @FXML
    private ComboBox<Workstation> workstationComboBox;

    @FXML
    private ComboBox<Parts> partComboBox;

    @FXML
    private TextField partQuantityField;

    // Buttons for requesting stock and closing the popup
    @FXML
    public Button sendRequestButton;
    @FXML
    public Button closePopupButton;

    public void initialize() {

        // Populate the ComboBoxes with data from the database
        partComboBox.getItems().setAll(new PartsDAO().getAllParts());
        workstationComboBox.getItems().setAll(new LocationsAndContentsDAO().getAllWorkstations());

        // Create a binding to check if any essential field is empty
        BooleanBinding emptyFields = Bindings.createBooleanBinding(() ->
                partQuantityField.getText().trim().isEmpty() ||
                        partComboBox.getSelectionModel().isEmpty() ||
                        workstationComboBox.getSelectionModel().isEmpty(),
                partQuantityField.textProperty()
        );

        sendRequestButton.disableProperty().bind(emptyFields);
    }

    // Method to clear product input fields
    private void clearPartInputFields() {
        partComboBox.getSelectionModel().clearSelection();
        workstationComboBox.getSelectionModel().clearSelection();
        partQuantityField.clear();
    }

    /**
     * Event handler for the "Request Stock Transfer" button.
     * Parses the input fields to create a new Requests object and adds it to the database.
     */
    @FXML
    protected void onSendRequestButton() {
        // Create a RequestsDAO to handle database connection
        RequestsDAO requestsDAO = new RequestsDAO();

        try {
            // Get the selected items from the ComboBoxes
            Parts selectedPart = partComboBox.getSelectionModel().getSelectedItem();
            Workstation selectedWorkstation = workstationComboBox.getSelectionModel().getSelectedItem();

            int quantity = Integer.parseInt(partQuantityField.getText());

            // Get the quantity entered by the user
            if (quantity <= 0) {
                throw new RuntimeException("Quantity cannot be less than 0");
            } else {
                // Create a new request and add it to the database
                requestsDAO.newRequest(new Requests(selectedWorkstation.getWorkstationId(), selectedPart.getPartsId(), (requestsDAO.getAllRequests().toArray()).length + 1, quantity));
            }

            // Reset the part selection and quantity fields
            clearPartInputFields();

        } catch (NumberFormatException e) {
            // Alert handles invalid number formats for quantity
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Quantity");
            alert.setContentText("Please enter a valid number for quantity.");
            alert.showAndWait();
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
        alert.setHeaderText("Cancel Stock Request");
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
