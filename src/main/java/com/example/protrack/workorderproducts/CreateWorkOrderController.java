package com.example.protrack.workorderproducts;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.productbuild.ProductBuild;
import com.example.protrack.productbuild.ProductBuildDAO;
import com.example.protrack.productorders.ProductOrder;
import com.example.protrack.productorders.ProductOrderDAO;
import com.example.protrack.products.BillOfMaterials;
import com.example.protrack.products.BillOfMaterialsDAO;
import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

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
    private ComboBox shippingMethodCombo;

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
    private TableView<WorkOrderProduct> workOrderTableView;

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

    /**
     * Initialises the controller. Sets up the TableView and populates the ComboBoxes
     * with data from the database. Binds the createWorkOrderButton's disable property
     * to the state of required fields.
     */
    public void initialize() {

        // Update the TableView
        colWorkOrderProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colWorkOrderProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colWorkOrderProductQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colWorkOrderProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colWorkOrderProductTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Set cell factory for the 'colTrash' column to display a bin icon
        colTrash.setCellFactory(new Callback<TableColumn<WorkOrderProduct, String>, TableCell<WorkOrderProduct, String>>() {
            @Override
            public TableCell<WorkOrderProduct, String> call(TableColumn<WorkOrderProduct, String> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button(" \uD83D\uDDD1 "); // Trash icon as button

                    {
                        deleteButton.getStyleClass().add("trash-button");
                        // Handles row deletion
                        deleteButton.setOnAction(event -> {
                            WorkOrderProduct product = getTableView().getItems().get(getIndex());
                            getTableView().getItems().remove(product); // Remove from table
                            updateTotalPrice(); // Update total after deletion
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : deleteButton); // Set the button to trash icon if there is an active row
                    }
                };
            }
        });

        // Initialize ObservableList
        workOrderProducts = FXCollections.observableArrayList();
        workOrderTableView.setItems(workOrderProducts);

        // Populate the ComboBoxes with data from the database
        workOrderOwnerComboBox.getItems().setAll(new UsersDAO().getProductionUsers());
        customerComboBox.getItems().setAll(new CustomerDAOImplementation().getAllCustomers());
        productComboBox.getItems().setAll(new ProductDAO().getAllProducts());
        shippingMethodCombo.getItems().addAll("Priority", "International", "Economy");

        // Create a binding to check if any essential field is empty
        BooleanBinding emptyFields = Bindings.createBooleanBinding(() ->
                        addressField.getText().trim().isEmpty() ||
                                emailField.getText().trim().isEmpty() ||
                                shippingAddressField.getText().trim().isEmpty() ||
                                workOrderTableView.getItems().isEmpty() ||
                                customerComboBox.getSelectionModel().isEmpty(),
                addressField.textProperty(), emailField.textProperty(),
                shippingAddressField.textProperty(), workOrderTableView.getItems()
        );

        createWorkOrderButton.disableProperty().bind(emptyFields);
    }

    /**
     * Populates customer-related fields when a customer is selected from the customer combo box
     */
    @FXML
    private void populateCustomerFields() {
        Customer selectedCustomer = customerComboBox.getValue();
        if (selectedCustomer != null) {
            addressField.setText(selectedCustomer.getBillingAddress());
            emailField.setText(selectedCustomer.getEmail());
            phoneField.setText(selectedCustomer.getPhoneNumber());
            shippingAddressField.setText(selectedCustomer.getShippingAddress());
        } else {
            addressField.clear();
            emailField.clear();
            phoneField.clear();
            shippingAddressField.clear();
        }
    }

    /**
     * Adds the selected product from the productComboBox to the work order table.
     * Updates the total price accordingly. Handles invalid input gracefully.
     */
    @FXML
    protected void addProductToTable() {
        try {
            // Get the selected product from the 'Product' ComboBox
            Product selectedProduct = productComboBox.getSelectionModel().getSelectedItem();
            int quantity = Integer.parseInt(productQuantityField.getText());

            // Check if the product is already in the table
            for (WorkOrderProduct workOrderProduct : workOrderProducts) {
                if (workOrderProduct.getProductId() == selectedProduct.getProductId()) {
                    // If the product exists, update the quantity
                    workOrderProduct.setQuantity(workOrderProduct.getQuantity() + quantity);
                    workOrderProduct.setPrice(calculateTotalPrice(selectedProduct.getProductId())); // Price based on BOM
                    updateTotalPrice(); // Update displayed total price
                    workOrderTableView.refresh(); // Refresh the TableView
                    clearProductInputFields(); // Clear input fields
                    return; // Exit the method
                }
            }

            // Create a new WorkOrderProduct if the product is not in the list
            double totalPrice = calculateTotalPrice(selectedProduct.getProductId());
            WorkOrderProduct workOrderProduct = new WorkOrderProduct(
                    0, // Placeholder workOrderProductId, will be set when the work order is saved
                    0, // Placeholder value for workOrderId, will be updated when saving
                    selectedProduct.getProductId(),
                    selectedProduct.getProductName(),
                    quantity,
                    totalPrice // Use the calculated price
            );

            // Add WorkOrderProduct to the list
            workOrderProducts.add(workOrderProduct);
            updateTotalPrice(); // Update the total price label
            clearProductInputFields(); // Reset the product selection and quantity fields

        } catch (NumberFormatException e) {
            // Handle invalid number formats for quantity
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText("Invalid Product Entry");
            alert.setContentText("Ensure product selected with a valid quantity.");
            alert.setGraphic(null);

            ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
            alert.getButtonTypes().setAll(confirmBtn);
            Button confirmButton = (Button) alert.getDialogPane().lookupButton(confirmBtn);
            confirmButton.setStyle("-fx-background-color: #390b91; -fx-text-fill: white; -fx-style: bold;");

            alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/protrack/stylesheet.css").toExternalForm());

            alert.showAndWait();
        }
    }

    /**
     * Calculates the total price for a given product ID based on the Bill of Materials (BOM).
     *
     * @param productId The ID of the product to calculate the total price for.
     * @return The calculated total price for the product.
     */
    private double calculateTotalPrice(int productId) {
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

    /**
     * Updates the total price label to reflect the sum of all work order products.
     */
    private void updateTotalPrice() {
        double totalOrderPrice = workOrderProducts.stream()
                .mapToDouble(WorkOrderProduct::getTotal)
                .sum();
        totalLabel.setText(String.format("%.2f", totalOrderPrice));
    }

    /**
     * Clears the product input fields in the form.
     */
    private void clearProductInputFields() {
        productComboBox.getSelectionModel().clearSelection();
        productQuantityField.clear();
    }

    /**
     * Creates a new work order using the information provided in the form fields.
     * Saves the work order and its associated products to the database.
     */
    @FXML
    protected void createWorkOrder() {
        try {
            // Fetch data from form fields
            ProductionUser orderOwner = workOrderOwnerComboBox.getSelectionModel().getSelectedItem();
            Customer customer = customerComboBox.getSelectionModel().getSelectedItem();
            String shippingAddress = shippingAddressField.getText();
            LocalDateTime orderDate = LocalDateTime.now();
            LocalDateTime deliveryDate = orderDate.plusDays(7); // estimated delivery

            // Create a new WorkOrder instance
            WorkOrder workOrder = new WorkOrder(0, orderOwner, customer, orderDate, deliveryDate, shippingAddress, "Pending", 0.0);

            List<ProductionUser> productionUsers = new UsersDAO().getProductionUsers();
            List<Customer> customers = new CustomerDAOImplementation().getAllCustomers();
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
            int workOrderId = newWorkOrder.getWorkOrderId(); // Get the new WorkOrder ID

            // Now create and save WorkOrderProduct instances with the correct WorkOrder ID
            for (WorkOrderProduct product : workOrderProducts) {

                //PB
                //Generates new Product Order
                ProductOrderDAO productOrderDAO = new ProductOrderDAO();
                int productOrderId = (productOrderDAO.getAllProductOrder().toArray()).length + 1;
                productOrderDAO.newProductOrder(new ProductOrder( productOrderId,
                        product.getProductId(),
                        product.getQuantity(),
                        workOrderId));

                //Generates new Product Builds

                for (int i = 0; i < product.getQuantity(); i++) {
                    ProductBuildDAO productBuildDAO = new ProductBuildDAO();
                    int buildId = (productBuildDAO.getAllProductBuilds().toArray()).length + 1;
                    productBuildDAO.newProductBuild(new ProductBuild(buildId, productOrderId, 0.00f, product.getProductId()));
                    System.out.println("In WO to build stuff for " + buildId + " for " + product.getProductId()
                            + " x" + product.getQuantity() + " " + productOrderId);
                }

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

            ((Stage) closePopupButton.getScene().getWindow()).close();

        } catch (NumberFormatException e) {
            System.out.println("Invalid input: Please ensure all fields are filled correctly.");
        }
    }

    /**
     * Closes the work order creation popup.
     */
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
        addressField.clear();
        emailField.clear();
        phoneField.clear();
        shippingAddressField.clear();
        shippingMethodCombo.getPromptText();
        workOrderOwnerComboBox.getSelectionModel().clearSelection();
        customerComboBox.getSelectionModel().clearSelection();
        productComboBox.getSelectionModel().clearSelection();
        productQuantityField.clear();
        workOrderProducts.clear(); // Clear the products from the table
        updateTotalPrice(); // Update total price to zero
    }
}
