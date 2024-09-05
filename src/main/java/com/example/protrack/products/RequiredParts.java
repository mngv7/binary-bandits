package com.example.protrack.products;

public class RequiredParts {

    private String partsId;
    private Integer productId;
    private Integer requiredAmount;

    public RequiredParts(String partsId, Integer productId, Integer requiredAmount) {
        this.partsId = partsId;
        this.productId = productId;
        this.requiredAmount = requiredAmount;
    }

    public String getPartsId() {
        return partsId;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getRequiredAmount() {
        return requiredAmount;
    }
}
