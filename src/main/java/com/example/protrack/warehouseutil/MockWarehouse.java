package com.example.protrack.warehouseutil;

import java.util.List;
import java.util.ArrayList;

public class MockWarehouse {
    private int warehouseId;
    private String warehouseName;
    private String warehouseLocation;
    public int maxParts;
    private final List<partIdWithQuantity> partsId;

    public MockWarehouse() {
        this.warehouseId = 0;
        this.warehouseName = "Default Warehouse";
        this.warehouseLocation = "Spike Site A";
        this.partsId = new ArrayList<>();
    }

    public MockWarehouse(int warehouseId) {
        this.warehouseId = warehouseId;
        this.warehouseName = "Default Warehouse";
        this.warehouseLocation = "Spike Site A";
        this.partsId = new ArrayList<>();
    }

    public MockWarehouse(int warehouseId, String warehouseName) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.warehouseLocation = "Spike Site A";
        this.partsId = new ArrayList<>();
    }

    public MockWarehouse(int warehouseId, String warehouseName, String warehouseLocation) {
        this.warehouseId = warehouseId;
        this.warehouseName = warehouseName;
        this.warehouseLocation = warehouseLocation;
        this.partsId = new ArrayList<>();
    }

    public String getWarehouseName() {
        return this.warehouseName;
    }
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    public String getWarehouseLocation() {
        return this.warehouseLocation;
    }
    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }
    public int getWarehouseId() {
        return this.warehouseId;
    }
    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }
    public int getMaxParts() { return this.maxParts; }
    public void setMaxParts(int maxParts) { this.maxParts = maxParts; }

    /*
     * Adds a specific quantity of the given partsID to the warehouse.
     * The Mock implementation deliberately leaves the dao variable unused and
     * only includes it for interface implementation requirements, it is
     * therefore safe to call this function with a null DAO.
     */
    public void addPartsIdWithQuantity (LocationsAndContentsDAO dao, int partsId, int quantity) {
        for (int i = 0; i < this.partsId.size(); ++i) {
            if (this.partsId.get(i).partsId == partsId) {
                this.partsId.get(i).quantity += quantity;
                return;
            }
        }

        partIdWithQuantity newPart = new partIdWithQuantity();
        newPart.partsId = partsId;
        newPart.quantity = quantity;
        this.partsId.add(newPart);
    }

    /*
     * Removes a specific quantity of the given partsID from the warehouse.
     * The Mock implementation deliberately leaves the dao variable unused and
     * only includes it for interface implementation requirements, it is
     * therefore safe to call this function with a null DAO.
     */
    public void removePartsIdWithQuantity (LocationsAndContentsDAO dao, int partsId, int quantity) {
        for (int i = 0; i < this.partsId.size(); ++i) {
            if (this.partsId.get(i).partsId == partsId) {
                int amountToSubtract = quantity;
                if (this.partsId.get(i).quantity < quantity) {
                    System.out.println("WARNING: Attempting to remove more " + String.valueOf(partsId) + " than is in the warehouse, truncating to current quantity.");
                    amountToSubtract = this.partsId.get(i).quantity;
                }
                this.partsId.get(i).quantity -= amountToSubtract;
                if (this.partsId.get(i).quantity <= 0) {
                    /* Remove part from workstation entirely. */
                    this.partsId.remove(this.partsId.get(i));
                    return;
                }
                return; /* We don't need to progress any further. */
            }
        }
    }
}
