package com.example.protrack.utility;

/**
 * Singleton class to keep track of the currently logged-in user in the application.
 * It stores the employee ID of the logged-in user and provides access to it.
 */
public class LoggedInUserSingleton {
    // Singleton instance
    private static LoggedInUserSingleton instance;

    // Logged-in user's employee ID
    private Integer employeeId;

    // Private constructor to prevent external instantiation
    private LoggedInUserSingleton() {
    }

    /**
     * Retrieves the single instance of the LoggedInUserSingleton class.
     * If the instance does not exist, it is created.
     *
     * @return the singleton instance of LoggedInUserSingleton
     */
    public static LoggedInUserSingleton getInstance() {
        if (instance == null) {
            instance = new LoggedInUserSingleton();
        }
        return instance;
    }

    /**
     * Gets the employee ID of the logged-in user.
     *
     * @return the employee ID, or null if not set
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets the employee ID for the logged-in user.
     *
     * @param employeeId the employee ID to be set
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
