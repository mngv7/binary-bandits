package com.example.protrack.requests;

/**
 * Represents a stock request made for transferring parts between locations.
 * Each request has a location ID, part ID, request ID, and the quantity requested.
 */
public class Requests {

    private final Integer locationId;

    private final Integer partId;

    private final Integer requestId;

    private final Integer quantity;

    /**
     * Constructs a new request with the specified location, part, request ID, and quantity.
     * @param locationId the ID of the location requesting the part
     * @param partId the ID of the part being requested
     * @param requestId the unique ID of the request
     * @param quantity the quantity of the part being requested
     * @throws IllegalArgumentException if any field is null or invalid
     */
    public Requests(Integer locationId, Integer partId, Integer requestId, Integer quantity) {
        if (locationId == null || partId == null || requestId == null || quantity == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (partId < 0) {
            throw new IllegalArgumentException("ID cannot be negative");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity requested must be more than 0");
        }

        this.locationId = locationId;
        this.partId = partId;
        this.requestId = requestId;
        this.quantity = quantity;
    }

    /**
     * @return the ID of the location making the request
     */
    public Integer getLocationId() {
        return locationId;
    }

    /**
     * @return the ID of the part being requested
     */
    public Integer getPartId() {
        return partId;
    }

    /**
     * @return the unique ID of the request
     */
    public Integer getRequestId() {
        return requestId;
    }

    /**
     * @return the quantity of the part requested
     */
    public Integer getQuantity() {
        return quantity;
    }
}
