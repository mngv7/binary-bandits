package com.example.protrack.requests;

public class Requests {

    private Integer locationId;

    private Integer partId;

    private Integer requestId;

    private Integer quantity;

    public Requests(Integer locationId, Integer partId, Integer requestId, Integer quantity) {
        if (locationId == null || partId == null || requestId == null || quantity == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (partId < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        } if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity requested must be more than 0");
        }

        this.locationId = locationId;
        this.partId = partId;
        this.requestId = requestId;
        this.quantity = quantity;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public Integer getPartId() {
        return partId;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
