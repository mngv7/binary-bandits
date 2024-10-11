package com.example.protrack.database;

public class ProductBuildWSAmt {
    Integer partId;
    String partName;
    Integer reqAmount;
    Integer quantity;


    public ProductBuildWSAmt(int partId, String partName, int reqAmount, int quantity) {
        this.partId = partId;
        this.partName = partName;
        this.reqAmount = reqAmount;
        this.quantity = quantity;
    }

    public Integer getPartId() {
        return partId;
    }

    public String getPartName() {
        return partName;
    }

    public Integer getReqAmount() {
        return reqAmount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
