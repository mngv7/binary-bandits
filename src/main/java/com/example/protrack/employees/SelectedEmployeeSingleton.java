package com.example.protrack.employees;

public class SelectedEmployeeSingleton {
    // Singleton instance
    private static SelectedEmployeeSingleton instance;

    // Selected employee details
    private String employeeFirstName;
    private String employeeLastName;

    // Private constructor to prevent external instantiation
    private SelectedEmployeeSingleton() {
    }

    // Get the single instance of the class
    public static SelectedEmployeeSingleton getInstance() {
        if (instance == null) {
            instance = new SelectedEmployeeSingleton();
        }
        return instance;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }
}
