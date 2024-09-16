package com.example.protrack.warehouseutil;

public interface Warehouse {
    String getWarehouseName();
    void setWarehouseName(String warehouseName);
    String getWarehouseLocation();
    void setWarehouseLocation(String warehouseLocation);
    int getWarehouseId();
    void setWarehouseId(int warehouseId);
    int getMaxParts();
    void setMaxParts(int maxParts);
    void addPartsIdWithQuantity (int partsId, int quantity);
    void removePartsIdWithQuantity (int partsId, int quantity);
}
