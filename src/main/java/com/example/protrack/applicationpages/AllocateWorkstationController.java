package com.example.protrack.applicationpages;

import com.example.protrack.warehouseutil.LocationsAndContentsDAO;
import com.example.protrack.warehouseutil.Workstation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllocateWorkstationController {
    /*
     * Handling Workstation allocations means we have to read the Workstation data loaded by
     * WarehouseController, therefore an instance of it is put here by code in WarehouseController.
     */
    public WarehouseController parentWarehouse;

    @FXML
    private ComboBox<String> workstationComboBox;

    public void initialize() {
    }

    // TODO: Why do we have both handleClose and closeDialog?
    @FXML
    private void handleClose() {
        Stage stage = (Stage) workstationComboBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAllocate() {
        Workstation selectedWorkstation = parentWarehouse.getAllWorkstationsInRAM().get(workstationComboBox.getSelectionModel().getSelectedIndex());
        if (selectedWorkstation != null) {
            // Allocate and open our workstation here.
            System.out.println("Allocated: " + selectedWorkstation.getWorkstationLocationAlias());
            parentWarehouse.getMainControllerInstance().loadWorkstationContent(workstationComboBox.getValue());
            handleClose();
        }
    }

    public void setParentWarehouseController(WarehouseController warehouse) {
        this.parentWarehouse = warehouse;

        /* HACK: Initialise the workstationComboBox here too; this guarantees warehouse isn't null when it's filled. */
        if (this.parentWarehouse != null) {
            List<String> workstationNames = new ArrayList<>();
            for (int i = 0; i < parentWarehouse.getAllWorkstationsInRAM().size(); ++i) {
                workstationNames.add(parentWarehouse.getAllWorkstationsInRAM().get(i).getWorkstationLocationAlias());
            }
            workstationComboBox.setItems(FXCollections.observableArrayList(workstationNames));
        }
    }

    // This function loads a new FXML page when called, complete with an error handler.
    private void loadNewPage(String fxmlFilePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Parent root = loader.load();

            WorkStationController workStationController = loader.getController();
            LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();

            int workstationId = locationsAndContentsDAO.getLocationIDFromAlias(workstationComboBox.getValue());
            workStationController.setWorkStationId(workstationId);

            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) workstationComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log the error if FXML loading fails
            System.out.println("Failed to load FXML file: " + fxmlFilePath);
        }
    }

    // These both close the combo box this controller controls.
    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) workstationComboBox.getScene().getWindow();
        stage.close();
    }
}
