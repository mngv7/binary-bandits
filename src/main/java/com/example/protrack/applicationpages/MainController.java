package com.example.protrack.applicationpages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

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
        loadContent("/com/example/protrack/customers.fxml");
    }

    @FXML
    private void suppliers() {
        loadContent("/com/example/protrack/suppliers.fxml");
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

    @FXML
    private void myTimesheets() { loadContent("/timesheets.fxml");}

    @FXML
    private void myProfile() {
        loadContent("/profile/my_profile.fxml");
    }

    @FXML
    private void warehouse() {
        loadContent("/com/example/protrack/warehouse.fxml");
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