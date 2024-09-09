package com.example.protrack.applicationpages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

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
            // try to connect WorkStation page
            loadNewPage("/com/example/protrack/WorkStation.fxml");
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
