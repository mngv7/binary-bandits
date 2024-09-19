package com.example.protrack.workorder;

import com.example.protrack.Main;
import com.example.protrack.workorder.WorkOrderProduct;
import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class CreateWorkOrderController {

    @FXML
    public Label totalLabel;

    @FXML
    private Button createWorkOrderButton;

    @FXML
    private Button closePopupButton;

    @FXML
    private TextField addressField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField shippingMethodField;

    @FXML
    private TextField shippingAddressField;

    @FXML
    private ComboBox<ProductionUser> workOrderOwnerComboBox;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private ComboBox<Product> productComboBox;

    @FXML
    private TextField productQuantityField;

    @FXML
    private TableView workOrderTableView;

    @FXML
    private TableColumn<WorkOrderProduct, String> colWorkOrderProductName;

    @FXML
    private TableColumn<WorkOrderProduct, String> colWorkOrderProductDescription;

    @FXML
    private TableColumn<WorkOrderProduct, Integer> colWorkOrderProductQuantity;

    @FXML
    private TableColumn<WorkOrderProduct, Double> colWorkOrderProductPrice;

    @FXML
    private TableColumn<WorkOrderProduct, Double> colWorkOrderTotal;

    @FXML
    private TableColumn<WorkOrderProduct, String> colTrash;

    private ObservableList<WorkOrderProduct> workOrderProducts;

    public void initialize() {

        // Update the TableView
        colWorkOrderProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colWorkOrderProductDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colWorkOrderProductQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colWorkOrderProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Initialises ObservableList
        workOrderProducts = FXCollections.observableArrayList();
        workOrderTableView.setItems(workOrderProducts);

        // Populate the ComboBoxes with data from the database
        workOrderOwnerComboBox.getItems().setAll(new UsersDAO().getProductionUsers());
        customerComboBox.getItems().setAll(new CustomerDAO().getAllCustomers());
        productComboBox.getItems().setAll(new ProductDAO().getAllProducts());

        // Create a binding to check if any essential field is empty
        BooleanBinding emptyFields = Bindings.createBooleanBinding(() ->

                                addressField.getText().trim().isEmpty() ||
                                emailField.getText().trim().isEmpty() ||
                                shippingAddressField.getText().trim().isEmpty() ||
                                shippingMethodField.getText().trim().isEmpty() ||
                                workOrderOwnerComboBox.getSelectionModel().isEmpty() ||
                                customerComboBox.getSelectionModel().isEmpty() ||
                                productComboBox.getSelectionModel().isEmpty(),
                addressField.textProperty(), emailField.textProperty(),
                shippingAddressField.textProperty(), shippingMethodField.textProperty()
        );

        // Disable the button if any required field is empty
        createWorkOrderButton.disableProperty().bind(emptyFields);
    }

    @FXML
    protected void addProductToTable() {
        try {
            // Get the selected product from the ComboBox
            Product selectedProduct = productComboBox.getSelectionModel().getSelectedItem();

            // Get the quantity entered by the user
            int quantity = Integer.parseInt(productQuantityField.getText());

            // Calculate the total price for the selected product
        //       double totalPriceForProduct = selectedProduct.getPrice() * quantity;

            WorkOrderProduct workOrderProduct = new WorkOrderProduct(
                    selectedProduct.getProductId(),
                    selectedProduct.getProductName(),
                    "testDescription", //desc
                    quantity,
                    6.9, //price
                    12.2 //totalprice
            );

            // Add the WorkOrderProduct to the list
            workOrderProducts.add(workOrderProduct);
            System.out.println(workOrderProducts.toString());

            workOrderTableView.setItems(workOrderProducts);
            System.out.println(workOrderTableView.getItems().toString());

            // Update the total price label
            // totalOrderPrice += totalPriceForProduct;
            totalLabel.setText(String.format("%.2f", 12.2));

            // Optionally, reset the product selection and quantity fields
            productComboBox.getSelectionModel().clearSelection();
            productQuantityField.clear();
        } catch (NumberFormatException e) {
            // Handle invalid number format for quantity
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Quantity");
            alert.setContentText("Please enter a valid number for quantity.");
            alert.showAndWait();
        }
    }

    @FXML
    protected void createWorkOrder() {
        // Create WorkOrder and associate products
        try {
            // Fetch data from form fields
            ProductionUser orderOwner = workOrderOwnerComboBox.getSelectionModel().getSelectedItem();
            Customer customer = customerComboBox.getSelectionModel().getSelectedItem();
            String shippingAddress = shippingAddressField.getText();
            String shippingMethod = shippingMethodField.getText();
            LocalDateTime orderDate = LocalDateTime.now();
            LocalDateTime deliveryDate = orderDate.plusDays(7);  // Example logic for delivery date

            // Create a new WorkOrder instance
            WorkOrder workOrder = new WorkOrder(0, orderOwner, customer, orderDate, deliveryDate, shippingAddress, null, "Pending", 0.0);

            // Fetch product details and quantity
            Product selectedProduct = productComboBox.getSelectionModel().getSelectedItem();
            int quantity = Integer.parseInt(productQuantityField.getText());

            // Add the product to the work order
            WorkOrderProductsDAO workOrderProductsDAO = new WorkOrderProductsDAO();
            workOrderProductsDAO.addWorkOrderProduct(workOrder, selectedProduct, quantity);

            // Optionally, reset the form fields after creation
            clearFormFields();

        } catch (NumberFormatException e) {
            System.out.println("Invalid input: Please ensure all fields are filled correctly.");
        }
    }

    @FXML
    protected void onClosePopupButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Cancel Work Order Creation");
        alert.setContentText("Are you sure you want to cancel?");
        alert.setGraphic(null);

        // Define dialog buttons
        ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
        ButtonType backBtn = new ButtonType("Back", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(confirmBtn, backBtn);

        // Show confirmation dialog and close if confirmed
        alert.showAndWait().ifPresent(result -> {
            if (result == confirmBtn) {
                ((Stage) closePopupButton.getScene().getWindow()).close();
            }
        });
    }

    /**
     * Clears all form fields to reset the state after creating a work order.
     */
    private void clearFormFields() {

        addressField.clear();
        emailField.clear();
        phoneField.clear();
        shippingAddressField.clear();
        shippingMethodField.clear();
        workOrderOwnerComboBox.getSelectionModel().clearSelection();
        customerComboBox.getSelectionModel().clearSelection();
        productComboBox.getSelectionModel().clearSelection();
        productQuantityField.clear();
    }
}