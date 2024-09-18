package com.example.protrack.users;

import java.sql.Date;

public abstract class AbstractUser {
    // Protected fields for user attributes
    protected Integer employeeId;
    protected String firstName;
    protected String lastName;
    protected Date dob;
    protected String email;
    protected String phoneNo;
    protected String gender;
    protected String password;

    // Constructor to initialize the AbstractUser with given attributes
    public AbstractUser(Integer employeeId, String firstName, String lastName, Date dob, String email, String phoneNo, String gender, String password) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.email = email;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.password = password;
    }

    // Getter methods for user attributes
    public Integer getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    // Abstract method to get the access level of the user
    public abstract String getAccessLevel();
}