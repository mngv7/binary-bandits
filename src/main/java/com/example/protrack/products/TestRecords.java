package com.example.protrack.products;

public class TestRecords {

    private Integer testRecordId;
    private Integer productId;
    private Integer stepsId;

    public TestRecords(Integer testRecordId, Integer productId, Integer stepsId) {
        this.testRecordId = testRecordId;
        this.productId = productId;
        this.stepsId = stepsId;
    }

    public Integer getTestRecordId() {
        return testRecordId;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getStepsId() {
        return stepsId;
    }
}