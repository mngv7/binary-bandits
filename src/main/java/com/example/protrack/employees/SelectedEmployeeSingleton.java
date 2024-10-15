package com.example.protrack.employees;

/**
 * Singleton class to store details of the currently selected employee.
 * This ensures that only one instance of the selected employee is available,
 * allowing the program to remember which employee is selected when expanding the view.
 */
public class SelectedEmployeeSingleton {

    // Singleton instance
    private static SelectedEmployeeSingleton instance;

    // Selected employee details
    private String employeeFirstName;
    private String employeeLastName;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Ensures that only one instance of this class is created.
     */
    private SelectedEmployeeSingleton() {
    }

    /**
     * Retrieves the singleton instance of the SelectedEmployeeSingleton class.
     *
     * @return The single instance of the SelectedEmployeeSingleton class.
     */
    public static SelectedEmployeeSingleton getInstance() {
        if (instance == null) {
            instance = new SelectedEmployeeSingleton();
        }
        return instance;
    }

    /**
     * Retrieves the first name of the selected employee.
     *
     * @return The first name of the selected employee.
     */
    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    /**
     * Sets the first name of the selected employee.
     *
     * @param employeeFirstName The first name of the selected employee.
     */
    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    /**
     * Retrieves the last name of the selected employee.
     *
     * @return The last name of the selected employee.
     */
    public String getEmployeeLastName() {
        return employeeLastName;
    }

    /**
     * Sets the last name of the selected employee.
     *
     * @param employeeLastName The last name of the selected employee.
     */
    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }
}
