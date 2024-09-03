package com.example.protrack.products;

public class Steps {

    private Integer stepsId;
    private Integer partsId;
    private Integer stepNum;
    private String stepDescription;
    private String checkType;
    private String checkCriteria;

    public Steps(Integer stepsId, Integer partsId, Integer stepNum, String stepDescription, String checkType, String checkCriteria) {
        this.stepsId = stepsId;
        this.partsId = partsId;
        this.stepNum = stepNum;
        this.stepDescription = stepDescription;
        this.checkType = checkType;
        this.checkCriteria = checkCriteria;
    }

    public Integer getStepsId() {
        return stepsId;
    }

    public Integer getPartsId() {
        return partsId;
    }

    public Integer getStepNum() {
        return stepNum;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public String getCheckType() {
        return checkType;
    }

    public String getCheckCriteria() {
        return checkCriteria;
    }
}
