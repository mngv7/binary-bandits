package com.example.protrack.warehouseutil;

public interface Warehouse {
    /* Getters and setters. Self-explanatory. */
    String getWarehouseName();
    void setWarehouseName(String warehouseName);
    String getWarehouseLocation();
    void setWarehouseLocation(String warehouseLocation);
    int getWarehouseId();
    void setWarehouseId(int warehouseId);
    int getMaxParts();
    void setMaxParts(int maxParts);

    /*
     * These functions add and remove quantities of the given partsID to
     * and from the warehouse in the underlying implementation of this interface.
     *
     * These functions generally need a DAO variable, however some implementations leave
     * it unused. Such cases are explicitly labeled in their implementation's code comments.
     */
    void addPartsIdWithQuantity (LocationsAndContentsDAO dao, int partsId, int quantity);
    void removePartsIdWithQuantity (LocationsAndContentsDAO dao, int partsId, int quantity);
}
