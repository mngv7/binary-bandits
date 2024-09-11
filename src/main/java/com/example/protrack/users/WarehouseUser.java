package com.example.protrack.users;

import java.sql.Date;

public class WarehouseUser extends AbstractUser {
    private final String accessLevel;

    public WarehouseUser(Integer employeeId, String firstName, String lastName, Date dob, String email, String phoneNo, String gender, String password) {
        super(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
        this.accessLevel = "MEDIUM";
    }

    @Override
    public String getAccessLevel() {
        return accessLevel;
    }
}