package com.example.protrack.applicationpages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

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
        loadContent("/org/example/dashboard.fxml", "/org/example/main_view");
    }

    @FXML
    private void products() {
        loadContent("/org/example/products.fxml", "/org/example/main_view");
    }

    @FXML
    private void parts() {
        loadContent("/org/example/parts.fxml", "/org/example/main_view");
    }

    @FXML
    private void workOrders() {
        loadContent("/profile/profile_work_orders.fxml", "/org/example/main_view");
    }

    @FXML
    private void purchaseOrders() {
        loadContent("/org/example/purchase_orders.fxml", "/org/example/main_view");
    }

    @FXML
    private void customers() {
        loadContent("/org/example/customers.fxml", "/org/example/main_view");
    }

    @FXML
    private void suppliers() {
        loadContent("/org/example/suppliers.fxml", "/org/example/main_view");
    }

    @FXML
    private void employees() {
        loadContent("/org/example/employees.fxml", "/org/example/main_view");
    }

    @FXML
    private void myProfile() {
        loadContent("/profile/my_profile.fxml", "/profile/my_profile");
    }

    @FXML
    private void warehouse() {
        loadContent("/org/example/warehouse.fxml", "/org/example/main_view");
    }

    private void loadContent(String fxmlFile, String stylesheet) {
        try {
            Scene scene = dynamicVBox.getScene();
            dynamicVBox.getChildren().clear(); // Clears existing content
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent content = loader.load();
            dynamicVBox.getChildren().add(content);
            scene.getStylesheets().clear();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/org/example/main_view")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(stylesheet)).toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }
}