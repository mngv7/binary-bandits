package com.example.protrack.parts;

public class Parts {
    private Integer partsId;
    private String name;
    private String description;
    private Integer supplierId;
    private Double cost;

    public Parts(Integer partsId, String name, String description, Integer supplierId, Double cost) {
        this.partsId = partsId;
        this.name = name;
        this.description = description;
        this.supplierId = supplierId;
        this.cost = cost;
    }

    public Integer getPartsId() {
        return partsId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public Double getCost() {
        return cost;
    }
}
