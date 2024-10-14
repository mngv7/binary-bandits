package com.example.protrack.warehouseutil;

public interface Warehouse {
    /**
     * Gets the location ID of a warehouse.
     * @return the location ID as an integer.
     */
    int getWarehouseLocationId();

    /**
     * Set the location ID of a warehouse to a specific value.
     * NOTE: In ProTrack, there is only one warehouse, therefore this
     *       value is usually always 0.
     * @param warehouseId the location ID to set this warehouse to.
     */
    void setWarehouseLocationId(int warehouseId);

    /**
     * Gets the location alias of the warehouse.
     * @return A string containing the location alias.
     */
    String getWarehouseLocationAlias();

    /**
     * Sets the location alias of the warehouse.
     * @param warehouseName the new location alias to set on the warehouse.
     */
    void setWarehouseLocationAlias(String warehouseName);

    /**
     * Gets the capacity of a warehouse.
     * @return An integer describing the maximum number of parts a warehouse can hold.
     */
    int getWarehouseMaxParts();

    /**
     * Sets the capacity of a warehouse.
     * @param maxParts = The new capacity for the warehouse.
     */
    void setWarehouseMaxParts(int maxParts);

    /* TODO: See if these are needed anymore. */
    String getWarehouseLocation();
    void setWarehouseLocation(String warehouseLocation);

    /*
     * These functions add and remove quantities of the given partsID to
     * and from the warehouse in the underlying implementation of this interface.
     *
     * These functions generally need a DAO variable, however some implementations leave
     * it unused. Such cases are explicitly labeled in their implementation's code comments.
     */

    /**
     * Adds a specific quantity of the given parts ID to a warehouse.
     * Such part ID numbers ideally should be cross-referenced with their corresponding records
     * retrieved from the Parts DAO.
     * @param dao The LocationsAndContentsDAO instance to utilise for database access.
     *            Mock implementations may choose to ignore this argument.
     * @param partsId The ID of the part to add to the warehouse.
     * @param quantity The quantity of the part to add to the warehouse.
     */
    void addPartsIdWithQuantity (LocationsAndContentsDAO dao, int partsId, int quantity);

    /**
     * Removes a specific quantity of the given parts ID to a warehouse, with a quantity higher than
     * the remaining quantity of the given parts ID resulting in removal of the partsID record from the warehouse.
     * Such part ID numbers ideally should be cross-referenced with their corresponding records
     * retrieved from the Parts DAO.
     * @param dao The LocationsAndContentsDAO instance to utilise for database access.
     *            Mock implementations may choose to ignore this argument.
     * @param partsId The ID of the part to remove from the warehouse.
     * @param quantity The quantity of the part to remove from the warehouse.
     *                 The behaviour in the case of a quantity higher than what's remaining is implementation defined.
     */
    void removePartsIdWithQuantity (LocationsAndContentsDAO dao, int partsId, int quantity);
}
