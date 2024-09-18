package com.example.protrack.applicationpages;

import com.example.protrack.warehouseutil.MockWorkstation;
import com.example.protrack.warehouseutil.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateWorkstationController {
    public WarehouseController parentWarehouse;
    @FXML
    private TextField nameField;

    @FXML
    private TextField capacityField;

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
        Workstation station = new MockWorkstation(11, nameField.getText(), "null", Integer.parseInt(capacityField.getText()));
        parentWarehouse.addWorkstation(station);
        closeDialog();
    }
    // the function to close the createWorkstation page
    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
