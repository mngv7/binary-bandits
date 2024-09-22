package com.example.protrack.workorderproducts;

public class WorkOrderProduct {
    private int workOrderId;
    private int productId;
    private String productName;
    private int quantity;
    private double price;
    private double total;

    // Constructor for WorkOrderProduct class
    public WorkOrderProduct(int workOrderId, int productId, String productName, int quantity, double price) {
        if (productName == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (workOrderId < 0) {
            throw new IllegalArgumentException("Work order ID cannot be negative");
        }
        if (productId < 0) {
            throw new IllegalArgumentException("Product ID cannot be negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        this.workOrderId = workOrderId;
        this.productId = productId;
        this.productName = productName;
        setQuantity(quantity); // Ensure total is calculated based on initial quantity and price
        setPrice(price);       // Ensure total is calculated based on initial quantity and price
        this.total = getTotal();
    }

    // Getters and Setters
    public int getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(int workOrderId) {
        if (workOrderId < 0) {
            throw new IllegalArgumentException("Work order ID cannot be negative");
        }
        this.workOrderId = workOrderId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
        updateTotal(); // Update total whenever quantity changes
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
        updateTotal(); // Update total whenever price changes
    }

    public double getTotal() {
        return total;
    }

    private void updateTotal() {
        this.total = quantity * price; // Calculate total based on quantity and price
    }
}