package com.example.protrack.applicationpages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

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
        loadContent("/org/example/dashboard.fxml");
    }

    @FXML
    private void products() {
        loadContent("/org/example/products.fxml");
    }

    @FXML
    private void parts() {
        loadContent("/org/example/parts.fxml");
    }

    @FXML
    private void workOrders() {
        loadContent("/org/example/work_orders.fxml");
    }

    @FXML
    private void purchaseOrders() {
        loadContent("/org/example/purchase_orders.fxml");
    }

    @FXML
    private void customers() {
        loadContent("/org/example/customers.fxml");
    }

    @FXML
    private void suppliers() {
        loadContent("/org/example/suppliers.fxml");
    }

    @FXML
    private void employees() {
        loadContent("/org/example/employees.fxml");
    }

    @FXML
    private void myProfile() {
        loadContent("/org/example/my_profile.fxml");
    }

    @FXML
    private void warehouse() {
        loadContent("/org/example/warehouse.fxml");
    }

    private void loadContent(String fxmlFile) {
        try {
            Scene scene = dynamicVBox.getScene();
            dynamicVBox.getChildren().clear(); // Clears existing content
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent content = loader.load();
            dynamicVBox.getChildren().add(content);

            scene.getStylesheets().add(getClass().getResource(fxmlFile.substring(0, fxmlFile.length()-5)).toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
}