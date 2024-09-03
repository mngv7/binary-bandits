package com.example.protrack.applicationpages;

import com.example.protrack.workstationutil.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WarehouseController {

    @FXML
    private TableView<Workstation> workstationTable;

    //@FXML
    //private TableView<SaleOrder> saleOrderTable;

    @FXML
    public void initialize() {
        loadWorkstationData();
        //loadSaleOrderData(); TODO: Commented as sale orders are not implemented yet.
    }

    private void loadWorkstationData() {
        // TODO: Work out what Cindy's instance of ChatGPT has tried to cook here.
//        ObservableList<Workstation> workstations = FXCollections.observableArrayList(
//                // Load data from database
//                new SQLWorkstation("Workstation 1", 100, "Empty", "Low", 0),
//                new SQLWorkstation("Workstation 2", 10, "Empty", "Low", 0),
//                new SQLWorkstation("Workstation 3", 1, "John Doe", "High", 3),
//                new SQLWorkstation("Workstation 4", 20, "Empty", "Critical", 0)
//        );
        // TODO: Fix this placeholder which makes sure it compiles.
        ObservableList<Workstation> workstations = FXCollections.observableArrayList(
                // Load data from database
                new SQLWorkstation("Workstation 1", 100, null),
                new SQLWorkstation("Workstation 2", 10, null),
                new SQLWorkstation("Workstation 3", 1, null),
                new SQLWorkstation("Workstation 4", 20, null)
        );

        workstationTable.setItems(workstations);
    }

    /* TODO: Sale orders are not implemented yet. */
//    private void loadSaleOrderData() {
//        ObservableList<SaleOrder> saleOrders = FXCollections.observableArrayList(
//                // Load data from database
//                new SaleOrder("SO-000001", "Workstation 3", "Jonathon Doe", 3, 57, "Product Fault")
//        );
//
//        saleOrderTable.setItems(saleOrders);
//    }

    @FXML
    private void handleAddWorkstation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/CreateWorkstation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Create Workstation");
            stage.initModality(Modality.APPLICATION_MODAL);
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
        stage.show();
    }
}
