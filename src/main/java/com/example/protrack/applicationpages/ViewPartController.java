package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.warehouseutil.MockWorkstation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.protrack.applicationpages.WarehousePastRequests;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
//import com.example.protrack.warehouseutil.LocationsAndContentsDAO;

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
        colPartRequestsPartID.setCellValueFactory(new PropertyValueFactory<>("Part ID"));
        colPartRequestsPartName.setCellValueFactory(new PropertyValueFactory<>("Part Name"));
        colPartRequestsPartQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));

        //Initialize the ObservableList and set it to the TableView
        PartRequestsList = FXCollections.observableArrayList();
        PartRequestsTable.setItems(PartRequestsList);
        PartRequestsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        // Load and display the initial list of work orders
//        refreshTable();
    }



    public void refreshTable() {
        // Create DAOs for fetching data
//        UsersDAO usersDAO = new UsersDAO();
//        CustomerDAO customerDAO = new CustomerDAO();
//        WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(
//                usersDAO.getProductionUsers(),
//                customerDAO.getAllCustomers()
//        );

        // Clear the current list and load the updated work orders
//        PartRequestsList.clear();
//        PartRequestsList.addAll(PartRequestsDAO());
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
