package com.example.protrack.applicationpages;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateWorkstationController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField capacityField;



    @FXML
    private void handleCreate() {
        // Your creation logic here
        closeDialog();
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
