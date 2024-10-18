package com.example.protrack.workorder;

import com.example.protrack.customer.Customer;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import com.example.protrack.workorderproducts.WorkOrderProductsDAOImplementation;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a work order in the system.
 */
public class WorkOrder {

    private final Integer workOrderId;

    private ProductionUser orderOwner;

    private Customer customer;

    private LocalDateTime orderDate;

    private LocalDateTime deliveryDate;

    private String shippingAddress; // Address specific to this order

    private String status;

    private Double subtotal;

    private final WorkOrderProductsDAOImplementation workOrderProductsDAO;

    /**
     * Constructs a new WorkOrder instance.
     *
     * @param workOrderId     Unique identifier for the work order
     * @param orderOwner      The user responsible for the order (can be null)
     * @param customer        The customer associated with the work order (must not be null)
     * @param orderDate       The date and time when the order was created (must not be null)
     * @param deliveryDate    The date and time scheduled for delivery (can be null)
     * @param shippingAddress The shipping address for the order (can be null)
     * @param status          The current status of the work order (can be null)
     * @param subtotal        The subtotal amount for the work order (must not be null and cannot be negative)
     * @throws IllegalArgumentException if any required fields are null or if subtotal is negative
     */
    public WorkOrder(Integer workOrderId, ProductionUser orderOwner, Customer customer, LocalDateTime orderDate,
                     LocalDateTime deliveryDate, String shippingAddress, String status, Double subtotal) {
        if (customer == null || orderDate == null || subtotal == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (subtotal < 0) {
            throw new IllegalArgumentException("Subtotal cannot be negative");
        }

        this.workOrderId = workOrderId;
        this.orderOwner = orderOwner; // This can be null
        this.customer = customer;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate; // This can be null
        this.shippingAddress = shippingAddress; // This can be null
        this.status = status; // This can be null
        this.subtotal = subtotal;
        this.workOrderProductsDAO = new WorkOrderProductsDAOImplementation();
    }

    /**
     * Returns the list of product in work order.
     *
     * @return List of products in the work order
     */
    public List<WorkOrderProduct> getProducts() {
        return workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(workOrderId);
    }

    /**
     * Returns the unique identifier for the work order.
     *
     * @return the work order ID
     */
    public Integer getWorkOrderId() {
        return workOrderId;
    }

    /**
     * Returns the user responsible for the order.
     *
     * @return the order owner
     */
    public ProductionUser getOrderOwner() {
        return orderOwner;
    }

    /**
     * Sets the user responsible for the order.
     *
     * @param orderOwner the user to set as the order owner
     */
    public void setOrderOwner(ProductionUser orderOwner) {
        this.orderOwner = orderOwner;
    }

    /**
     * Returns the customer associated with the work order.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the customer associated with the work order.
     *
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Returns the date and time when the order was created.
     *
     * @return the order date
     */
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the date and time when the order was created.
     *
     * @param orderDate the date and time to set as the order date
     */
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Returns the date and time scheduled for delivery.
     *
     * @return the delivery date, or null if not set
     */
    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * Sets the date and time scheduled for delivery.
     *
     * @param deliveryDate the date and time to set as the delivery date
     */
    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * Returns the shipping address for the order.
     *
     * @return the shipping address, or null if not set
     */
    public String getShippingAddress() {
        return shippingAddress;
    }

    /**
     * Sets the shipping address for the order.
     *
     * @param shippingAddress the address to set as the shipping address
     */
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * Returns the current status of the work order.
     *
     * @return the order status, or null if not set
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the work order.
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns the subtotal amount for the work order.
     *
     * @return the subtotal
     */
    public Double getSubtotal() {
        return subtotal;
    }

    /**
     * Sets the subtotal amount for the work order.
     *
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
