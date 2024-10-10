package com.example.protrack.timesheets;

import java.time.LocalDateTime;

/**
 * Represents a timesheet entry for an employee working on a product build.
 * Contains information about start time, end time, employee ID, and product order ID.
 */
public class Timesheets {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer employeeID;
    private Integer productOrderID;

    /**
     * Constructs a Timesheets object with the specified start time, end time, employee ID, and product order ID.
     * @param startTime the start time of the timesheet entry
     * @param endTime the end time of the timesheet entry
     * @param employeeID the ID of the employee
     * @param productOrderID the ID of the product order
     * @throws IllegalArgumentException if any of the fields are null or if the end time is in the future
     */
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

    /**
     * Returns the start time of the timesheet.
     * @return the start time
     */
    public LocalDateTime getStartTime() { return startTime; }

    /**
     * Returns the end time of the timesheet.
     * @return the end time
     */
    public LocalDateTime getEndTime() { return endTime; }

    /**
     * Returns the employee ID associated with this timesheet.
     * @return the employee ID
     */
    public Integer getEmployeeID() { return employeeID; }

    /**
     * Returns the product order ID associated with this timesheet.
     * @return the product order ID
     */
    public Integer getProductOrderID() { return productOrderID; }

}
