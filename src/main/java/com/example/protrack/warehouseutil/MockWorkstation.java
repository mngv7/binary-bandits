package com.example.protrack.warehouseutil;

import java.util.ArrayList;
import java.util.List;

public class MockWorkstation implements Workstation {
    private int workstationId;
    private String workstationName;
    private String workstationLocation;
    private final List<partIdWithQuantity> partsId;
    private int maxParts;

    public MockWorkstation() {
        this.workstationId = 0;
        this.workstationName = "Default workstation";
        this.workstationLocation = "Default location";
        this.partsId = new ArrayList<>();
        this.maxParts = 100;
    }

    public MockWorkstation(int locationID, String locationAlias, int maxParts) {
        if (locationAlias == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        this.workstationId = locationID;
        this.workstationName = locationAlias;
        this.workstationLocation = "Spike Site A";
        this.partsId = new ArrayList<>();
        this.maxParts = maxParts;
    }

    public MockWorkstation(int locationID, String locationAlias, int maxParts, List<partIdWithQuantity> parts) {
        if (locationAlias == null || parts == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        this.workstationId = locationID; /* lmao even */
        this.workstationName = locationAlias;
        this.workstationLocation = "Spike Site A";
        this.partsId = parts;
        this.maxParts = maxParts;
    }

    public int getWorkstationLocationId() {
        return workstationId;
    }
    public void setWorkstationLocationId(int workstationId) {
        this.workstationId = workstationId;
    }

    public String getWorkstationLocationAlias() {
        return this.workstationName;
    }
    public void setWorkstationLocationAlias(String workstationName) {
        this.workstationName = workstationName;
    }

    public int getWorkstationMaxParts() { return this.maxParts; }
    public void setWorkstationMaxParts (int maxParts) { this.maxParts = maxParts; }

    public String getWorkstationLocation() {
        return this.workstationLocation;
    }
    public void setWorkstationLocation(String workstationLocation) {
        this.workstationLocation = workstationLocation;
    }

    /*
     * Imports a specific quantity of the given partsID from the target warehouse into this workstation.
     * This also removes said quantity of the given partsID from the warehouse.
     *
     * The Mock implementation deliberately leaves the dao variable unused and
     * only includes it for interface implementation requirements, it is
     * therefore safe to call this function with a null DAO.
     */
    public void importPartsIdWithQuantityFromWarehouse (Warehouse targetWarehouse,
                                                        LocationsAndContentsDAO dao,
                                                        int partsId,
                                                        int quantity) {
        for (int i = 0; i < this.partsId.size(); ++i) {
            if (this.partsId.get(i).partsId == partsId) {
                this.partsId.get(i).quantity += quantity;
                if (targetWarehouse != null)
                    targetWarehouse.removePartsIdWithQuantity(dao, partsId, quantity);
                return;
            }
        }

        partIdWithQuantity newPart = new partIdWithQuantity();
        newPart.partsId = partsId;
        newPart.quantity = quantity;
        this.partsId.add(newPart);
    }

    /*
     * Returns a specific quantity of the given partsID from the workstation to the target warehouse.
     * This also adds said quantity of the given partsID back to the warehouse.
     *
     * The Mock implementation deliberately leaves the dao variable unused and
     * only includes it for interface implementation requirements, it is
     * therefore safe to call this function with a null DAO.
     */
    public void returnPartsIdWithQuantityToWarehouse (Warehouse targetWarehouse,
                                                      LocationsAndContentsDAO dao,
                                                      int partsId,
                                                      int quantity) {
        for (int i = 0; i < this.partsId.size(); ++i) {
            if (this.partsId.get(i).partsId == partsId) {
                int amountToSubtract = quantity;
                if (this.partsId.get(i).quantity < quantity) {
                    System.out.println("WARNING: Attempting to remove more " + String.valueOf(partsId) + " than is in the workstation, truncating to current quantity.");
                    amountToSubtract = this.partsId.get(i).quantity;
                }
                this.partsId.get(i).quantity -= amountToSubtract;
                if (this.partsId.get(i).quantity <= 0) {
                    /* Remove part from workstation entirely. */
                    this.partsId.remove(this.partsId.get(i));
                    return;
                }
                if (targetWarehouse != null)
                    targetWarehouse.addPartsIdWithQuantity(dao, partsId, quantity);
                return; /* We don't need to progress any further. */
            }
        }
    }
}

