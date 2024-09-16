package com.example.protrack.users;

import java.sql.Date;

public class ProductionUser extends AbstractUser {
    private final String accessLevel;

    public ProductionUser(Integer employeeId, String firstName, String lastName, Date dob, String email, String phoneNo, String gender, String password) {
        super(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
        this.accessLevel = "LOW";
    }

    @Override
    public String getAccessLevel() {
        return accessLevel;
    }
}