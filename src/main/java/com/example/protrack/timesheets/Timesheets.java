package com.example.protrack.timesheets;

import java.time.LocalDateTime;

public class Timesheets {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer employeeID;
    private Integer productOrderID;

    // Constructor initializes the timesheets with specific attributes
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

    // Getter for the Timesheets start time
    public LocalDateTime getStartTime() { return startTime; }

    // Getter for the Timesheets start time
    public LocalDateTime getEndTime() { return endTime; }

    // Getter for the Timesheets employee ID
    public Integer getEmployeeID() { return employeeID; }

    // Getter for the Timesheets Product order ID
    public Integer getProductOrderID() { return productOrderID; }

}
