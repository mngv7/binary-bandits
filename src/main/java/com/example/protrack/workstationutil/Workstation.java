package com.example.protrack.workstationutil;

import java.util.*;

public interface Workstation {
    String getWorkstationName();
    void setWorkstationName(String workstationName);
    int getWorkstationID();
    void setWorkstationID(int workstationID);
    void addComponentID (int componentID);
    void removeComponentID (int componentID);
}
