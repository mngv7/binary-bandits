package com.example.protrack.products;

public class TestRecord {

    private final Integer stepId;
    private final Integer productId;
    private final Integer stepNumber;
    private final String stepDescription;
    private final String stepCheckType;
    private final String stepCheckCriteria;

    public TestRecord(Integer stepId, Integer productId, Integer stepNumber, String stepDescription, String stepCheckType, String stepCheckCriteria) {
        this.stepId = stepId;
        this.productId = productId;
        this.stepNumber = stepNumber;
        this.stepDescription = stepDescription;
        this.stepCheckType = stepCheckType;
        this.stepCheckCriteria = stepCheckCriteria;
    }

    public Integer getStepId() {
        return stepId;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public String getStepCheckType() {
        return stepCheckType;
    }

    public String getStepCheckCriteria() {
        return stepCheckCriteria;
    }
}
