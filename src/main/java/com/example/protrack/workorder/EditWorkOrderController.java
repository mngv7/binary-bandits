package com.example.protrack.workorder;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EditWorkOrderController {

    @FXML
    private TextField workOrderIdField;

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
    private TextField subtotalField;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    private WorkOrder workOrder;
    private WorkOrdersDAOImplementation workOrdersDAO;

    private ObservableList<ProductionUser> usersList;
    private ObservableList<Customer> customerList;

    /**
     * Initializes the controller, populating ComboBoxes with data from DAOs.
     */
    public void initialize() {

        // Load ProductionUser and Customer data from DAOs into lists
        usersList = FXCollections.observableArrayList(new UsersDAO().getProductionUsers());
        customerList = FXCollections.observableArrayList(new CustomerDAO().getAllCustomers());

        // Populate ComboBoxes
        orderOwnerComboBox.setItems(usersList);
        customerComboBox.setItems(customerList);
    }

    /**
     * Sets the current work order to be edited and populates the form with its data.
     *
     * @param workOrder The work order to edit
     */
    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;

        // Populate fields with current work order data
        workOrderIdField.setText(String.valueOf(workOrder.getWorkOrderId()));
        orderOwnerComboBox.setValue(workOrder.getOrderOwner());
        customerComboBox.setValue(workOrder.getCustomer());
        orderDatePicker.setValue(workOrder.getOrderDate().toLocalDate());
        deliveryDatePicker.setValue(workOrder.getDeliveryDate() != null ? workOrder.getDeliveryDate().toLocalDate() : null);
        shippingAddressField.setText(workOrder.getShippingAddress());
        statusField.setText(workOrder.getStatus());
        subtotalField.setText(String.valueOf(workOrder.getSubtotal()));
    }

    /**
     * Saves the updated work order back to the database.
     */
    public void saveWorkOrder() {
        // Validate and get input values
        ProductionUser selectedUser = orderOwnerComboBox.getValue();
        Customer selectedCustomer = customerComboBox.getValue();
        LocalDate orderDate = orderDatePicker.getValue();
        LocalDate deliveryDate = deliveryDatePicker.getValue();
        String shippingAddress = shippingAddressField.getText();
        String status = statusField.getText();
        double subtotal = Double.parseDouble(subtotalField.getText());

        if (selectedCustomer == null || orderDate == null || shippingAddress.isEmpty() || status.isEmpty()) {
            // Add validation logic as needed
            System.out.println("Validation failed");
            return;
        }

        // Update the current work order object

        workOrder.setOrderOwner(selectedUser);
        workOrder.setCustomer(selectedCustomer);
        workOrder.setOrderDate(LocalDateTime.of(orderDate, workOrder.getOrderDate().toLocalTime()));
        workOrder.setDeliveryDate(deliveryDate != null ? LocalDateTime.of(deliveryDate, workOrder.getOrderDate().toLocalTime()) : null);
        workOrder.setShippingAddress(shippingAddress);
        workOrder.setStatus(status);
        workOrder.setSubtotal(subtotal);

        // Update work order in the database
        workOrdersDAO = new WorkOrdersDAOImplementation(usersList, customerList);
        workOrdersDAO.updateWorkOrder(workOrder);

        // Close the window
        closeWindow();
    }

    /**
     * Deletes the work order from the database.
     */
    public void deleteWorkOrder() {
        if (workOrder != null) {
            // Delete the work order from the database
            workOrdersDAO = new WorkOrdersDAOImplementation(usersList, customerList);
            workOrdersDAO.deleteWorkOrder(workOrder.getWorkOrderId());

            // Closes the popup stage
            closeWindow();
        }
    }

    /**
     * Closes the current window upon action completion
     */
    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}