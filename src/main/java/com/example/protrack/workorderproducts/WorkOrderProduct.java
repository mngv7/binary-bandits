package com.example.protrack.workorderproducts;

public class WorkOrderProduct {
    private int workOrderId;
    private int productId;
    private String productName;
    private int quantity;
    private double price;
    private double total;

    // Constructor for WorkOrderProduct class
    public WorkOrderProduct(int workOrderId, int productId, String productName, int quantity, double price, double total) {
        this.workOrderId = workOrderId;
        this.productId = productId;
        this.productName = productName;
        setQuantity(quantity); // Ensure total is calculated based on initial quantity and price
        setPrice(price);       // Ensure total is calculated based on initial quantity and price
    }

    // Getters and Setters
    public int getWorkOrderId() {
        return workOrderId;
    }

    public int setWorkOrderId(Integer workOrderId) {
        return this.workOrderId = workOrderId;
    }

    // Getters and Setters
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
        this.quantity = quantity;
        updateTotal(); // Update total whenever quantity changes
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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