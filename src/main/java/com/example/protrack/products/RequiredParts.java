package com.example.protrack.products;

public class RequiredParts {

    private Integer partsId;
    private Integer productId;
    private Integer requiredAmount;

    public RequiredParts(Integer partsId, Integer productId, Integer requiredAmount) {
        this.partsId = partsId;
        this.productId = productId;
        this.requiredAmount = requiredAmount;
    }

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
