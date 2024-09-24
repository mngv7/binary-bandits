package com.example.protrack.productbuild;

public class ProductBuild {
    private Integer buildId;
    private Integer productOrderId;
    private float buildCompletion;
    private Integer productId;

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

    public Integer getProductId() {
        return productId;
    }
}
