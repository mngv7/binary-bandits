package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.protrack.applicationpages.WarehousePastRequests;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.requests.RequestsDAO;
import com.example.protrack.requests.Requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewPartController {
    @FXML
    private TableView<WarehousePastRequests> PartRequestsTable;
//    private List<PartRequests> partRequests;



    @FXML
    private TableColumn<WarehousePastRequests, Integer> colPartRequestsPartID;

    @FXML
    private TableColumn<WarehousePastRequests, String> colPartRequestsPartName;

    @FXML
    private TableColumn<WarehousePastRequests, Integer> colPartRequestsPartQuantity;

    private ObservableList<WarehousePastRequests> PartRequestsList;

//    private WarehouseController parentWarehouse;

    public Button closePopupButton;


//    private ViewPartController parentViewPart;

    //    public void setParentWarehouseController (WarehouseController warehouse){
//        this.parentWarehouse = warehouse;
//    }
//    public void setParentViewPartController (ViewPartController WarehousePastRequests){
//        this.parentViewPart = WarehousePastRequests;
//    }


    public void initialize() {



        // Set up the TableView columns with the corresponding property values
        colPartRequestsPartID.setCellValueFactory(new PropertyValueFactory<>("partsId"));
        colPartRequestsPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPartRequestsPartQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        //Initialize the ObservableList and set it to the TableView
        PartRequestsList = FXCollections.observableArrayList();
        PartRequestsTable.setItems(PartRequestsList);
        PartRequestsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadPartsRequestFormDB();

    }
    // load parts request from db.
    public void loadPartsRequestFormDB() {
        PartRequestsList.clear();  // Clear the list to avoid duplicates

        // Create DAO objects
        RequestsDAO requestsDAO = new RequestsDAO();

        // Use getPartRequests() method to fetch all part requests from both 'requests' and 'parts' tables
        List<WarehousePastRequests> partRequests = requestsDAO.getPartRequests();

        // Add all the part requests to the ObservableList
        PartRequestsList.addAll(partRequests);

        // Refresh the TableView to display updated data
        PartRequestsTable.refresh();
    }


    public void onClosePopupButton(ActionEvent actionEvent) {
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
