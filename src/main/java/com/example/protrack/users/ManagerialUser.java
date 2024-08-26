package com.example.protrack.users;

public class ManagerialUser extends AbstractUser {
    public ManagerialUser(String employeeId, String firstName, String lastName, String password) {
        super(employeeId, firstName, lastName, password);
    }

    @Override
    public void getAccessLevel() {

    }
}
