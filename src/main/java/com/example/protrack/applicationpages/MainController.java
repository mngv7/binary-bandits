package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.warehouseutil.LocationsAndContentsDAO;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.utility.LoggedInUserSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class MainController {

    @FXML
    private Label employeeName;

    @FXML
    private Label employeeTitle;

    @FXML
    private Label timesheet;

    @FXML
    private VBox dynamicVBox;

    @FXML
    private VBox inventoryVBox;

    @FXML
    private void inventory() {
        if (!(inventoryVBox.isManaged())) {
            inventoryVBox.setManaged(true);
            inventoryVBox.setVisible(true);
            inventoryVBox.setPadding(new Insets(0, 0, 0, 20));
        } else {
            inventoryVBox.setManaged(false);
            inventoryVBox.setVisible(false);
        }
    }

    @FXML
    private void dashboard() {
        loadContent("/com/example/protrack/dashboard.fxml");
    }

    @FXML
    private void products() {
        loadContent("/com/example/protrack/products.fxml");
    }

    @FXML
    private void parts() {
        loadContent("/com/example/protrack/Parts/parts.fxml");
    }

    @FXML
    private void purchaseOrders() {
        loadContent("/workorder/work_orders.fxml");
    }

    @FXML
    private void customers() {
        loadContent("/customer/customers.fxml");
    }

    @FXML
    private void suppliers() {
        loadContent("/supplier/suppliers.fxml");
    }

    @FXML
    public void employees() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/employees.fxml"));
            Parent content = loader.load();

            // Get the EmployeesController and inject MainController
            EmployeesController employeesController = loader.getController();
            employeesController.setMainController(this);  // Pass the current MainController instance

            dynamicVBox.getChildren().clear();  // Clear existing content
            dynamicVBox.getChildren().add(content);  // Add new content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void employeesExpanded() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/expanded-employee-view.fxml"));
            Parent content = loader.load();

            // Get the controller and inject MainController
            ExpandedEmployeeController expandedController = loader.getController();
            expandedController.setMainController(this);  // Inject the current MainController instance

            dynamicVBox.getChildren().clear();
            dynamicVBox.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String TimesheetTITLE = "Add Timesheets";
    private static final int TimesheetWIDTH = 500;
    private static final int TimesheetHEIGHT = 500;

    @FXML
    private void myTimesheets() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/timesheets.fxml"));
            Parent addPartsRoot = fxmlLoader.load();
            TimesheetsController timesheetsController = fxmlLoader.getController();

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(TimesheetTITLE);

            // Set employee details in the main controller
            UsersDAO usersDAO = new UsersDAO();
            Integer employeeId = LoggedInUserSingleton.getInstance().getEmployeeId();
            timesheetsController.setEmployeeId(usersDAO.getUserById(employeeId).getEmployeeId().toString());

            // Set the scene for the pop-up
            Scene scene = new Scene(addPartsRoot, TimesheetWIDTH, TimesheetHEIGHT);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void myProfile() {
        loadContent("/profile/my_profile.fxml");
    }

    @FXML
    private void warehouse() {
        try {
            Scene scene = dynamicVBox.getScene();
            dynamicVBox.getChildren().clear(); // Clears existing content
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/warehouse.fxml"));
            Parent content = loader.load();

            WarehouseController controller = loader.getController();
            if (controller != null) {
                controller.setMainControllerInstance(this);
            } else {
                System.out.println("WARN: No main controller assigned to warehouse page.");
            }

            dynamicVBox.getChildren().add(content);
            //scene.getStylesheets().add(getClass().getResource("/com/example/protrack/stylesheet.css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    public void loadWarehouseFromOtherPage () {
        warehouse();
    }

    public void loadWorkstationContent(String workstationAlias) {
        Scene scene = dynamicVBox.getScene();
        dynamicVBox.getChildren().clear();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/view-workstation2.fxml"));
        String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
        Parent createAllocateWSRoot = null;
        try {
            createAllocateWSRoot = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Failed to load Workstation content: " + e.getMessage());
            return; /* Don't continue loading. */
        }

        ViewWorkstation2 workStationController = fxmlLoader.getController();
        workStationController.setMainController(this);
        LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();
        int workstationId = locationsAndContentsDAO.getLocationIDFromAlias(workstationAlias);
        workStationController.setWorkStationIdV3(workstationId);

        dynamicVBox.getChildren().add(createAllocateWSRoot);
    }

    private void loadContent(String fxmlFile) {
        try {
            Scene scene = dynamicVBox.getScene();
            dynamicVBox.getChildren().clear(); // Clears existing content
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent content = loader.load();
            dynamicVBox.getChildren().add(content);
            //scene.getStylesheets().add(getClass().getResource("/com/example/protrack/stylesheet.css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName.setText(employeeName);
    }

    public void setEmployeeTitle(String employeeTitle) {
        this.employeeTitle.setText("Access Level: " + employeeTitle);
    }
}