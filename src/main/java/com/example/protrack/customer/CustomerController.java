package com.example.protrack.customer;

import com.example.protrack.Main;
import com.example.protrack.observers.CustomersTableSubject;
import com.example.protrack.observers.Observer;
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

/**
 * The {@code CustomerController} class manages the customer data display and interactions within the application.
 * It implements the Observer pattern to update the customer data from the subject and to handle
 * customer-related actions of adding and editing customer information.
 */
public class CustomerController implements Observer {

    @FXML
    private Button addCustomerButton;
    @FXML
    private TableView<Customer> customersTableView;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> firstNameColumn;
    @FXML
    private TableColumn<Customer, String> lastNameColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> phoneNumberColumn;
    @FXML
    private TableColumn<Customer, String> billingAddressColumn;
    @FXML
    private TableColumn<Customer, String> shippingAddressColumn;
    @FXML
    private TableColumn<Customer, String> statusColumn;

    private ObservableList<Customer> customers;

    private CustomersTableSubject subject;

    /**
     * Initializes the CustomerController, setting up the observable list for customers,
     * configuring table columns, sets row click event for table rows, and registers the observer
     * with the CustomersTableSubject. Data is synchronised from the database.
     */
    @FXML
    public void initialize() {
        subject = new CustomersTableSubject();
        subject.registerObserver(this);

        customers = FXCollections.observableArrayList();

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        billingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("billingAddress"));
        shippingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("shippingAddress"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        customersTableView.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Customer selectedCustomer = row.getItem();
                    editCustomerPopup(selectedCustomer);
                }
            });
            return row;
        });

        subject.syncDataFromDB();
        subject.notifyObservers();

        Platform.runLater(() -> {
            Window window = addCustomerButton.getScene().getWindow();
            if (window instanceof Stage stage) {
                stage.setOnCloseRequest(event -> {
                    subject.deregisterObserver(this);
                });
            }
        });
    }

    /**
     * Updates the customer table view with the updated data from CustomersTableSubject.
     */
    public void update() {
        customers.clear();
        customers.setAll(subject.getData());
        customersTableView.setItems(customers);
    }

    /**
     * Opens a popup window to create a new customer.
     */
    public void addCustomerPopup() {
        try {
            // Load the FXML file for the create customer dialog
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/customer/add_customer.fxml"));
            Parent createCustomerRoot = fxmlLoader.load();

            // Set up the popup stage
            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add Customer");

            // Create the scene and apply styles
            Scene scene = new Scene(createCustomerRoot, 350, 380);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);

            // Show the popup window
            popupStage.showAndWait();
            subject.syncDataFromDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a popup window to edit the details of an existing customer.
     *
     * @param customer The customer object to be edited.
     */
    public void editCustomerPopup(Customer customer) {
        try {
            // Load the FXML file for the edit customer dialog
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/customer/edit_customer.fxml"));
            Parent editCustomerRoot = fxmlLoader.load();

            // Get the controller of the edit customer FXML
            EditCustomerController editCustomerController = fxmlLoader.getController();
            editCustomerController.setCustomer(customer); // Pass the customer object

            // Set up the popup stage
            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Edit Customer");

            // Create the scene and apply styles
            Scene scene = new Scene(editCustomerRoot, 350, 400);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);

            // Show the popup window
            popupStage.showAndWait();
            subject.syncDataFromDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
