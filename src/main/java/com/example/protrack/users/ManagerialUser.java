package com.example.protrack.users;

import java.sql.Date;

public class ManagerialUser extends AbstractUser {
    // Define the access level for ManagerialUser
    private final String accessLevel;

    // Constructor initializes the ManagerialUser with specific attributes
    public ManagerialUser(Integer employeeId, String firstName, String lastName, Date dob, String email, String phoneNo, String gender, String password) {
        super(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
        if (employeeId == null || firstName == null || lastName == null || dob == null || email == null || password == null) {
            throw new IllegalArgumentException("None of the fields can be null");
        }
        // Validates email (to a degree)
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email must contain an '@' and a '.'");
        }
        if (employeeId <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive integer");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        this.accessLevel = "HIGH";  // Set access level specific to ManagerialUser
    }

    // Override method to return the access level of the user
    @Override
    public String getAccessLevel() {
        return accessLevel;
    }

    // Override method to return user full name
    @Override
    public String toString() {
        return this.firstName + " " + this.getLastName();
    }
}