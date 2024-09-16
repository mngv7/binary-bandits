package com.example.protrack.products;

public class BillOfMaterials {

    private final Integer partsId;
    private final Integer productId;
    private final Integer requiredAmount;

    public BillOfMaterials(Integer partsId, Integer productId, Integer requiredAmount) {
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
