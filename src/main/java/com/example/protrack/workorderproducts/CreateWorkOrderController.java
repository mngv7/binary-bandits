package com.example.protrack.workorderproducts;

import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.products.*;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;

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
    private TableColumn<WorkOrderProduct, String> colWorkOrderProductId;

    @FXML
    private TableColumn<WorkOrderProduct, String> colWorkOrderProductName;

    @FXML
    private TableColumn<WorkOrderProduct, Integer> colWorkOrderProductQuantity;

    @FXML
    private TableColumn<WorkOrderProduct, Double> colWorkOrderProductPrice;

    @FXML
    private TableColumn<WorkOrderProduct, Double> colWorkOrderProductTotal;

    @FXML
    private TableColumn<WorkOrderProduct, String> colTrash;

    private ObservableList<WorkOrderProduct> workOrderProducts;

    public void initialize() {

        // Update the TableView
        colWorkOrderProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colWorkOrderProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colWorkOrderProductQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colWorkOrderProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colWorkOrderProductTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Sets cell factory for 'colTrash' column to display a bin icon
        colTrash.setCellFactory(new Callback<TableColumn<WorkOrderProduct, String>, TableCell<WorkOrderProduct, String>>() {
            @Override
            public TableCell<WorkOrderProduct, String> call(TableColumn<WorkOrderProduct, String> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("  \uD83D\uDDD1  "  ); // Trash icon as button

                    {
                        deleteButton.getStyleClass().add("trash-button");

                        // Handles row deletion
                        deleteButton.setOnAction(event -> {
                            WorkOrderProduct product = getTableView().getItems().get(getIndex());
                            getTableView().getItems().remove(product);  // Remove from table
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {

                            setGraphic(deleteButton);  // Set the button to trash icon if there is an active row
                        }
                    }
                };
            }
        });

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
                                workOrderTableView.getItems().isEmpty() ||
                                customerComboBox.getSelectionModel().isEmpty(),
                addressField.textProperty(), emailField.textProperty(),
                shippingAddressField.textProperty(), shippingMethodField.textProperty(), workOrderTableView.getItems()
        );

        createWorkOrderButton.disableProperty().bind(emptyFields);
    }

    @FXML
    protected void addProductToTable() {
        try {
            // Get the selected product from the 'Product' ComboBox
            Product selectedProduct = productComboBox.getSelectionModel().getSelectedItem();

            // Get the quantity entered by the user
            int quantity = Integer.parseInt(productQuantityField.getText());

            // Check if the product is already in the table
            for (WorkOrderProduct workOrderProduct : workOrderProducts) {
                if (workOrderProduct.getProductId() == selectedProduct.getProductId()) {
                    // If the product exists, update the quantity
                    workOrderProduct.setQuantity(workOrderProduct.getQuantity() + quantity);
                    // Recalculate the total price based on the updated quantity
                    workOrderProduct.setPrice(calculateTotalPrice(selectedProduct.getProductId())); // Price based on BOM
                    updateTotalPrice();  // Update the displayed total price
                    workOrderTableView.refresh();  // Refresh the TableView
                    clearProductInputFields();  // Clear input fields
                    return;  // Exit the method
                }
            }

            // If the product is not in the list, create a new WorkOrderProduct with workOrderId as a placeholder (0)
            double totalPrice = calculateTotalPrice(selectedProduct.getProductId());
            WorkOrderProduct workOrderProduct = new WorkOrderProduct(
                    0, // Placeholder workOrderProductId, will be set when the work order is saved
                    0, // Placeholder value for workOrderId, this will be updated when saving
                    selectedProduct.getProductId(),
                    selectedProduct.getProductName(),
                    quantity,
                    totalPrice // Use the calculated price
            );

            // Add WorkOrderProduct to the list
            workOrderProducts.add(workOrderProduct);

            // Update the total price label
            updateTotalPrice();

            // Reset the product selection and quantity fields
            clearProductInputFields();

        } catch (NumberFormatException e) {
            // Handle invalid number formats for quantity
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Quantity");
            alert.setContentText("Please enter a valid number for quantity.");
            alert.showAndWait();
        }
    }

    // Method to calculate total price for a given product ID based on BOM
    private double calculateTotalPrice(int productId) {
        // Fetch Bill of Materials for the product
        BillOfMaterialsDAO bomDAO = new BillOfMaterialsDAO();
        List<BillOfMaterials> bomList = bomDAO.getBillOfMaterialsForProduct(productId);

        // Calculate the total cost based on parts
        double totalPrice = 0.0;
        PartsDAO partsDAO = new PartsDAO();

        for (BillOfMaterials bom : bomList) {
            Parts part = partsDAO.getPartById(bom.getPartsId());
            totalPrice += part.getCost() * bom.getRequiredAmount(); // Calculate cost for the required amount of parts
        }

        return totalPrice; // Return the total price for the product based on BOM
    }

    // Method to update the total price label
    private void updateTotalPrice() {
        double totalOrderPrice = workOrderProducts.stream()
                .mapToDouble(WorkOrderProduct::getTotal)
                .sum();
        totalLabel.setText(String.format("%.2f", totalOrderPrice));
    }

    // Method to clear product input fields
    private void clearProductInputFields() {
        productComboBox.getSelectionModel().clearSelection();
        productQuantityField.clear();
    }

    @FXML
    protected void createWorkOrder() {
        try {
            // Fetch data from form fields
            ProductionUser orderOwner = workOrderOwnerComboBox.getSelectionModel().getSelectedItem();
            Customer customer = customerComboBox.getSelectionModel().getSelectedItem();
            String shippingAddress = shippingAddressField.getText();
            String shippingMethod = shippingMethodField.getText();
            LocalDateTime orderDate = LocalDateTime.now();
            LocalDateTime deliveryDate = orderDate.plusDays(7);  // estimated delivery

            // Create a new WorkOrder instance
            WorkOrder workOrder = new WorkOrder(0, orderOwner, customer, orderDate, deliveryDate, shippingAddress, "Pending", 0.0);

            List<ProductionUser> productionUsers = new UsersDAO().getProductionUsers();
            List<Customer> customers = new CustomerDAO().getAllCustomers();

            WorkOrdersDAOImplementation workOrdersDAOImplementation = new WorkOrdersDAOImplementation(productionUsers, customers);

            // Calculate total order price from the products in the TableView
            double totalOrderPrice = workOrderProducts.stream()
                    .mapToDouble(WorkOrderProduct::getTotal)
                    .sum();

            // Set the subtotal of the work order
            workOrder.setSubtotal(Double.parseDouble(String.format("%.2f", totalOrderPrice)));

            // Save the WorkOrder to the database and retrieve the generated ID
            workOrdersDAOImplementation.createWorkOrder(workOrder);
            WorkOrder newWorkOrder = workOrdersDAOImplementation.getWorkOrderByCustomerAndDate(customer, orderDate);
            int workOrderId = newWorkOrder.getWorkOrderId();  // Get the new WorkOrder ID

            // Now create and save WorkOrderProduct instances with the correct WorkOrder ID
            for (WorkOrderProduct product : workOrderProducts) {
                WorkOrderProduct newWorkOrderProduct = new WorkOrderProduct(
                        0,  // workOrderProductId will be auto-generated by the database
                        workOrderId,  // Assign the newly created WorkOrder ID
                        product.getProductId(),
                        product.getProductName(),
                        product.getQuantity(),
                        product.getPrice()
                );
                new WorkOrderProductsDAOImplementation().addWorkOrderProduct(newWorkOrderProduct);
            }

            // Clear the form after saving the work order and products
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

        Button confirmButton = (Button) alert.getDialogPane().lookupButton(confirmBtn);
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(backBtn);

        confirmButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-style: bold;");
        cancelButton.setStyle("-fx-background-color: #ccccff; -fx-text-fill: white; -fx-style: bold");

        // Load the CSS file
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/protrack/stylesheet.css").toExternalForm());

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

        // JavaFX objects to be cleared
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