package com.example.protrack.warehouseutil;

import java.util.ArrayList;
import java.util.List;

public class RealWorkstation implements Workstation {
    private int workstationId;
    private String workstationName;
    private String workstationLocation;
    private final List<partIdWithQuantity> partsId;
    private int maxParts;

    public RealWorkstation () {
        this.workstationId = 0;
        this.workstationName = "Workstation 1";
        this.workstationLocation = "Spike Site A";
        this.maxParts = 200;
        this.partsId = new ArrayList<>();
    }

    public RealWorkstation (int locationID, String locationAlias, int maxParts) {
        this.workstationId = locationID;
        this.workstationName = locationAlias;
        this.workstationLocation = "Spike Site A";
        this.maxParts = maxParts;
        this.partsId = new ArrayList<>();
    }

    public RealWorkstation (int locationID, String locationAlias, int maxParts, List<partIdWithQuantity> parts) {
        this.workstationId = locationID;
        this.workstationName = locationAlias;
        this.workstationLocation = "Spike Site A";
        this.maxParts = maxParts;
        this.partsId = parts;
    }

    public String getWorkstationName() {
        return this.workstationName;
    }

    public void setWorkstationName (String workstationName) {
        this.workstationName = workstationName;
    }

    public String getWorkstationLocation() {
        return this.workstationLocation;
    }

    public void setWorkstationLocation(String workstationLocation) {
        this.workstationLocation = workstationLocation;
    }

    public int getWorkstationId() {
        return workstationId;
    }
    public void setWorkstationId(int workstationId) {
        this.workstationId = workstationId;
    }

    public int getWorkstationMaxParts() { return this.maxParts; }
    public void setWorkstationMaxParts (int maxParts) { this.maxParts = maxParts; }

    /*
     * Imports a specific quantity of the given partsID from the target warehouse into this workstation.
     * This also removes said quantity of the given partsID from the warehouse.
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

        dao.insertPartsIdWithQuantityIntoLocation (this.workstationId, newPart);
    }

    /*
     * Returns a specific quantity of the given partsID to the target warehouse from this workstation.
     * This also adds back said quantity of the given partsID to the warehouse.
     */
    public void returnPartsIdWithQuantityToWarehouse (Warehouse targetWarehouse, LocationsAndContentsDAO dao, int partsId, int quantity) {
        for (int i = 0; i < this.partsId.size(); ++i) {
            if (this.partsId.get(i).partsId == partsId) {
                int amountToSubtract = quantity;
                if (this.partsId.get(i).quantity < quantity) {
                    System.out.println("WARNING: Attempting to remove more " + String.valueOf(partsId) + " than is in the workstation, truncating to current quantity.");
                    amountToSubtract = this.partsId.get(i).quantity;
                }
                this.partsId.get(i).quantity -= amountToSubtract;
                dao.removePartsIdWithQuantityFromLocation(this.workstationId, this.partsId.get(i));
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

    @Override
    public String toString() {return this.workstationName;}
}
