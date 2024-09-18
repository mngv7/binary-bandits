package com.example.protrack.users;

import java.sql.Date;

public class ManagerialUser extends AbstractUser {
    // Define the access level for ManagerialUser
    private final String accessLevel;

    // Constructor initializes the ManagerialUser with specific attributes
    public ManagerialUser(Integer employeeId, String firstName, String lastName, Date dob, String email, String phoneNo, String gender, String password) {
        super(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
        this.accessLevel = "HIGH";  // Set access level specific to ManagerialUser
    }

    // Override method to return the access level of the user
    @Override
    public String getAccessLevel() {
        return accessLevel;
    }
}