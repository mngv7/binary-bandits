package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.warehouseutil.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Objects;

/* TODO: Now that the database structure is sort of present, how do we implement this well? */
public class WarehouseController {

    @FXML
    private TableView<Workstation> workstationTable;
    private List<Workstation> workstations; /* Loaded workstations from a database. */

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
        if (workstations == null) {
            workstations = new ArrayList<>();

            /* TODO: load from mock/real DB later. For now, make dummy data. */
            workstations.add(new MockWorkstation(0, "Workstation for noobs.", "Warehouse room A"));
            workstations.add(new MockWorkstation(1, "Workstation 2", "Warehouse room A"));
            workstations.add(new MockWorkstation(2, "Workstation 3", "Warehouse room A"));
            workstations.add(new MockWorkstation(3, "Workstation 4", "Warehouse room A"));
        }
        workstationTable.setItems(FXCollections.observableArrayList(workstations));
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
                workstations.remove(selectedWorkstation);
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

            CreateWorkstationController controller = loader.getController();
            if (controller != null) {
                controller.setParentWarehouseController(this);
            }

            Stage stage = new Stage();
            String stylesheet = Objects.requireNonNull(Main.class.getResource("main_app.css")).toExternalForm();
            root.getStylesheets().add(stylesheet);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Create Workstation");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadWorkstationData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWorkstation (Workstation workstation) {
        workstations.add(workstation);
    }

    public void openWorkstation (Workstation workstation) {
        /* TODO: Connect selected workstation to new page. */
        try {
            //System.out.println("Loading FXML: " + fxmlFilePath);  // Debugging line
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/WorkStation.fxml"));
            Parent root = loader.load();

            // Switch to the Workstation UI.
            /* HACK: Pull the VBox used in MainController from here to clear; layout changes will likely break this. */
            VBox contentPane = (VBox) workstationTable.getParent();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace(); // Log the error if FXML loading fails
            System.out.println("Failed to load FXML file: /com/example/protrack/WorkStation.fxml");
        }
    }

    @FXML
    private Button allocateWorkstationButton;

    @FXML
    private void handleAllocateWorkstation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/AllocateWorkstation.fxml"));
        Parent root = loader.load();

        AllocateWorkstationController controller = loader.getController();
        if (controller != null) {
            controller.setParentWarehouseController(this);
        }

        Scene rootScene = new Scene(root);
        String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
        rootScene.getStylesheets().add(stylesheet);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Allocate Workstation");
        stage.setScene(rootScene);
        stage.showAndWait();
    }
}
