package com.example.protrack.users;

public class WarehouseUser extends AbstractUser {
    public WarehouseUser(String employeeId, String firstName, String lastName, String password) {
        super(employeeId, firstName, lastName, password);
    }

    @Override
    public void getAccessLevel() {

    }
}
