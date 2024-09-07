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
            // try to connect WorkStation page
            switch (selectedWorkstation) {
                case "Workstation 1":
                    loadNewPage("/com/example/workorder/WorkStation-1.fxml");
                    break;
                case "Workstation 2":
                    loadNewPage("/com/example/workorder/WorkStation-2.fxml");
                    break;
                case "Workstation 3":
                    loadNewPage("/com/example/workorder/WorkStation-3.fxml");
                    break;
                case "Workstation 4":
                    loadNewPage("/com/example/workorder/WorkStation-4.fxml");
                    break;
                case "Workstation 5":
                    loadNewPage("/com/example/workorder/WorkStation-5.fxml");
                    break;
                default:
                    System.out.println("No valid workstation selected.");
            }
        }
    }

    //function to loadNewPage
    private void loadNewPage(String fxmlFilePath) {
        try {
            //System.out.println("Loading FXML: " + fxmlFilePath);  // Debugging line
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(fxmlFilePath));
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
