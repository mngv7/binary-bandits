package com.example.protrack.users;

public class WarehouseUser extends AbstractUser {
    public WarehouseUser(int employeeId, String firstName, String lastName, String password) {
        super(employeeId, firstName, lastName, password);
    }

    @Override
    public String getAccessLevel() {
        return "Low";
    }
}
