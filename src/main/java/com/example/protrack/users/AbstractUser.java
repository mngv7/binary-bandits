package com.example.protrack.users;

public abstract class AbstractUser {

    protected String employeeId;
    protected String firstName;
    protected String lastName;
    protected String password;

    public AbstractUser(String employeeId, String firstName, String lastName, String password) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;

    }

    public String getEmployeeId() {
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

    public abstract void getAccessLevel();
}