package com.example.protrack.parts;

public class Parts {

    // Unique identifier for the part
    private final Integer partsId;

    // Name of the part
    private final String name;

    // Description of the part
    private final String description;

    // ID of the supplier providing the part
    private final Integer supplierId;

    // Cost of the part
    private final Double cost;

    // Constructor initializes the parts with specific attributes
    public Parts(Integer partsId, String name, String description, Integer supplierId, Double cost) {
        this.partsId = partsId;
        this.name = name;
        this.description = description;
        this.supplierId = supplierId;
        this.cost = cost;
    }

    // Getter for the part's unique identifier
    public Integer getPartsId() {
        return partsId;
    }

    // Getter for the part's name
    public String getName() {
        return name;
    }

    // Getter for the part's description
    public String getDescription() {
        return description;
    }

    // Getter for the supplier's ID
    public Integer getSupplierId() {
        return supplierId;
    }

    // Getter for the part's cost
    public Double getCost() {
        return cost;
    }
}
