package com.example.protrack.timesheets;

import com.example.protrack.productbuild.ProductBuild;
import com.example.protrack.productbuild.ProductBuildDAO;
import com.example.protrack.users.ProductionUser;

import java.time.LocalDateTime;

public class Timesheets {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer employeeID;
    private Integer productOrderID;

    public Timesheets (LocalDateTime startTime, LocalDateTime endTime, Integer employeeID, Integer productOrderID) {
        if (startTime == null || endTime == null || employeeID == null || productOrderID == null) {
            throw new IllegalArgumentException("No fields can be null");
        }
        if (endTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("End Time must be past/current time");
        }

        this.startTime = startTime;
        this.endTime = endTime;
        this.employeeID = employeeID;
        this.productOrderID = productOrderID;
    }

    public LocalDateTime getStartTime() { return startTime; }

    public LocalDateTime getEndTime() { return endTime; }

    public Integer getEmployeeID() { return employeeID; }

    public void setEmployeeID(Integer employeeID) {this.employeeID = employeeID; }

    public Integer getProductOrderID() { return productOrderID; }

    public void setProductOrderID(Integer productOrderID) {this.productOrderID = productOrderID; }
}
