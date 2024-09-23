package com.example.protrack.requests;

public class Requests {

    private int locationId;

    private int partId;

    private int requestId;

    private int quantity;

    public Requests(int locationId, int partId, int requestId, int quantity) {
        this.locationId = locationId;
        this.partId = partId;
        this.requestId = requestId;
        this.quantity = quantity;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getPartId() {
        return partId;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getQuantity() {
        return quantity;
    }
}
