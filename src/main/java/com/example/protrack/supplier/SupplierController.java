package com.example.protrack.supplier;

import com.example.protrack.Main;
import com.example.protrack.observers.Observer;
import com.example.protrack.observers.SuppliersTableSubject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;

public class SupplierController implements Observer {

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

    private ObservableList<Supplier> suppliers;

    // Reference to the subject
    private SuppliersTableSubject subject;

    // Initialize the controller
    @FXML
    private void initialize() {
        subject = new SuppliersTableSubject();
        // Register this controller as an observer to the subject
        subject.registerObserver(this);

        // Set up the TableView columns with the corresponding property values
        supplierIdColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        billingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("billingAddress"));
        shippingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("shippingAddress"));
        leadTimeColumn.setCellValueFactory(new PropertyValueFactory<>("leadTime"));

        // Initialize the ObservableList and set it to the TableView
        suppliers = FXCollections.observableArrayList();
        suppliersTableView.setItems(suppliers);

        // Load and display the initial list of suppliers
        subject.syncDataFromDB();

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

        subject.syncDataFromDB(); // Fetch data from the database directly
        update();

        Platform.runLater(() -> {
            Window window = addSupplierButton.getScene().getWindow();
            if (window instanceof Stage stage) {
                stage.setOnCloseRequest(event -> {
                    subject.deregisterObserver(this);
                });
            }
        });
    }

    // Refresh the suppliers table with data from the database
    public void update() {
        suppliers.clear();
        suppliers.setAll(subject.getData());
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
            subject.syncDataFromDB();

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
            subject.syncDataFromDB();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
