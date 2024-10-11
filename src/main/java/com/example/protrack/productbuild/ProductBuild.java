package com.example.protrack.productbuild;

public class ProductBuild {
    private final Integer buildId;
    private final Integer productOrderId;
    private float buildCompletion;
    private final Integer productId;

    public ProductBuild(Integer buildId, Integer productOrderId, float buildCompletion, Integer productId) {
        this.buildId = buildId;
        this.productOrderId = productOrderId;
        this.buildCompletion = buildCompletion;
        this.productId = productId;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public Integer getProductOrderId() {
        return productOrderId;
    }

    public float getBuildCompletion() {
        return buildCompletion;
    }

    public void setBuildCompletion(Float buildCompletion) {
        this.buildCompletion = buildCompletion;
    }

    public Integer getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return this.productOrderId.toString();
    }
}
