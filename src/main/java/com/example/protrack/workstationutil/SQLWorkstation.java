package com.example.protrack.workstationutil;

import java.util.ArrayList;
import java.util.List;

public class SQLWorkstation implements Workstation {
    private String workstationName;
    private int workstationID;
    public List<Component> componentIDs;

    public SQLWorkstation() {
        this.workstationID = 0; /* lol */
        this.componentIDs = new ArrayList<>();
    }

    public SQLWorkstation(String workstationName) {
        this.workstationName = workstationName;
        this.workstationID = 0; /* lmao even */
        this.componentIDs = new ArrayList<>();
    }

    public SQLWorkstation(String workstationName, int workstationID) {
        this.workstationName = workstationName;
        this.workstationID = workstationID;
        this.componentIDs = new ArrayList<>();
    }

    public SQLWorkstation(String workstationName, int workstationID, List<Component> componentIDs) {
        this.workstationName = workstationName;
        this.workstationID = workstationID;
        this.componentIDs = componentIDs;
    }

    public String getWorkstationName() {
        return this.workstationName;
    }

    public void setWorkstationName (String workstationName) {
        this.workstationName = workstationName;
    }

    public int getWorkstationID() {
        return workstationID;
    }
    public void setWorkstationID(int workstationID) {
        this.workstationID = workstationID;
    }

    public void addComponentID (int componentID) {
        for (int i = 0; i < this.componentIDs.size(); ++i) {
            if (this.componentIDs.get(i).getComponentID() == componentID) {
                /* TODO: Too much OOP? */
                int currentCount = this.componentIDs.get(i).getComponentCount();
                this.componentIDs.get(i).setComponentCount(currentCount + 1);
                return; /* We don't need to progress any further. */
            }
        }

        /* TODO: We should fetch the component ID info from the database to fill in the new component. */
        Component newComponent = new SQLComponent();
        newComponent.setComponentID(componentID);
        this.componentIDs.add(newComponent);
    }

    public void removeComponentID (int componentID) {
        for (int i = 0; i < this.componentIDs.size(); ++i) {
            if (this.componentIDs.get(i).getComponentID() == componentID) {
                /* TODO: Too much OOP? */
                int currentCount = this.componentIDs.get(i).getComponentCount();
                if (currentCount <= 0) {
                    /* Remove part from workstation entirely. TODO: Do we keep a record with 0 parts? */
                    this.componentIDs.remove(this.componentIDs.get(i));
                    return;
                }
                this.componentIDs.get(i).setComponentCount(currentCount - 1);
                return; /* We don't need to progress any further. */
            }
        }
    }
}

