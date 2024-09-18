package com.example.protrack.products;

public class BillOfMaterials {

    // id of part in BoM
    private final Integer partsId;

    // id of product of BoM
    private final Integer productId;

    // number of parts required for BoM
    private final Integer requiredAmount;

    // Constructor initialises the bill of materials with specific attributes
    public BillOfMaterials(Integer partsId, Integer productId, Integer requiredAmount) {
        this.partsId = partsId;
        this.productId = productId;
        this.requiredAmount = requiredAmount;
    }

    // Getters for the updated fields
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
