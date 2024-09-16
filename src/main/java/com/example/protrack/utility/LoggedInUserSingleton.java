package com.example.protrack.utility;

public class LoggedInUserSingleton {
    private static LoggedInUserSingleton instance;
    private Integer employeeId;

    private LoggedInUserSingleton() {
    }

    public static LoggedInUserSingleton getInstance() {
        if (instance == null) {
            instance = new LoggedInUserSingleton();
        }
        return instance;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
