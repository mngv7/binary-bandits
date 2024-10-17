package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.warehouseutil.LocationsAndContentsDAO;
import com.example.protrack.warehouseutil.Warehouse;
import com.example.protrack.warehouseutil.Workstation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.example.protrack.requests.*;

import java.util.List;
import java.util.Objects;

public class ViewPartController {
    @FXML
    private TableView<Requests> PartRequestsTable;

    @FXML
    private TableColumn<Requests, Integer> colPartRequestsPartID;

    @FXML
    private TableColumn<Requests, String> colPartRequestsPartName;

    @FXML
    private TableColumn<Requests, Integer> colPartRequestsPartQuantity;

    @FXML
    public Button closePopupButton;

    List<Requests> partRequests;
    private ObservableList<Requests> PartRequestsList;

    public void initialize() {
        // Set up the TableView columns with the corresponding property values
        colPartRequestsPartID.setCellValueFactory(new PropertyValueFactory<>("partId"));
        colPartRequestsPartName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        colPartRequestsPartQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // If click the data in table, will show delete and accept options
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem acceptMenuItem = new MenuItem("Accept");
        contextMenu.getItems().addAll(acceptMenuItem, deleteMenuItem);
        // connect the options to functions
        deleteMenuItem.setOnAction(event -> handleDeletePartRequest());
        acceptMenuItem.setOnAction(event -> handleAcceptPartRequest());

        PartRequestsTable.setOnContextMenuRequested(event -> {
            contextMenu.show(PartRequestsTable, event.getScreenX(), event.getScreenY());
        });

        //Initialize the ObservableList and set it to the TableView
        PartRequestsList = FXCollections.observableArrayList();
        PartRequestsTable.setItems(PartRequestsList);
        PartRequestsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadPartsRequestFormDB();

    }

    /**
     * Event handler for the "AcceptPartRequest" button.
     * Accept the data from Part Request and send the data to WareStation
     */
    private void handleAcceptPartRequest() {
        Requests request = PartRequestsTable.getSelectionModel().getSelectedItem();
        if (request != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Accept Confirmation");
            alert.setHeaderText("Would you like to accept this parts request? This will automatically transfer parts to the Workstation that sent this request.");
            alert.setContentText("This action cannot be undone.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                /* Transfer parts from Warehouse to Workstation. */
                RequestsDAO requestsDAO = new RequestsDAO();
                LocationsAndContentsDAO dao = new LocationsAndContentsDAO();
                Warehouse targetWarehouse = dao.getAllWarehouses().get(0); /* Get first warehouse as there's only one warehouse. */
                List<Workstation> allWorkstations = dao.getAllWorkstations();

                /* Man, wtf is this... */
                /* connect the part Database to Location and Contents Database*/
                for (int i = 0; i < allWorkstations.size(); ++i) {
                    if (allWorkstations.get(i).getWorkstationLocationId() == request.getLocationId()) {
                        allWorkstations.get(i).importPartsIdWithQuantityFromWarehouse(targetWarehouse, dao, request.getPartId(), request.getQuantity());
                        PartRequestsTable.getItems().remove(request);
                        requestsDAO.removePartRequest(request.getPartId());
                        PartRequestsTable.refresh();
                        return; /* Exit this loop and function. */
                    }
                }

                /* If we reach here at all, we've hit an error condition. */
                throw new IllegalArgumentException("The workstation for this parts request doesn't exist.");
            }
        }
    }
    /**
     * Event handler for the "DeletePartRequest" button.
     * Delete the data from Part Request
     */
    private void handleDeletePartRequest() {
        Requests selectedPartRequest = PartRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedPartRequest != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete this part request?");
            alert.setContentText("This action cannot be undone.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                // Remove the selected request from the TableView
                PartRequestsTable.getItems().remove(selectedPartRequest);

                // Remove the selected request from the database using partId
                RequestsDAO requestsDAO = new RequestsDAO();
                requestsDAO.removePartRequest(selectedPartRequest.getPartId());  // Pass partId directly

                // Optionally, refresh the table after the deletion
                PartRequestsTable.refresh();
            }
        }
    }

    /**
     * Event handler for loadPartsRequestFormDB.
     * Refresh the table and get the data from database again.
     */
    public void loadPartsRequestFormDB() {
        PartRequestsList.clear();  // Clear the list to avoid duplicates

        // Create DAO objects
        RequestsDAO requestsDAO = new RequestsDAO();

        // Use getPartRequests() method to fetch all part requests from both 'requests' and 'parts' tables
        partRequests = requestsDAO.getAllRequests();

        // Add all the part requests to the ObservableList
        PartRequestsList.addAll(partRequests);

        // Refresh the TableView to display updated data
        PartRequestsTable.refresh();
    }
    /**
     * Event handler for the "Close Popup" button.
     * Displays a confirmation dialog asking the user if they want to cancel part creation.
     */
    public void onClosePopupButton() {
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
