package com.example.protrack.workorder;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import com.example.protrack.workorderproducts.WorkOrderProductsDAOImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EditWorkOrderController {

    @FXML
    private Button closePopupButton;

    @FXML
    private Label workOrderIdLabel;

    @FXML
    private ComboBox<ProductionUser> orderOwnerComboBox;

    @FXML
    private ComboBox<Customer> customerComboBox;

    @FXML
    private DatePicker orderDatePicker;

    @FXML
    private DatePicker deliveryDatePicker;

    @FXML
    private TextField shippingAddressField;

    @FXML
    private TextField statusField;

    @FXML
    private Label subtotalLabel;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label orderStatusLabel;

    @FXML
    private Label shippingAddressLabel;

    @FXML
    private Button editButton;

    @FXML
    private Label orderOwnerLabel;

    @FXML
    private Label customerLabel;

    @FXML
    private Label orderDateLabel;

    @FXML
    private Label deliveryDateLabel;

    private boolean edit = false;

    private WorkOrder workOrder;
    private WorkOrdersDAOImplementation workOrdersDAO;

    private ObservableList<ProductionUser> usersList;
    private ObservableList<Customer> customerList;

    // Fields to store original values
    private ProductionUser originalOrderOwner;
    private Customer originalCustomer;
    private LocalDate originalOrderDate;
    private LocalDate originalDeliveryDate;
    private String originalShippingAddress;
    private String originalStatus;

    @FXML
    private TableView<WorkOrderProduct> workOrderProductsTableView;

    @FXML
    private TableColumn<WorkOrderProduct, String> colProductId;
    @FXML
    private TableColumn<WorkOrderProduct, String> colProductName;
    @FXML
    private TableColumn<WorkOrderProduct, Integer> colQuantity;
    @FXML
    private TableColumn<WorkOrderProduct, Double> colPrice;
    @FXML
    private TableColumn<WorkOrderProduct, Double> colTotal;

    private ObservableList<WorkOrderProduct> workOrderProducts;

    public void initialize() {
        usersList = FXCollections.observableArrayList(new UsersDAO().getProductionUsers());
        customerList = FXCollections.observableArrayList(new CustomerDAOImplementation().getAllCustomers());

        orderOwnerComboBox.setItems(usersList);
        customerComboBox.setItems(customerList);

        // Set up columns
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Initialize the ObservableList and bind it to the TableView
        workOrderProducts = FXCollections.observableArrayList();
        workOrderProductsTableView.setItems(workOrderProducts);

        // Hide editable fields initially
        orderOwnerComboBox.setVisible(false);
        customerComboBox.setVisible(false);
        orderDatePicker.setVisible(false);
        deliveryDatePicker.setVisible(false);
        shippingAddressField.setVisible(false);
        statusField.setVisible(false);
        statusField.setEditable(false);
        saveButton.setVisible(false);
        statusField.setEditable(false);

        // Set managed properties
        orderOwnerComboBox.managedProperty().bind(orderOwnerComboBox.visibleProperty());
        customerComboBox.managedProperty().bind(customerComboBox.visibleProperty());
        orderDatePicker.managedProperty().bind(orderDatePicker.visibleProperty());
        deliveryDatePicker.managedProperty().bind(deliveryDatePicker.visibleProperty());
        saveButton.managedProperty().bind(saveButton.visibleProperty());
    }

    private void loadWorkOrderProducts(int workOrderId) {
        // Fetch work order products from the database
        WorkOrderProductsDAOImplementation workOrderProductsDAO = new WorkOrderProductsDAOImplementation();
        List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(workOrderId);
        System.out.println(products.toString());
        // Populate the ObservableList
        workOrderProducts.clear(); // Clear existing products in case of reloading
        workOrderProducts.addAll(products);
    }

    @FXML
    private void toggleEdit() {
        edit = !edit;

        if (edit) {
            // Switch to Edit Mode
            editButton.setText("Cancel");
            saveButton.setVisible(true);

            orderOwnerComboBox.setVisible(true);
            customerComboBox.setVisible(true);
            orderDatePicker.setVisible(true);
            deliveryDatePicker.setVisible(true);
            shippingAddressField.setVisible(true);
            shippingAddressField.setEditable(true);
            statusField.setVisible(true);
            statusField.setEditable(true);

            orderOwnerLabel.setVisible(false);
            customerLabel.setVisible(false);
            orderDateLabel.setVisible(false);
            deliveryDateLabel.setVisible(false);
            shippingAddressLabel.setVisible(false);
            orderStatusLabel.setVisible(false);

            shippingAddressField.setEditable(true);
            statusField.setEditable(true);

            setOriginalValues();
        } else {
            // Switch to View Mode and reset fields
            resetFieldsToOriginal();
        }
    }

    private void setOriginalValues() {
        originalOrderOwner = workOrder.getOrderOwner();
        originalCustomer = workOrder.getCustomer();
        originalOrderDate = workOrder.getOrderDate().toLocalDate();
        originalDeliveryDate = workOrder.getDeliveryDate() != null ? workOrder.getDeliveryDate().toLocalDate() : null;
        originalShippingAddress = workOrder.getShippingAddress();
        originalStatus = workOrder.getStatus();
    }

    private void resetFieldsToOriginal() {
        editButton.setText("Edit");
        saveButton.setVisible(false);

        orderOwnerComboBox.setVisible(false);
        customerComboBox.setVisible(false);
        orderDatePicker.setVisible(false);
        deliveryDatePicker.setVisible(false);
        shippingAddressField.setVisible(false);
        shippingAddressField.setEditable(false);
        statusField.setVisible(false);
        statusField.setEditable(false);

        orderOwnerLabel.setVisible(true);
        customerLabel.setVisible(true);
        orderDateLabel.setVisible(true);
        deliveryDateLabel.setVisible(true);
        shippingAddressLabel.setVisible(true);
        orderStatusLabel.setVisible(true);

        orderOwnerComboBox.setValue(originalOrderOwner);
        customerComboBox.setValue(originalCustomer);
        orderDatePicker.setValue(originalOrderDate);
        deliveryDatePicker.setValue(originalDeliveryDate);
        shippingAddressField.setText(originalShippingAddress);
        statusField.setText(originalStatus);


        if (originalOrderOwner != null) {
            orderOwnerLabel.setText(originalOrderOwner.toString());
        } else {
            orderOwnerLabel.setText("No order owner");
        }
        customerLabel.setText(originalCustomer.getFirstName() + " " + originalCustomer.getLastName());
        orderDateLabel.setText(originalOrderDate.toString().substring(0,10));
        if (originalDeliveryDate != null) {
            deliveryDateLabel.setText(originalDeliveryDate.toString());
        } else {
            deliveryDateLabel.setText("No delivery date set");
        }
        shippingAddressLabel.setText(originalShippingAddress);
        orderStatusLabel.setText(originalStatus);
    }

    // setWorkOrder acts as an initialiser as well, as is called through another controller,
    // if called in initialize the workOrder instance cannot be accessed (until after init)
    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;

        // Populate view with work order fields
        workOrderIdLabel.setText(workOrder.getWorkOrderId().toString());
        if (workOrder.getOrderOwner() != null) {
            orderOwnerLabel.setText(workOrder.getOrderOwner().toString());
        }
        customerLabel.setText(workOrder.getCustomer().toString());
        shippingAddressLabel.setText(workOrder.getShippingAddress());
        orderDateLabel.setText(workOrder.getOrderDate().toString().substring(0,10));
        if (workOrder.getDeliveryDate() != null) {
            deliveryDateLabel.setText(workOrder.getDeliveryDate().toString().substring(0,10));
        }
        orderStatusLabel.setText(workOrder.getStatus());
        subtotalLabel.setText(workOrder.getSubtotal().toString());

        // Populate editable fields with current work order data (initially hidden)
        workOrderIdLabel.setText(String.valueOf(workOrder.getWorkOrderId()));
        orderOwnerComboBox.setValue(workOrder.getOrderOwner());
        customerComboBox.setValue(workOrder.getCustomer());
        orderDatePicker.setValue(workOrder.getOrderDate().toLocalDate());
        deliveryDatePicker.setValue(workOrder.getDeliveryDate() != null ? workOrder.getDeliveryDate().toLocalDate() : null);
        shippingAddressField.setText(workOrder.getShippingAddress());
        statusField.setText(workOrder.getStatus());

        // Save the original values to reset if cancel is clicked
        setOriginalValues();

        int workOrderId = workOrder.getWorkOrderId();
        loadWorkOrderProducts(workOrderId);
    }

    public void saveWorkOrder() {
        ProductionUser selectedUser = orderOwnerComboBox.getValue();
        Customer selectedCustomer = customerComboBox.getValue();
        LocalDate orderDate = orderDatePicker.getValue();
        LocalDate deliveryDate = deliveryDatePicker.getValue();
        String shippingAddress = shippingAddressField.getText();
        String status = statusField.getText();
        double subtotal = Double.parseDouble(subtotalLabel.getText());

        if (selectedCustomer == null || orderDate == null || shippingAddress.isEmpty() || status.isEmpty()) {
            System.out.println("Validation failed");
            return;
        }

        workOrder.setOrderOwner(selectedUser);
        workOrder.setCustomer(selectedCustomer);
        workOrder.setOrderDate(LocalDateTime.of(orderDate, workOrder.getOrderDate().toLocalTime()));
        workOrder.setDeliveryDate(deliveryDate != null ? LocalDateTime.of(deliveryDate, workOrder.getDeliveryDate().toLocalTime()) : null);
        workOrder.setShippingAddress(shippingAddress);
        workOrder.setStatus(status);
        workOrder.setSubtotal(subtotal);

        workOrdersDAO = new WorkOrdersDAOImplementation(usersList, customerList);
        workOrdersDAO.updateWorkOrder(workOrder);

        setOriginalValues();
        resetFieldsToOriginal();
    }

    @FXML
    public void deleteWorkOrder() {
        // Add logic to delete the work order from the database
        workOrdersDAO = new WorkOrdersDAOImplementation(usersList, customerList);
        workOrdersDAO.deleteWorkOrder(workOrder.getWorkOrderId());

        // Close the popup after deletion
        closePopup();
    }

    @FXML
    public void closePopup() {
        if (editButton.getText().equals("Cancel")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText("Close Work Order View");
            alert.setContentText("Are you sure you want to cancel editing the work order?");
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
        } else {
            Stage stage = (Stage) editButton.getScene().getWindow();
            stage.close();
        }
    }
}