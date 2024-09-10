package com.example.protrack.applicationpages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.io.IOException;

public class MainController {

    @FXML
    private Label employeeName;

    @FXML
    private Label employeeTitle;

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
        loadContent("/com/example/protrack/parts.fxml");
    }

    @FXML
    private void purchaseOrders() {
        loadContent("/com/example/protrack/purchase_orders.fxml");
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
    private void employees() {
        loadContent("/com/example/protrack/employees.fxml");
    }

    @FXML
    private void warehouse() {
        loadContent("/com/example/protrack/warehouse.fxml");
    }

    private void loadContent(String fxmlFile) {
        try {
            dynamicVBox.getChildren().clear(); // Clears existing content
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent content = loader.load();
            dynamicVBox.getChildren().add(content);
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