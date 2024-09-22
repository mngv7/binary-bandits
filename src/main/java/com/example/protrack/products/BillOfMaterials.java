package com.example.protrack.products;

public class BillOfMaterials {

    // id of part in BoM
    private final Integer partsId;

    // id of product of BoM
    private final Integer productId;

    // number of parts required for BoM
    private final Integer requiredAmount;

    // initializes the BoM with specific attributes
    public BillOfMaterials(Integer partsId, Integer productId, Integer requiredAmount) {
        if (partsId == null || productId == null || requiredAmount == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (partsId <= 0 || productId <= 0) {
            throw new IllegalArgumentException("IDs must be positive integers");
        }
        if (requiredAmount < 1 || requiredAmount > 1000) {
            throw new IllegalArgumentException("Required amount must be between 1 and 1000");
        }
        this.partsId = partsId;
        this.productId = productId;
        this.requiredAmount = requiredAmount;
    }

    // Getters for the fields
    public Integer getPartsId() {
        return partsId;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getRequiredAmount() {
        return requiredAmount;
    }
}