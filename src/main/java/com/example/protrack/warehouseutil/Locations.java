package com.example.protrack.warehouseutil;

public class Locations {
    private Integer locationId;
    private String locationAlias;
    private String locationType;
    private Float locationCapacity;

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
