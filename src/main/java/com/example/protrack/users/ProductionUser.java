package com.example.protrack.users;

public class ProductionUser extends AbstractUser {
    public ProductionUser(String employeeId, String firstName, String lastName, String password) {
        super(employeeId, firstName, lastName, password);
    }

    @Override
    public void getAccessLevel() {

    }
}
