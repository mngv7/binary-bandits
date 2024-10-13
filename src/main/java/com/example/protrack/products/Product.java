package com.example.protrack.products;

import java.sql.Date;

/**
 * The {@code Product} class represents a product
 */
public class Product {
    private final Integer productId; // Unique identifier for the product
    private final String productName; // Name of the product
    private final Date dateCreated; // Date when the product was created
    private final Double price; // Price of the product

    /**
     * Constructs a {@code Product} instance with the specified attributes.
     *
     * @param productId   the unique identifier for the product
     * @param productName the name of the product
     * @param dateCreated the creation date of the product
     * @param price       the price of the product
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Product(Integer productId, String productName, Date dateCreated, Double price) {
        if (productId == null || productId <= 0 || productName == null || productName.length() > 255 || dateCreated == null || price == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (productName.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (dateCreated.after(new Date(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Date created cannot be in the future");
        }
        if (price.isNaN() || price < 0) {
            throw new IllegalArgumentException("Price must be a non-negative number");
        }
        this.productId = productId;
        this.productName = productName;
        this.dateCreated = dateCreated;
        this.price = price;
    }

    /**
     * Retrieves the unique identifier of the product.
     *
     * @return the product ID
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * Retrieves the name of the product.
     *
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Retrieves the creation date of the product.
     *
     * @return the date the product was created
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * Retrieves the price of the product.
     *
     * @return the product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns a string representation of the product.
     *
     * @return the name of the product
     */
    @Override
    public String toString() {
        return this.productName; // Adjust according to your attribute
    }
}
