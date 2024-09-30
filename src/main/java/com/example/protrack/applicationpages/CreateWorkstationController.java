package com.example.protrack.applicationpages;

import com.example.protrack.warehouseutil.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateWorkstationController {
    /*
     * Creating a new Workstation requires adding new entries to the LocationsAndContentsDAO which
     * requires having a reference to the WarehouseController that opened the CreateWarehouse dialog.
     */
    public WarehouseController parentWarehouse;
    public LocationsAndContentsDAO dao;

    @FXML
    private TextField nameField;

    @FXML
    private TextField capacityField;

    public void initialize() {
        dao = new LocationsAndContentsDAO();
    }

    public CreateWorkstationController() {
        this.parentWarehouse = null;
    }

    public CreateWorkstationController(WarehouseController parentWarehouse) {
        this.parentWarehouse = parentWarehouse;
    }

    public void setParentWarehouseController (WarehouseController parentWarehouse) {
        this.parentWarehouse = parentWarehouse;
    }

    // the function to create the data in workstation table
    @FXML
    private void handleCreate() {
        /* TODO: Auto-increment ID number based on number of workstations already in the database. */
        Workstation station = new RealWorkstation(11, nameField.getText(), Integer.parseInt(capacityField.getText()));
        dao.newWorkstation(station);
        parentWarehouse.addWorkstation(station);
        closeDialog();
    }

    // the functions to close the createWorkstation page from two different places.
    // TODO: Condense this code into a single function to close the window in question.
    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
