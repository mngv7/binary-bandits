package com.example.protrack.database;

import java.sql.Date;

public class ProductDBTable {
    private final Integer productId;
    private final String productName;
    private final Date dateCreated;
    private final Double price;

    // Constructor initialises the products with specific attributes
    public ProductDBTable(Integer productId, String productName, Date dateCreated, Double price) {
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

    public Double getPrice() {
        return price;
    }
}
