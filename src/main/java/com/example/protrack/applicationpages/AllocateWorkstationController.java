package com.example.protrack.applicationpages;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import com.example.protrack.warehouseutil.*;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

public class AllocateWorkstationController {
    private WarehouseController parentWarehouse;

    @FXML
    private ComboBox<String> workstationComboBox;

    public void initialize() {
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) workstationComboBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAllocate() {
        Workstation selectedWorkstation = parentWarehouse.getAllWorkstations().get(workstationComboBox.getSelectionModel().getSelectedIndex());
        if (selectedWorkstation != null) {
            // Handle allocation logic here
            System.out.println("Allocated: " + selectedWorkstation.getWorkstationName());
            // try to connect WorkStation page
            this.parentWarehouse.openWorkstation(selectedWorkstation);
            handleClose();
        }
    }

    public void setParentWarehouseController(WarehouseController warehouse) {
        this.parentWarehouse = warehouse;

        /* HACK: Initialise the workstationComboBox here too; this guarantees warehouse isn't null when it's filled. */
        if (this.parentWarehouse != null) {
            List<String> workstationNames = new ArrayList<>();
            for (int i = 0; i < parentWarehouse.getAllWorkstations().size(); ++i) {
                workstationNames.add(parentWarehouse.getAllWorkstations().get(i).getWorkstationName());
            }
            workstationComboBox.setItems(FXCollections.observableArrayList(workstationNames));
        }
    }

    //function to loadNewPage
    private void loadNewPage(String fxmlFilePath) {
        try {
            //System.out.println("Loading FXML: " + fxmlFilePath);  // Debugging line
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Parent root = loader.load();

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) workstationComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log the error if FXML loading fails
            System.out.println("Failed to load FXML file: " + fxmlFilePath);
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) workstationComboBox.getScene().getWindow();
        stage.close();
    }
}
