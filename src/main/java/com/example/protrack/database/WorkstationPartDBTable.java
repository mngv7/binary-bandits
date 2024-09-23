package com.example.protrack.database;

public class WorkstationPartDBTable {
    private Integer partID;
    private String partName;
    private Integer quantity;

    public WorkstationPartDBTable(Integer partID, String partName, Integer quantity) {
        this.partID = partID;
        this.partName = partName;
        this.quantity = quantity;
    }

    public Integer getPartID() {
        return partID;
    }

    public String getPartName() {
        return partName;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
