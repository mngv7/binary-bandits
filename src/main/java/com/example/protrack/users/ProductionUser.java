package com.example.protrack.users;

import java.sql.Date;

public class ProductionUser extends AbstractUser {
    // Define the access level for ProductionUser
    private final String accessLevel;

    // Constructor initializes the ProductionUser with specific attributes
    public ProductionUser(Integer employeeId, String firstName, String lastName, Date dob, String email, String phoneNo, String gender, String password) {
        super(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);

        if (employeeId == null || firstName == null || lastName == null || dob == null || email == null || password == null) {
            throw new IllegalArgumentException("No field can be null");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Email must contain an '@' and a '.'");
        }
        if (employeeId <= 0) {
            throw new IllegalArgumentException("Employee ID must be a positive integer");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        this.accessLevel = "LOW";  // Set access level specific to ProductionUser
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