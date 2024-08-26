package com.example.protrack.users;

public abstract class AbstractUser {

    enum accessLevel {
        LOW,
        MEDIUM,
        HIGH
    }

    protected String username;

    public abstract void getAccessLevel();

    public String getUsername()
    {
        return username;
    }
}