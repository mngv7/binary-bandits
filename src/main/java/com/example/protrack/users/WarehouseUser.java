package com.example.protrack.users;

public class WarehouseUser extends AbstractUser {
    private final String accessLevel;

    public WarehouseUser(int employeeId, String firstName, String lastName, String password) {
        super(employeeId, firstName, lastName, password);
        this.accessLevel = "MEDIUM";
    }

    @Override
    public String getAccessLevel() {
        return accessLevel;
    }
}
