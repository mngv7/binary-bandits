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

/**
 * Controller for handling the stock request creation UI.
 * Allows users to select parts and quantities, and submit stock requests.
 */
public class CreatePartsRequestController {

    @FXML
    private ComboBox<Parts> partComboBox;

    @FXML
    private TextField partQuantityField;

    // Buttons for requesting stock and closing the popup
    @FXML
    public Button sendRequestButton;
    @FXML
    public Button closePopupButton;

    private int workStationId = -1;

    /**
     * Initialises the controller by populating the part selection ComboBox
     * and binding the "Request Stock" button to ensure no empty fields.
     */
    public void initialize() {

        // Populate the ComboBoxes with parts from the database
        partComboBox.getItems().setAll(new PartsDAO().getAllParts());

        // Disable the send button if any required field is empty
        BooleanBinding emptyFields = Bindings.createBooleanBinding(() ->
                partQuantityField.getText().trim().isEmpty() ||
                        partComboBox.getSelectionModel().isEmpty(),
                partQuantityField.textProperty()
        );

        sendRequestButton.disableProperty().bind(emptyFields);
    }

    /**
     * Sets the ID of the workstation making the stock request.
     * @param value ID of the workstation
     */
    public void setWorkStationId(Integer value) {
        workStationId = value;
    }

    /**
     * Clears all part selection input fields after a request is made.
     */
    private void clearPartInputFields() {
        partComboBox.getSelectionModel().clearSelection();
        //workstationComboBox.getSelectionModel().clearSelection();
        partQuantityField.clear();
    }

    /**
     * Handles the submission of a stock request. Validates the input
     * and adds a new request to the database if the input is valid.
     */
    @FXML
    protected void onSendRequestButton() {
        // Create a RequestsDAO to handle database connection
        RequestsDAO requestsDAO = new RequestsDAO();

        try {
            // Get the selected items from the ComboBoxes
            Parts selectedPart = partComboBox.getSelectionModel().getSelectedItem();
            //Workstation selectedWorkstation = workstationComboBox.getSelectionModel().getSelectedItem();

            int quantity = Integer.parseInt(partQuantityField.getText());

            // Get the quantity entered by the user
            if (quantity <= 0) {
                throw new RuntimeException("Quantity cannot be less than 0");
            } else {
                // Create a new request and add it to the database
                requestsDAO.newRequest(new Requests(workStationId, selectedPart.getPartsId(), (requestsDAO.getAllRequests().toArray()).length + 1, quantity));
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
     * Handles the close action for the popup window, displaying
     * a confirmation dialog before closing the window.
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
        stage.close();

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
