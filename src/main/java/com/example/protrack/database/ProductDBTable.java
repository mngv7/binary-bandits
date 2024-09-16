package com.example.protrack.database;

import java.sql.Date;

public class ProductDBTable {
    private final Integer productId;
    private final String productName;
    private final Date dateCreated;
    private final Double price;

    public ProductDBTable(Integer productId, String productName, Date dateCreated, Double price) {
        this.productId = productId;
        this.productName = productName;
        this.dateCreated = dateCreated;
        this.price = price;
    }

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
