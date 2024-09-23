package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import com.example.protrack.warehouseutil.*;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class AllocateWorkstationController {
    /*
     * Handling Workstation allocations means we have to read the Workstation data loaded by
     * WarehouseController, therefore an instance of it is put here by code in WarehouseController.
     */
    private WarehouseController parentWarehouse;

    @FXML
    private ComboBox<String> workstationComboBox;

    @FXML
    private Button allocateButton;

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



            // Handle allocation logic here
            System.out.println("Allocated: " + selectedWorkstation.getWorkstationName());
            goToWorkstationPage();

            // try to connect WorkStation page
            //this.parentWarehouse.openWorkstation(selectedWorkstation);
            //handleClose();
        }
    }

    public void setParentWarehouseController(WarehouseController warehouse) {
        this.parentWarehouse = warehouse;

        /* HACK: Initialise the workstationComboBox here too; this guarantees warehouse isn't null when it's filled. */
        if (this.parentWarehouse != null) {
            List<String> workstationNames = new ArrayList<>();
            for (int i = 0; i < parentWarehouse.getAllWorkstationsInRAM().size(); ++i) {
                workstationNames.add(parentWarehouse.getAllWorkstationsInRAM().get(i).getWorkstationName());
            }
            workstationComboBox.setItems(FXCollections.observableArrayList(workstationNames));
        }
    }

    // This function loads a new FXML page when called, complete with an error handler.
    private void loadNewPage(String fxmlFilePath) {
        try {
            //System.out.println("Loading FXML: " + fxmlFilePath);  // Debugging line
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            Parent root = loader.load();

            //Transfers productID to create test controller page
            //CreateTestRecordController createTestRecordController = fxmlLoader.getController();
            //String productIdValue = productIdField.getText();
            //createTestRecordController.setProductId(productIdValue);

            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/WorkStation.fxml"));
            WorkStationController workStationController = loader.getController();
            LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();
            //System.out.println("This is workstation alias" + workstationComboBox.getValue());
            int workstationId = locationsAndContentsDAO.getLocationIDFromAlias(workstationComboBox.getValue());
            workStationController.setWorkStationId(workstationId);
            //System.out.println("This is ws id " + workstationId);


            // Get the current stage (window) and set the new scene
            Stage stage = (Stage) workstationComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log the error if FXML loading fails
            System.out.println("Failed to load FXML file: " + fxmlFilePath);
        }
    }

    public void goToWorkstationPage() {
        Stage stage = (Stage) allocateButton.getScene().getWindow();
        stage.hide();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/view-workstation.fxml"));

        try {
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();

            Parent createAllocateWSRoot = fxmlLoader.load();

//            WorkStationController workStationController = fxmlLoader.getController();
//            LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();
//            //System.out.println("This is workstation alias" + workstationComboBox.getValue());
//            int workstationId = locationsAndContentsDAO.getLocationIDFromAlias(workstationComboBox.getValue());
//            workStationController.setWorkStationId(workstationId);
//            System.out.println("This is ws id " + workstationId);


            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/WorkStation.fxml"));
            ViewWorkstationController workStationController = fxmlLoader.getController();
            LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();
            //System.out.println("This is workstation alias" + workstationComboBox.getValue());
            int workstationId = locationsAndContentsDAO.getLocationIDFromAlias(workstationComboBox.getValue());
            workStationController.setWorkStationIdV2(workstationId);
            System.out.println("This is ws id " + workstationId);

            Scene scene = new Scene(createAllocateWSRoot, Main.getWidth(), Main.getHeight());
            scene.getStylesheets().add(stylesheet);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }


//        String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
//
//        //Parent createAllocateWSRoot = fxmlLoader.load();
//
//
//        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/WorkStation.fxml"));
//        ViewWorkstationController workStationController = fxmlLoader.getController();
//        LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();
//        //System.out.println("This is workstation alias" + workstationComboBox.getValue());
//        int workstationId = locationsAndContentsDAO.getLocationIDFromAlias(workstationComboBox.getValue());
//        workStationController.setWorkStationIdV2(workstationId);
//        System.out.println("This is ws id " + workstationId);


        //Transfers productID to create test controller page
        //CreateTestRecordController createTestRecordController = fxmlLoader.getController();
        //String productIdValue = productIdField.getText();
        //createTestRecordController.setProductId(productIdValue);

        //Stage stage = (Stage) productIdField.getScene().getWindow();

        //Scene scene = new Scene(createAllocateWSRoot, Main.getWidth(), Main.getHeight());
        //scene.getStylesheets().add(stylesheet);
        //stage.setScene(scene);
        //stage.show();

    }

    /*
    private void loadHomePage() throws IOException {
        // Hide the current stage
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.hide();

        // Load the home page FXML and get the controller
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/main_view.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();

        // Set employee details in the main controller
        UsersDAO usersDAO = new UsersDAO();
        String fullName = fullNameTextField.getText();
        mainController.setEmployeeName(fullName);
        Integer employeeId = LoggedInUserSingleton.getInstance().getEmployeeId();
        mainController.setEmployeeTitle(usersDAO.getUserById(employeeId).getAccessLevel());

        // Set up the new scene and show the stage
        Scene scene = new Scene(root, Main.getWidth(), Main.getHeight());
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/protrack/stylesheet.css")).toExternalForm());
    }

    public void openCreateTestRecordPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/create-test-record-view.fxml"));
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();

            Parent createProductRoot = fxmlLoader.load();

            //Transfers productID to create test controller page
            CreateTestRecordController createTestRecordController = fxmlLoader.getController();
            String productIdValue = productIdField.getText();
            createTestRecordController.setProductId(productIdValue);

            Stage stage = (Stage) productIdField.getScene().getWindow();

            Scene scene = new Scene(createProductRoot, WIDTH, HEIGHT);
            scene.getStylesheets().add(stylesheet);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */


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
