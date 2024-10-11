package com.example.protrack.parts;

/**
 * Represents the creation of a new part.
 * Each part has a part ID, name, description, supplier ID, and cost.
 */
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

    /**
     * Constructs a new part with the specified part ID, name, description, supplier ID, and cost.
     * @param partsId the ID of the part being created
     * @param name the name of the part being created
     * @param description the description of the part being created
     * @param supplierId the supplier of the part being created
     * @param cost the cost of the part being created
     * @throws IllegalArgumentException if any field is null or invalid
     */
    public Parts(Integer partsId, String name, String description, Integer supplierId, Double cost) {
        if (partsId == null || name == null || description == null || supplierId == null || cost == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (partsId < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        }
        if (cost < 0 || cost > 10000.00) {
            throw new IllegalArgumentException("Cost must be between 0 and 10,000.00");
        }
        if (name.isEmpty() || description.isEmpty()) {
            throw new IllegalArgumentException("Name and description cannot be empty");
        }
        this.partsId = partsId;
        this.name = name;
        this.description = description;
        this.supplierId = supplierId;
        this.cost = cost;
    }

    /**
     * @return the ID of the part being created
     */
    public Integer getPartsId() {
        return partsId;
    }

    /**
     * @return the name of the part being created
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description of the part being created
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the supplier ID of the part being created
     */
    public Integer getSupplierId() {
        return supplierId;
    }

    /**
     * @return the cost of the part being created
     */
    public Double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return this.name;
    }
}