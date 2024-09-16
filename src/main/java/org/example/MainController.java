package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.StringProperty;
import javafx.collections.transformation.FilteredList;

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
        loadContent("dashboard.fxml");
    }

    @FXML
    private void products() {
        loadContent("products.fxml");
    }

    @FXML
    private void parts() {
        loadContent("parts.fxml");
    }

    @FXML
    private void workOrders() {
        loadContent("work_orders.fxml");
    }

    @FXML
    private void purchaseOrders() {
        loadContent("work_orders.fxml");
    }

    @FXML
    private void customers() {
        loadContent("customers.fxml");
    }

    @FXML
    private void suppliers() {
        loadContent("suppliers.fxml");
    }

    @FXML
    private void employees() {
        loadContent("employees.fxml");
    }

    @FXML
    private void warehouse() {
        loadContent("warehouse.fxml");
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
}