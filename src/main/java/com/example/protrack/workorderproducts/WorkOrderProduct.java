package com.example.protrack.workorderproducts;

/**
 * The WorkOrderProduct class represents a product associated with a work order.
 */
public class WorkOrderProduct {
    private int workOrderProductId;
    private int workOrderId;
    private final int productId;
    private final String productName;
    private int quantity;
    private double price;
    private double total;

    /**
     * Constructs a WorkOrderProduct with specified parameters.
     *
     * @param workOrderProductId The unique ID for the work order product.
     * @param workOrderId The ID of the associated work order.
     * @param productId The ID of the product.
     * @param productName The name of the product.
     * @param quantity The quantity of the product.
     * @param price The price of the product.
     * @throws IllegalArgumentException if any of the values are invalid.
     */
    public WorkOrderProduct(int workOrderProductId, int workOrderId, int productId, String productName, int quantity, double price) {
        if (workOrderProductId < 0) {
            throw new IllegalArgumentException("No fields can be null");
        }
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

        this.workOrderProductId = workOrderProductId;
        this.workOrderId = workOrderId;
        this.productId = productId;
        this.productName = productName;
        setQuantity(quantity); // Ensure total is calculated based on initial quantity and price
        setPrice(price);       // Ensure total is calculated based on initial quantity and price
        this.total = getTotal();
    }

    /**
     * Gets the unique ID of the work order product.
     *
     * @return The work order product ID.
     */
    public int getWorkOrderProductId() {
        return this.workOrderProductId;
    }

    /**
     * Sets the unique ID for the work order product.
     *
     * @param workOrderProductId The new work order product ID.
     */
    public void setWorkOrderProductId(Integer workOrderProductId) {
        this.workOrderProductId = workOrderProductId;
    }

    /**
     * Gets the ID of the associated work order.
     *
     * @return The work order ID.
     */
    public int getWorkOrderId() {
        return workOrderId;
    }

    /**
     * Sets the ID of the associated work order.
     *
     * @param workOrderId The new work order ID.
     * @throws IllegalArgumentException if the work order ID is negative.
     */
    public void setWorkOrderId(int workOrderId) {
        if (workOrderId < 0) {
            throw new IllegalArgumentException("Work order ID cannot be negative");
        }
        this.workOrderId = workOrderId;
    }

    /**
     * Gets the product ID.
     *
     * @return The product ID.
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Gets the name of the product.
     *
     * @return The product name.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Gets the quantity of the product.
     *
     * @return The quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product.
     *
     * @param quantity The new quantity.
     * @throws IllegalArgumentException if the quantity is negative.
     */
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
        updateTotal(); // Update total whenever quantity changes
    }

    /**
     * Gets the price of the product.
     *
     * @return The price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price The new price.
     * @throws IllegalArgumentException if the price is negative.
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
        updateTotal(); // Update total whenever price changes
    }

    /**
     * Gets the total cost for the product based on quantity and price.
     *
     * @return The total cost.
     */
    public double getTotal() {
        return total;
    }

    /**
     * Updates the total cost based on the current quantity and price.
     */
    private void updateTotal() {
        this.total = quantity * price; // Calculate total based on quantity and price
    }
}
