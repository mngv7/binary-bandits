package com.example.protrack.users;

import java.sql.Date;

public class ProductionUser extends AbstractUser {
    // Define the access level for ProductionUser
    private final String accessLevel;

    // Constructor initializes the ProductionUser with specific attributes
    public ProductionUser(Integer employeeId, String firstName, String lastName, Date dob, String email, String phoneNo, String gender, String password) {
        super(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
        this.accessLevel = "LOW";  // Set access level specific to ProductionUser
    }

    // Override method to return the access level of the user
    @Override
    public String getAccessLevel() {
        return accessLevel;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.getLastName(); // Adjust according to your attribute
    }
}
