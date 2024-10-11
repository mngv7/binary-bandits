package com.example.protrack.warehouseutil;

public class Locations {
    private final Integer locationId;
    private final String locationAlias;
    private final String locationType;
    private final Float locationCapacity;

    public Locations(int locationId, String locationAlias, String locationType, Float locationCapacity) {
        this.locationId = locationId;
        this.locationAlias = locationAlias;
        this.locationType = locationType;
        this.locationCapacity = locationCapacity;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public String getLocationAlias() {
        return locationAlias;
    }

    public String getLocationType() {
        return locationType;
    }

    public Float getLocationCapacity() {
        return locationCapacity;
    }
}
