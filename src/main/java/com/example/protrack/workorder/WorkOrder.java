package com.example.protrack.workorder;

import com.example.protrack.customer.Customer;
import com.example.protrack.users.ProductionUser;

import java.time.LocalDateTime;

public class WorkOrder {

    private final Integer workOrderId;
    private ProductionUser orderOwner;
    private final Customer customer;
    private final LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private String shippingAddress; // Address specific to this order
    private String status;
    private final Double subtotal;

    // Constructor
    public WorkOrder(Integer workOrderId, ProductionUser orderOwner, Customer customer, LocalDateTime orderDate, LocalDateTime deliveryDate, String shippingAddress, String status, Double subtotal) {
        if (workOrderId == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (customer == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (orderDate == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (subtotal == null) {
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
    }

    // Getters and Setters
    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public ProductionUser getOrderOwner() {
        return orderOwner;
    }

    public void setOrderOwner(ProductionUser orderOwner) {
        this.orderOwner = orderOwner;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getSubtotal() {
        return subtotal;
    }
}