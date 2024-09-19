package com.example.protrack.workorder;

public class WorkOrderProduct {
    private int productId;
    private String productName;
    private String description;
    private int quantity;
    private double price;
    private double total;

    public WorkOrderProduct(int productId, String productName, String description, int quantity, double price, double total) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return total;
    }
}