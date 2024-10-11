package com.example.protrack.applicationpages;

public class WarehousePastRequests {

    private final Integer partsId;

    // Name of the part
    private final String name;

    private final Integer quantity;


    public WarehousePastRequests(Integer partsId, String name, Integer quantity) {

        if (partsId == null) {
            throw new IllegalArgumentException("No fields can be null");
        }

        if (name == null) {
            throw new IllegalArgumentException("No fields can be null");
        }

        if (quantity == null) {
            throw new IllegalArgumentException("No fields can be null");
        }

        this.partsId = partsId;
        this.name = name;
        this.quantity = quantity;
    }

    public Integer getPartsId() {
        return partsId;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
