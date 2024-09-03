package com.example.protrack.applicationpages;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AllocateWorkstationController {

    @FXML
    private ComboBox<String> workstationComboBox;

    @FXML
    private void handleClose() {
        Stage stage = (Stage) workstationComboBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAllocate() {
        String selectedWorkstation = workstationComboBox.getValue();
        if (selectedWorkstation != null) {
            // Handle allocation logic here
            System.out.println("Allocated: " + selectedWorkstation);
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
