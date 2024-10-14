package com.example.protrack.warehouseutil;

public interface Workstation {
    /**
     * Gets the workstation's location ID.
     * @return An integer describing the workstation's location ID
     */
    int getWorkstationLocationId();

    /**
     * Sets the workstation's location ID.
     * @param workstationID The new location ID to assign to the workstation.
     */
    void setWorkstationLocationId(int workstationID);

    /**
     * Gets the workstation's location alias.
     * @return A string containing this workstation's location alias.
     */
    String getWorkstationLocationAlias();

    /**
     * Sets the workstation's location alias to a new name.
     * @param workstationName The new name to set the workstation to.
     */
    void setWorkstationLocationAlias(String workstationName);

    /**
     * Gets the capacity of the workstation.
     * @return An integer describing the capacity of the workstation.
     */
    int getWorkstationMaxParts();

    /**
     * Sets a new capacity for the workstation.
     * @param maxParts The new capacity of the workstation expressed as an integer.
     */
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

    /**
     * Imports the given partsID of the given quantity from the target warehouse.
     * Implementations of this interface are recommended to also subtract said quantity from the warehouse itself.
     * @param targetWarehouse The target instance of the Warehouse interface to subtract parts from.
     * @param dao The LocationsAndContentsDAO to update. Mock implementations can ignore this argument.
     * @param componentId The given parts ID to import from the warehouse.
     * @param quantity The quantity of the given parts ID to import from the warehouse.
     */
    void importPartsIdWithQuantityFromWarehouse(Warehouse targetWarehouse, LocationsAndContentsDAO dao, int componentId, int quantity);

    /**
     * Returns the given partsID of the given quantity to the target warehouse.
     * Implementations of this interface are recommended to also add said quantity to the warehouse itself.
     * @param targetWarehouse The target instance of the Warehouse interface to add parts back to.
     * @param dao The LocationsAndContentsDAO to update. Mock implementations can ignore this argument.
     * @param componentId The given parts ID to return to the warehouse.
     * @param quantity The quantity of the given parts ID to return to the warehouse.
     */
    void returnPartsIdWithQuantityToWarehouse(Warehouse targetWarehouse, LocationsAndContentsDAO dao, int componentId, int quantity);

    /**
     * Returns ALL parts currently allocated to the workstation to the target warehouse.
     * Implementations of this interface are required to add ALL parts back to the target warehouse unless targetWarehouse is null.
     * <p>NOTE: This function is intended to be called when destroying a workstation interface instance; there should not
     * be a need to call this function otherwise!</p>
     * @param targetWarehouse The target instance of the Warehouse interface to add all parts back to.
     * @param dao The LocationsAndContentsDAO to update. Mock implementations can ignore this argument.
     */
    void returnAllPartsToWarehouse (Warehouse targetWarehouse, LocationsAndContentsDAO dao);
}