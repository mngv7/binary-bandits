package com.example.protrack.users;

public abstract class AbstractUser {

    protected String employeeId;
    protected String firstName;
    protected String lastName;
    protected String password;

    public abstract void getAccessLevel();

    public String getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}