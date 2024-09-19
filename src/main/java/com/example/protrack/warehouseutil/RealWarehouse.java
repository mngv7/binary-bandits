package com.example.protrack.warehouseutil;

import java.util.ArrayList;
import java.util.List;

public class RealWarehouse implements Warehouse {
    private int warehouseId;
    private String warehouseName;
    private String warehouseLocation;
    private final List<partIdWithQuantity> partsId;
    private int maxParts;

    public RealWarehouse () {
        this.warehouseId = 0;
        this.warehouseName = "Default Warehouse";
        this.warehouseLocation = "Default location"; /* TODO: Unused by DAO */
        this.partsId = new ArrayList<>();
        this.maxParts = 5000;
    }

    public RealWarehouse (int locationID, String locationAlias, int locationCapacity) {
        this.warehouseId = locationID;
        this.warehouseName = locationAlias;
        this.warehouseLocation = "Lotus"; /* TODO: Unused by DAO */
        this.partsId = new ArrayList<>();
        this.maxParts = locationCapacity;
    }

    public RealWarehouse (int locationID, String locationAlias, int locationCapacity, List<partIdWithQuantity> partsLinked) {
        this.warehouseId = locationID;
        this.warehouseName = locationAlias;
        this.warehouseLocation = "Lotus"; /* TODO: Unused by DAO */
        this.partsId = partsLinked;
        this.maxParts = locationCapacity;
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
     * Adds a specific quantity of the given partsID to the Warehouse.
     * RealWorkstation utilises this function when returning stock.
     * It can also be called directly to add parts to a given Warehouse.
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

        dao.insertPartsIdWithQuantityIntoLocation(this.warehouseId, newPart);
    }

    /*
     * Removes a specific quantity of the given partsID from the warehouse.
     * RealWorkstation utilises this function when importing stock.
     * It can also be called directly to remove parts from a given Warehouse.
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
                dao.removePartsIdWithQuantityFromLocation(this.warehouseId, this.partsId.get(i));
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
