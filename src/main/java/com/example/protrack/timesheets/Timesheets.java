package com.example.protrack.timesheets;

import com.example.protrack.productbuild.ProductBuild;
import com.example.protrack.productbuild.ProductBuildDAO;
import com.example.protrack.users.ProductionUser;

import java.time.LocalDateTime;

public class Timesheets {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ProductionUser employeeID;
    private ProductBuild productOrderID;

    public Timesheets (LocalDateTime startTime, LocalDateTime endTime, ProductionUser employeeID, ProductBuild productOrderID) {

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

    public ProductionUser getEmployeeID() { return employeeID; }

    public void setEmployeeID(ProductionUser employeeID) {this.employeeID = employeeID; }

    public ProductBuild getProductOrderID() { return productOrderID; }

    public void setProductOrderID(ProductBuild productOrderID) {this.productOrderID = productOrderID; }
}
