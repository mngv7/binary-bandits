package com.example.protrack.warehouseutil;

public interface Warehouse {
    String getWarehouseName();
    void setWarehouseName(String workstationName);
    String getWarehouseLocation();
    void setWarehouseLocation(String workstationLocation);
    int getWarehouseId();
    void setWarehouseId(int workstationID);
    void addPartsIdWithQuantity (int partsId, int quantity);
    void removePartsIdWithQuantity (int partsId, int quantity);
}
