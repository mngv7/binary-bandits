package com.example.protrack.warehouseutil;

import java.util.ArrayList;
import java.util.List;

public class RealWarehouse implements Warehouse {
    private final List<partIdWithQuantity> partsId;
    private int warehouseId;
    private String warehouseName;
    private String warehouseLocation;
    private int maxParts;

    public RealWarehouse() {
        this.warehouseId = 0;
        this.warehouseName = "Default Warehouse";
        this.warehouseLocation = "420 Blazeit ave. Brisbane QLD"; /* TODO: Unused by DAO */
        this.partsId = new ArrayList<>();
        this.maxParts = 5000;
    }

    public RealWarehouse(int locationId, String locationAlias, int locationCapacity) {
        if (locationAlias == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        this.warehouseId = locationId;
        this.warehouseName = locationAlias;
        this.warehouseLocation = "420 Blazeit ave. Brisbane QLD"; /* TODO: Unused by DAO */
        this.partsId = new ArrayList<>();
        this.maxParts = locationCapacity;
    }

    public RealWarehouse(int locationId, String locationAlias, int locationCapacity, List<partIdWithQuantity> partsLinked) {
        if (locationAlias == null || partsLinked == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        this.warehouseId = locationId;
        this.warehouseName = locationAlias;
        this.warehouseLocation = "420 Blazeit ave. Brisbane QLD"; /* TODO: Unused by DAO */
        this.partsId = partsLinked;
        this.maxParts = locationCapacity;
    }

    public String getWarehouseLocationAlias() {
        return this.warehouseName;
    }

    public void setWarehouseLocationAlias(String warehouseName) {
        if (warehouseName == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        this.warehouseName = warehouseName;
    }

    public String getWarehouseLocation() {
        return this.warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        if (warehouseLocation == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        this.warehouseLocation = warehouseLocation;
    }

    public int getWarehouseLocationId() {
        return this.warehouseId;
    }

    public void setWarehouseLocationId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getWarehouseMaxParts() {
        return this.maxParts;
    }

    public void setWarehouseMaxParts(int maxParts) {
        this.maxParts = maxParts;
    }

    public void addPartsIdWithQuantity(LocationsAndContentsDAO dao, int partsId, int quantity) {
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

    public void removePartsIdWithQuantity(LocationsAndContentsDAO dao, int partsId, int quantity) {
        for (int i = 0; i < this.partsId.size(); ++i) {
            if (this.partsId.get(i).partsId == partsId) {
                int amountToSubtract = quantity;
                if (this.partsId.get(i).quantity < quantity) {
                    System.out.println("WARNING: Attempting to remove more " + partsId + " than is in the warehouse, truncating to current quantity.");
                    amountToSubtract = this.partsId.get(i).quantity;
                }
                this.partsId.get(i).quantity -= amountToSubtract;
                dao.removePartsIdWithQuantityFromLocation(this.warehouseId, this.partsId.get(i));
                if (this.partsId.get(i).quantity <= 0) {
                    this.partsId.remove(this.partsId.get(i));
                    return;
                }
                return;
            }
        }
    }
}