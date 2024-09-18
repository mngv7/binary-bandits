package com.example.protrack.products;

public class TestRecord {

    private final Integer stepId;
    private final Integer productId;
    private final Integer stepNumber;
    private final String stepDescription;
    private final String stepCheckType;
    private final String stepCheckCriteria;

    // Constructor initialises the test record with specific attributes
    public TestRecord(Integer stepId, Integer productId, Integer stepNumber, String stepDescription, String stepCheckType, String stepCheckCriteria) {
        this.stepId = stepId;
        this.productId = productId;
        this.stepNumber = stepNumber;
        this.stepDescription = stepDescription;
        this.stepCheckType = stepCheckType;
        this.stepCheckCriteria = stepCheckCriteria;
    }

    // Getter methods for step id
    public Integer getStepId() {
        return stepId;
    }

    // Getter methods for product id
    public Integer getProductId() {
        return productId;
    }

    // Getter methods for step number
    public Integer getStepNumber() {
        return stepNumber;
    }

    // Getter methods for step description
    public String getStepDescription() {
        return stepDescription;
    }

    // Getter methods for step check type
    public String getStepCheckType() {
        return stepCheckType;
    }

    // Getter methods for step check criteria
    public String getStepCheckCriteria() {
        return stepCheckCriteria;
    }
}
