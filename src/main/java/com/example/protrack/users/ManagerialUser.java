package com.example.protrack.users;

public class ManagerialUser extends AbstractUser {
    public ManagerialUser(int employeeId, String firstName, String lastName, String password) {
        super(employeeId, firstName, lastName, password);
    }

    @Override
    public String getAccessLevel() {
        return "High";
    }
}
