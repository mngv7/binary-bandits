package com.example.protrack.users;

import java.sql.Date;

public class WarehouseUser extends AbstractUser {
    // Define the access level for WarehouseUser
    private final String accessLevel;

    // Constructor initializes the WarehouseUser with specific attributes
    public WarehouseUser(Integer employeeId, String firstName, String lastName, Date dob, String email, String phoneNo, String gender, String password) {
        super(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
        this.accessLevel = "MEDIUM";  // Set access level specific to WarehouseUser
    }

    // Override method to return the access level of the user
    @Override
    public String getAccessLevel() {
        return accessLevel;
    }
}
