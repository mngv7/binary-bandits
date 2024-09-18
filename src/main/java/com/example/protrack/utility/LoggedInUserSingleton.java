package com.example.protrack.utility;

public class LoggedInUserSingleton {
    // Singleton instance
    private static LoggedInUserSingleton instance;

    // Logged-in user's employee ID
    private Integer employeeId;

    // Private constructor to prevent external instantiation
    private LoggedInUserSingleton() {
    }

    // Get the single instance of the class
    public static LoggedInUserSingleton getInstance() {
        if (instance == null) {
            instance = new LoggedInUserSingleton();
        }
        return instance;
    }

    // Get the employee ID
    public Integer getEmployeeId() {
        return employeeId;
    }

    // Set the employee ID
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}