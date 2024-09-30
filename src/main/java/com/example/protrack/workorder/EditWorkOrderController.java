package com.example.protrack.workorder;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EditWorkOrderController {

    @FXML
    private Label workOrderIdField;

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
    private Double originalSubtotal;

    /**
     * Initializes the controller, populating ComboBoxes with data from DAOs.
     */
    public void initialize() {
        usersList = FXCollections.observableArrayList(new UsersDAO().getProductionUsers());
        customerList = FXCollections.observableArrayList(new CustomerDAO().getAllCustomers());

        orderOwnerComboBox.setItems(usersList);
        customerComboBox.setItems(customerList);

        // Hide editable fields initially
        orderOwnerComboBox.setVisible(false);
        customerComboBox.setVisible(false);
        orderDatePicker.setVisible(false);
        deliveryDatePicker.setVisible(false);
        saveButton.setVisible(false);
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

            orderOwnerLabel.setVisible(false);
            customerLabel.setVisible(false);
            orderDateLabel.setVisible(false);
            deliveryDateLabel.setVisible(false);

            shippingAddressField.setEditable(true);
            statusField.setEditable(true);

            saveOriginalValues();
        } else {
            // Switch to View Mode and reset fields
            resetFieldsToOriginal();
        }
    }

    /**
     * Saves the current work order's original values for later reset.
     */
    private void saveOriginalValues() {
        originalOrderOwner = workOrder.getOrderOwner();
        originalCustomer = workOrder.getCustomer();
        originalOrderDate = workOrder.getOrderDate().toLocalDate();
        originalDeliveryDate = workOrder.getDeliveryDate() != null ? workOrder.getDeliveryDate().toLocalDate() : null;
        originalShippingAddress = workOrder.getShippingAddress();
        originalStatus = workOrder.getStatus();
    }

    /**
     * Resets the fields to their original values.
     */
    private void resetFieldsToOriginal() {
        editButton.setText("Edit");
        saveButton.setVisible(false);

        orderOwnerComboBox.setVisible(false);
        customerComboBox.setVisible(false);
        orderDatePicker.setVisible(false);
        deliveryDatePicker.setVisible(false);

        orderOwnerLabel.setVisible(true);
        customerLabel.setVisible(true);
        orderDateLabel.setVisible(true);
        deliveryDateLabel.setVisible(true);
        shippingAddressLabel.setVisible(true);
        orderStatusLabel.setVisible(true);

        shippingAddressField.setEditable(false);
        statusField.setEditable(false);

        orderOwnerComboBox.setValue(originalOrderOwner);
        customerComboBox.setValue(originalCustomer);
        orderDatePicker.setValue(originalOrderDate);
        deliveryDatePicker.setValue(originalDeliveryDate);
        shippingAddressField.setText(originalShippingAddress);
        statusField.setText(originalStatus);
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;

        // Populates view with work order fields
        if (workOrder.getOrderOwner() != null) {
            orderOwnerLabel.setText(workOrder.getOrderOwner().toString());
        }
        customerLabel.setText(workOrder.getCustomer().toString());
        shippingAddressLabel.setText(workOrder.getShippingAddress());
        orderDateLabel.setText(workOrder.getOrderDate().toString());
        if (workOrder.getDeliveryDate() != null) {
            deliveryDateLabel.setText(workOrder.getDeliveryDate().toString());
        }
        orderStatusLabel.setText(workOrder.getStatus());
        subtotalLabel.setText(workOrder.getSubtotal().toString());

        // Populate editable fields with current work order data (initially hidden)
        workOrderIdField.setText(String.valueOf(workOrder.getWorkOrderId()));
        orderOwnerComboBox.setValue(workOrder.getOrderOwner());
        customerComboBox.setValue(workOrder.getCustomer());
        orderDatePicker.setValue(workOrder.getOrderDate().toLocalDate());
        deliveryDatePicker.setValue(workOrder.getDeliveryDate() != null ? workOrder.getDeliveryDate().toLocalDate() : null);
        shippingAddressField.setText(workOrder.getShippingAddress());
        statusField.setText(workOrder.getStatus());

        // Save the original values to reset if cancel is clicked
        saveOriginalValues();
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
        workOrder.setDeliveryDate(deliveryDate != null ? LocalDateTime.of(deliveryDate, workOrder.getOrderDate().toLocalTime()) : null);
        workOrder.setShippingAddress(shippingAddress);
        workOrder.setStatus(status);
        workOrder.setSubtotal(subtotal);

        workOrdersDAO.updateWorkOrder(workOrder);

        toggleEdit(); // Return to view mode
    }

    public void deleteWorkOrder() {
        if (workOrder != null) {
            WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(usersList, customerList);
            workOrdersDAO.deleteWorkOrder(workOrder.getWorkOrderId());
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}