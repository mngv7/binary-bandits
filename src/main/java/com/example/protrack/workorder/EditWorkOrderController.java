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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class for editing work orders in the application. This class handles the
 * functionality of the Edit Work Order popup, allowing users to modify the details of a selected work order
 */
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

    /**
     * Initializes the controller class, setting up UI components after FXML file loads
     *
     */
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

    /**
     * Loads the work order products associated with a specific work order ID.
     *
     * @param workOrderId the ID of the work order whose products are to be loaded
     */
    private void loadWorkOrderProducts(int workOrderId) {
        // Fetch work order products from the database
        WorkOrderProductsDAOImplementation workOrderProductsDAO = new WorkOrderProductsDAOImplementation();
        List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(workOrderId);
        System.out.println(products.toString());
        // Populate the ObservableList
        workOrderProducts.clear(); // Clear existing products in case of reloading
        workOrderProducts.addAll(products);
    }

    /**
     * Toggles between edit mode and view mode. When in edit mode editable fields are visible.
     * When in view mode fields are reset to their original values.
     */
    @FXML
    private void toggleEdit() {
        edit = !edit;

        if (edit) {
            // Switch to Edit Mode
            editButton.setText("Cancel");
            saveButton.setVisible(true);

            setOriginalValues();
            orderOwnerComboBox.setValue(originalOrderOwner);
            customerComboBox.setValue(originalCustomer);
            orderDatePicker.setValue(originalOrderDate);
            deliveryDatePicker.setValue(originalDeliveryDate);
            shippingAddressField.setText(originalShippingAddress);
            statusField.setText(originalStatus);

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
        } else {
            // Switch to View Mode and reset fields
            resetFieldsToOriginal();
        }
    }

    /**
     * Saves the original values of the work order fields for potential
     * restoration if edit mode is canceled.
     */
    private void setOriginalValues() {
        originalOrderOwner = workOrder.getOrderOwner();
        originalCustomer = workOrder.getCustomer();
        originalOrderDate = workOrder.getOrderDate().toLocalDate();
        originalDeliveryDate = workOrder.getDeliveryDate() != null ? workOrder.getDeliveryDate().toLocalDate() : null;
        originalShippingAddress = workOrder.getShippingAddress();
        originalStatus = workOrder.getStatus();
    }

    /**
     * Resets the fields in the UI to the original values stored before
     * entering edit mode.
     */
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
        orderDateLabel.setText(originalOrderDate.toString().substring(0, 10));
        if (originalDeliveryDate != null) {
            deliveryDateLabel.setText(originalDeliveryDate.toString());
        } else {
            deliveryDateLabel.setText("No delivery date set");
        }
        shippingAddressLabel.setText(originalShippingAddress);
        orderStatusLabel.setText(originalStatus);
    }

    /**
     * Sets the work order to be edited and populates the UI fields with
     * the current values from the work order.
     *
     * @param workOrder the work order to be edited
     */
    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;

        // Populate view with work order fields
        workOrderIdLabel.setText(workOrder.getWorkOrderId().toString());
        if (workOrder.getOrderOwner() != null) {
            orderOwnerLabel.setText(workOrder.getOrderOwner().toString());
        }
        customerLabel.setText(workOrder.getCustomer().toString());
        shippingAddressLabel.setText(workOrder.getShippingAddress());
        orderStatusLabel.setText(workOrder.getStatus());
        orderDateLabel.setText(workOrder.getOrderDate().toString().substring(0, 10));
        deliveryDateLabel.setText(workOrder.getDeliveryDate() != null
                ? workOrder.getDeliveryDate().toString() : "No delivery date set");

        // Load products associated with the work order
        loadWorkOrderProducts(workOrder.getWorkOrderId());
    }

    /**
     * Saves the modified work order details back to the database
     * and closes the edit popup.
     */
    @FXML
    private void saveWorkOrder() {
        workOrdersDAO = new WorkOrdersDAOImplementation(usersList, customerList);

        workOrder.setOrderOwner(orderOwnerComboBox.getValue());
        workOrder.setCustomer(customerComboBox.getValue());
        workOrder.setOrderDate(LocalDateTime.now());
        workOrder.setDeliveryDate(deliveryDatePicker.getValue() != null
                ? LocalDateTime.of(deliveryDatePicker.getValue(), LocalDateTime.now().toLocalTime())
                : null);
        workOrder.setShippingAddress(shippingAddressField.getText());
        workOrder.setStatus(statusField.getText());

        workOrdersDAO.updateWorkOrder(workOrder);

        // Close the popup window
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Deletes the current work order from the database and closes the edit popup.
     */
    @FXML
    private void deleteWorkOrder() {
        workOrdersDAO = new WorkOrdersDAOImplementation(usersList, customerList);

        workOrdersDAO.deleteWorkOrder(workOrder.getWorkOrderId());

        // Close the popup window
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Closes the edit popup without making changes.
     */
    @FXML
    private void closePopup() {
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }
}
