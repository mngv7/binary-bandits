package com.example.protrack.warehouseutil;

public interface Workstation {
    int getWorkstationLocationId();

    void setWorkstationLocationId(int workstationID);

    String getWorkstationLocationAlias();

    void setWorkstationLocationAlias(String workstationName);

    int getWorkstationMaxParts();

    void setWorkstationMaxParts(int maxParts);

    /* TODO: See if these are actually needed anymore. */
    String getWorkstationLocation();

    void setWorkstationLocation(String workstationLocation);

    /*
     * These functions import and return quantities of the given partsID between
     * this interface's underlying implementation and a given warehouse implementation
     * passed in via the function arguments.
     *
     * These functions generally need a DAO variable, however some implementations leave
     * it unused. Such cases are explicitly labeled in their implementation's code comments.
     */
    void importPartsIdWithQuantityFromWarehouse(Warehouse targetWarehouse, LocationsAndContentsDAO dao, int componentId, int quantity);

    void returnPartsIdWithQuantityToWarehouse(Warehouse targetWarehouse, LocationsAndContentsDAO dao, int componentId, int quantity);

    void returnAllPartsToWarehouse(Warehouse targetWarehouse, LocationsAndContentsDAO dao);
}