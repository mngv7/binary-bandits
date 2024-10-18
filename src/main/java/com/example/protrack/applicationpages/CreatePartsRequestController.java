package com.example.protrack.applicationpages;

import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.requests.Requests;
import com.example.protrack.requests.RequestsDAO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }
}
