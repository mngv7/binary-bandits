package com.example.protrack.users;

public class ManagerialUser extends AbstractUser {
    private final String accessLevel;

    public ManagerialUser(int employeeId, String firstName, String lastName, String password) {
        super(employeeId, firstName, lastName, password);
        this.accessLevel = "HIGH";
    }

    @Override
    public String getAccessLevel() {
        return accessLevel;
    }
}
