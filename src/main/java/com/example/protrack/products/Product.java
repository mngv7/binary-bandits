package com.example.protrack.products;

import java.sql.Date;

public class Product {
    private final Integer productId;
    private final String productName;
    private final Date dateCreated;
    private final Double price;

    public Product(Integer productId, String productName, Date dateCreated, Double price) {
        if (productId == null || productId <= 0 || productName == null || productName.length() > 255 || dateCreated == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (productName.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (dateCreated.after(new Date(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Date created cannot be in the future");
        }
        if (price.isNaN()) {
            throw new IllegalArgumentException("Price must be a number");
        }
        this.productId = productId;
        this.productName = productName;
        this.dateCreated = dateCreated;
        this.price = price;
    }

    // Getters for the updated fields
    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public String toString() {
        return this.productName; // Adjust according to your attribute
    }

    public double getPrice() {
        return price;
    }
}