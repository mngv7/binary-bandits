package com.example.protrack.workstationutil;

public class SQLComponent implements Component {
    private int componentID; /* What's the ID of the component? */
    private String componentName; /* What's the component's name? */
    private int componentCount; /* How many of this component do we have? */

    public int getComponentID () {
        return componentID;
    }
    public void setComponentID (int componentID) {
        this.componentID = componentID;
    }

    public String getComponentName () {
        return this.componentName;
    }
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public int getComponentCount() {
        return this.componentCount;
    }
    public void setComponentCount(int componentCount) {
        this.componentCount = componentCount;
    }
}
