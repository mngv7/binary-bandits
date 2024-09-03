package com.example.protrack.workstationutil;

public interface Component {
    int getComponentID ();
    void setComponentID (int componentID);

    String getComponentName ();
    void setComponentName(String componentName);

    int getComponentCount();
    void setComponentCount(int componentCount);
}
