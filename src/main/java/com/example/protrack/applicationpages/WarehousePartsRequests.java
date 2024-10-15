package com.example.protrack.applicationpages;

public class WarehousePartsRequests {

    private final Integer partsId;
    private final Integer locationId;

    // Name of the part
    private final String name;

    private final Integer quantity;



    public WarehousePartsRequests(Integer partsId, Integer locationId, String name, Integer quantity) {

        if (partsId == null) {
            throw new IllegalArgumentException("No fields can be null");
        }

        if (locationId == null) {
            throw new IllegalArgumentException("No fields can be null");
        }

        if (name == null) {
            throw new IllegalArgumentException("No fields can be null");
        }

        if (quantity == null) {
            throw new IllegalArgumentException("No fields can be null");
        }

        this.partsId = partsId;
        this.locationId = locationId;
        this.name = name;
        this.quantity = quantity;
    }

    public Integer getPartsId() {
        return partsId;
    }
    public Integer getLocationId () {
        return locationId;
    }
    public String getName() {
        return name;
    }
    public Integer getQuantity() {
        return quantity;
    }
}
