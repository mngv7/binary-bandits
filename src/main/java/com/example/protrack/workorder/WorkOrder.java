package com.example.protrack.workorder;

import com.example.protrack.customer.Customer;
import com.example.protrack.users.ProductionUser;

import java.time.LocalDateTime;

public class WorkOrder {

    private Integer workOrderId;
    private ProductionUser orderOwner;
    private Customer customer;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private String shippingAddress; // Address specific to this order
    private Integer productId;
    private String status;
    private Double subtotal;

    // Constructor
    public WorkOrder(Integer workOrderId, ProductionUser orderOwner, Customer customer, LocalDateTime orderDate, LocalDateTime deliveryDate, String shippingAddress, Integer productId, String status, Double subtotal) {
        this.workOrderId = workOrderId;
        this.orderOwner = orderOwner;
        this.customer = customer;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.shippingAddress = shippingAddress;
        this.productId = productId;
        this.status = status;
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

    public Integer getProducts() {
        return productId;
    }

    public void setProducts(Integer products) {
        this.productId = products;
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