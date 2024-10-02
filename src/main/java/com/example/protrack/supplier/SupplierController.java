package com.example.protrack.supplier;

import com.example.protrack.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class SupplierController {

    @FXML
    private TableView<Supplier> suppliersTableView;

    @FXML
    private TableColumn<Supplier, Integer> supplierIdColumn;

    @FXML
    private TableColumn<Supplier, String> nameColumn;

    @FXML
    private TableColumn<Supplier, String> emailColumn;

    @FXML
    private TableColumn<Supplier, String> phoneNumberColumn;

    @FXML
    private TableColumn<Supplier, String> billingAddressColumn;

    @FXML
    private TableColumn<Supplier, String> shippingAddressColumn;

    @FXML
    private TableColumn<Supplier, Double> leadTimeColumn;

    @FXML
    private Button addSupplierButton;

    private ObservableList<Supplier> supplierList;
    private SupplierDAO supplierDAO;

    // Initialize the controller
    @FXML
    private void initialize() {
        // Set up the TableView columns with the corresponding property values
        supplierIdColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        billingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("billingAddress"));
        shippingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("shippingAddress"));
        leadTimeColumn.setCellValueFactory(new PropertyValueFactory<>("leadTime"));

        // Initialize the ObservableList and set it to the TableView
        supplierList = FXCollections.observableArrayList();
        suppliersTableView.setItems(supplierList);

        // Load and display the initial list of suppliers
        refreshTable();

        // Add functionality to click on a row to edit supplier
        suppliersTableView.setRowFactory(tv -> {
            TableRow<Supplier> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    Supplier selectedSupplier = row.getItem();
                    editSupplierPopup(selectedSupplier);
                }
            });
            return row;
        });
    }

    // Refresh the suppliers table with data from the database
    public void refreshTable() {
        supplierDAO = new SupplierDAO();
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        supplierList.setAll(suppliers);
    }

    // Opens add supplier popup
    @FXML
    private void addSupplierPopup() {
        try {
            // Load the FXML file for the add supplier dialog
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/supplier/add_supplier.fxml"));
            Parent addSupplierRoot = fxmlLoader.load();

            // Set up the popup stage
            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add Supplier");

            // Create the scene and apply styles
            Scene scene = new Scene(addSupplierRoot, 350, 345);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);

            // Center the popup window on the screen
            Bounds rootBounds = addSupplierButton.getScene().getRoot().getLayoutBounds();
            popupStage.setY(rootBounds.getCenterY() - 100);
            popupStage.setX(rootBounds.getCenterX());

            // Show the popup window
            popupStage.showAndWait();

            // Refresh the table after adding a supplier
            refreshTable();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Open the edit supplier popup
    public void editSupplierPopup(Supplier supplier) {
        try {
            // Load the FXML file for the edit supplier dialog
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/supplier/edit_supplier.fxml"));
            Parent editSupplierRoot = fxmlLoader.load();

            // Get the controller of the edit supplier FXML
            EditSupplierController editSupplierController = fxmlLoader.getController();
            editSupplierController.setSupplier(supplier); // Pass the supplier object

            // Set up the popup stage
            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Edit Supplier");

            // Create the scene and apply styles
            Scene scene = new Scene(editSupplierRoot, 350, 365);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);

            // Center the popup window on the screen
            Bounds rootBounds = addSupplierButton.getScene().getRoot().getLayoutBounds();
            popupStage.setY(rootBounds.getCenterY() - 100);
            popupStage.setX(rootBounds.getCenterX());

            // Show the popup window
            popupStage.showAndWait();

            // Refresh the table after editing a supplier
            refreshTable();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
