package com.example.protrack.warehouseutil;

public interface Workstation {
    String getWorkstationName();
    void setWorkstationName(String workstationName);
    String getWorkstationLocation();
    void setWorkstationLocation(String workstationLocation);
    int getWorkstationId();
    void setWorkstationId(int workstationID);
    void addPartsIdWithQuantity (int componentId, int quantity);
    void removePartsIdWithQuantity (int componentId, int quantity);
}