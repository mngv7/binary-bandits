package com.example.protrack.products;

public class RequiredParts {

    private Integer reqPartsId;
    private String partsId;
    private Integer requiredAmt;
    private Integer currentAmt;

    public RequiredParts(Integer reqPartsId, String partsId, Integer requiredAmt, Integer currentAmt) {
        this.reqPartsId = reqPartsId;
        this.partsId = partsId;
        this.requiredAmt = requiredAmt;
        this.currentAmt = currentAmt;
    }

    public Integer getReqPartsId() {
        return reqPartsId;
    }

    public String getPartsId() {
        return partsId;
    }

    public Integer getRequiredAmt() {
        return requiredAmt;
    }

    public Integer getCurrentAmt() {
        return currentAmt;
    }
}
