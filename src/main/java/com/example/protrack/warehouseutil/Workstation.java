package com.example.protrack.warehouseutil;

public interface Workstation {
    String getWorkstationName();
    void setWorkstationName(String workstationName);
    String getWorkstationLocation();
    void setWorkstationLocation(String workstationLocation);
    int getWorkstationId();
    void setWorkstationId(int workstationID);
    int getWorkstationMaxParts();
    void setWorkstationMaxParts(int maxParts);
    void importPartsIdWithQuantityFromWarehouse(Warehouse targetWarehouse, int componentId, int quantity);
    void returnPartsIdWithQuantityToWarehouse(Warehouse targetWarehouse, int componentId, int quantity);
}