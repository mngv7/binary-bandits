package com.example.protrack.products;

import java.sql.Date;

public class Product {

    private final Integer productId;
    private final String productName;
    private final Date dateCreated;

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
}
