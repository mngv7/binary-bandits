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
        this.workstationId = 0; /* lol */
        this.workstationName = "Default workstation";
        this.workstationLocation = "Default location";
        this.partsId = new ArrayList<>();
        this.maxParts = 100;
    }

    public MockWorkstation(int workstationId) {
        this.workstationId = workstationId; /* lmao even */
        this.workstationName = "Default workstation";
        this.workstationLocation = "Default location";
        this.partsId = new ArrayList<>();
        this.maxParts = 100;
    }

    public MockWorkstation(int workstationId, String workstationName) {
        this.workstationId = workstationId; /* lmao even */
        this.workstationName = workstationName;
        this.workstationLocation = "Default Location";
        this.partsId = new ArrayList<>();
        this.maxParts = 100;
    }

    public MockWorkstation(int workstationId, String workstationName, String workstationLocation) {
        this.workstationId = workstationId; /* lmao even */
        this.workstationName = workstationName;
        this.workstationLocation = workstationLocation;
        this.partsId = new ArrayList<>();
        this.maxParts = 100;
    }

    public MockWorkstation(int workstationId, String workstationName, String workstationLocation, int maxParts) {
        this.workstationId = workstationId; /* lmao even */
        this.workstationName = workstationName;
        this.workstationLocation = workstationLocation;
        this.partsId = new ArrayList<>();
        this.maxParts = maxParts;
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

    public void importPartsIdWithQuantityFromWarehouse (Warehouse targetWarehouse, int partsId, int quantity) {
        for (int i = 0; i < this.partsId.size(); ++i) {
            if (this.partsId.get(i).partsId == partsId) {
                this.partsId.get(i).quantity += quantity;
                if (targetWarehouse != null)
                    targetWarehouse.removePartsIdWithQuantity(partsId, quantity);
                return;
            }
        }

        partIdWithQuantity newPart = new partIdWithQuantity();
        newPart.partsId = partsId;
        newPart.quantity = quantity;
        this.partsId.add(newPart);
    }

    public void returnPartsIdWithQuantityToWarehouse (Warehouse targetWarehouse, int partsId, int quantity) {
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
                    targetWarehouse.addPartsIdWithQuantity(partsId, quantity);
                return; /* We don't need to progress any further. */
            }
        }
    }
}

