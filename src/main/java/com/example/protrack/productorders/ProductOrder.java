package com.example.protrack.productorders;

public class ProductOrder {
    private  int productOrderID;

    private int productID;

    private int quantity;

    private int workOrderID;

    public ProductOrder(int productOrderID, int productID, int quantity, int workOrderID) {
        this.productOrderID = productOrderID;
        this.productID = productID;
        this.quantity = quantity;
        this.workOrderID = workOrderID;
    }

    public int getProductOrderID() {
        return productOrderID;
    }

    public int getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getWorkOrderID() {
        return workOrderID;
    }

    @Override
    public String toString() {
        return String.valueOf(this.productOrderID);
    }
}
