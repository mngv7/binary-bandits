package com.example.protrack.users;

public class ProductionUser extends AbstractUser {
    private final String accessLevel;

    public ProductionUser(int employeeId, String firstName, String lastName, String password) {
        super(employeeId, firstName, lastName, password);
        this.accessLevel = "LOW";
    }

    @Override
    public String getAccessLevel() {
        return accessLevel;
    }
}
