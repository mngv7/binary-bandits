package com.example.protrack.warehouseutil;

import java.util.ArrayList;
import java.util.List;

public class MockWarehouse implements Warehouse {
    private final List<partIdWithQuantity> partsId;
    public int maxParts;
    private int warehouseId;
    private String warehouseName;
    private String warehouseLocation;

    public MockWarehouse() {
        this.warehouseId = 0;
        this.warehouseName = "Default Warehouse";
        this.warehouseLocation = "420 Blazeit ave. Brisbane QLD";
        this.partsId = new ArrayList<>();
    }

    public MockWarehouse(int locationId, String locationAlias, int maxParts) {
        if (locationAlias == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        this.warehouseId = locationId;
        this.warehouseName = locationAlias;
        this.warehouseLocation = "420 Blazeit ave. Brisbane QLD";
        this.partsId = new ArrayList<>();
        this.maxParts = maxParts;
    }

    public MockWarehouse(int locationId, String locationAlias, int maxParts, List<partIdWithQuantity> parts) {
        if (locationAlias == null || parts == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        this.warehouseId = locationId;
        this.warehouseName = locationAlias;
        this.warehouseLocation = "420 Blazeit ave. Brisbane QLD";
        this.partsId = parts;
        this.maxParts = maxParts;
    }

    public int getWarehouseLocationId() {
        return this.warehouseId;
    }

    public void setWarehouseLocationId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseLocationAlias() {
        return this.warehouseName;
    }

    public void setWarehouseLocationAlias(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getWarehouseMaxParts() {
        return this.maxParts;
    }

    public void setWarehouseMaxParts(int maxParts) {
        this.maxParts = maxParts;
    }

    public String getWarehouseLocation() {
        return this.warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    /*
     * Adds a specific quantity of the given partsID to the warehouse.
     * The Mock implementation deliberately leaves the dao variable unused and
     * only includes it for interface implementation requirements, it is
     * therefore safe to call this function with a null DAO.
     */
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
    }

    /*
     * Removes a specific quantity of the given partsID from the warehouse.
     * The Mock implementation deliberately leaves the dao variable unused and
     * only includes it for interface implementation requirements, it is
     * therefore safe to call this function with a null DAO.
     */
    public void removePartsIdWithQuantity(LocationsAndContentsDAO dao, int partsId, int quantity) {
        for (int i = 0; i < this.partsId.size(); ++i) {
            if (this.partsId.get(i).partsId == partsId) {
                int amountToSubtract = quantity;
                if (this.partsId.get(i).quantity < quantity) {
                    System.out.println("WARNING: Attempting to remove more " + partsId + " than is in the warehouse, truncating to current quantity.");
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
