package com.example.protrack.applicationpages;

import com.example.protrack.warehouseutil.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;

/* TODO: Now that the database structure is sort of present, how do we implement this well? */
public class WarehouseController {

    @FXML
    private TableView<Workstation> workstationTable;

    //@FXML
    //private TableView<SaleOrder> saleOrderTable;

    @FXML
    public void initialize() {
        loadWorkstationData();

        // Create and set the context menu for the workstationTable
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().add(deleteMenuItem);

        deleteMenuItem.setOnAction(event -> handleDeleteWorkstation());

        workstationTable.setOnContextMenuRequested(event -> {
            contextMenu.show(workstationTable, event.getScreenX(), event.getScreenY());
        });
    }

    private void loadWorkstationData() {
        /* TODO: Need to load from DB for this in production. */
        ObservableList<Workstation> workstations = FXCollections.observableArrayList(
                // Load data from database
                new MockWorkstation(0,"Workstation 1", "Warehouse room A"),
                new MockWorkstation(1,"Workstation 2", "Warehouse room A"),
                new MockWorkstation(2,"Workstation 3", "Warehouse room A"),
                new MockWorkstation(3,"Workstation 4", "Warehouse room A")
        );

        workstationTable.setItems(workstations);
    }

    private void handleDeleteWorkstation() {
        Workstation selectedWorkstation = workstationTable.getSelectionModel().getSelectedItem();
        if (selectedWorkstation != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete this workstation?");
            alert.setContentText("This action cannot be undone.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                // Remove the selected item from the table
                workstationTable.getItems().remove(selectedWorkstation);
                // Optionally, remove it from the database or data source here
            }
        }
    }

    /* TODO: Sale orders are not implemented yet. */
    private void loadSaleOrderData() {
        System.out.println("FIXME: loadSaleOrderData stubbed.");
    }

    /* TODO: Workstation DB and related code is very incomplete, attempt to resolve with the database guys later. */
    @FXML
    private void handleAddWorkstation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/CreateWorkstation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Create Workstation");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Optionally, reload workstation data after adding a new workstation
            loadWorkstationData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button allocateWorkstationButton;

    @FXML
    private void handleAllocateWorkstation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/AllocateWorkstation.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Allocate Workstation");
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}
