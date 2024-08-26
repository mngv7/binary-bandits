package com.example.protrack.users;

public abstract class AbstractUser {

    protected int employeeId;
    protected String firstName;
    protected String lastName;
    protected String password;

    public AbstractUser(int employeeId, String firstName, String lastName, String password) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;

    }

    public int getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public abstract String getAccessLevel();
}