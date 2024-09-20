package com.example.protrack.products;

import java.sql.Date;

public class Product {

    // id of product
    private final Integer productId;

    // name of product
    private final String productName;

    // date of product creation
    private final Date dateCreated;

    // Constructor initialises the products with specific attributes
    public Product(Integer productId, String productName, Date dateCreated) {
        this.productId = productId;
        this.productName = productName;
        this.dateCreated = dateCreated;
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
}
