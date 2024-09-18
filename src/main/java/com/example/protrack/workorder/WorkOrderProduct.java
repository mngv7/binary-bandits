package com.example.protrack.workorder;

import com.example.protrack.products.Product;

public class WorkOrderProduct {

    private int workOrderId;   // FK to WorkOrder
    private int productId;     // FK to Product
    private int quantity;      // Quantity of product in the work order
    private Product product;   // Reference to the Product object

    // Constructor
    public WorkOrderProduct(int workOrderId, int productId, int quantity, Product product) {
        this.workOrderId = workOrderId;
        this.productId = productId;
        this.quantity = quantity;
        this.product = product;
    }

    // Getters and setters
    public int getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(int workOrderId) {
        this.workOrderId = workOrderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Method to calculate the total cost of this product in the work order
    //public double calculateTotal() {
    //   return product.getPrice() * quantity;
    //}
}
