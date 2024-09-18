package com.example.protrack.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IUsersDAO {
    // Method to create the users table if it doesn't exist
    void createTable();

    // Method to retrieve a list of all users
    List<AbstractUser> getAllUsers();

    // Method to map a ResultSet to an AbstractUser instance
    AbstractUser mapResultSetToUser(ResultSet resultSet) throws SQLException;

    // Method to retrieve a list of ManagerialUser instances
    List<ManagerialUser> getManagerialUsers() throws SQLException;

    // Method to retrieve a list of WarehouseUser instances
    List<WarehouseUser> getWarehouseUsers() throws SQLException;

    // Method to retrieve a list of ProductionUser instances
    List<ProductionUser> getProductionUsers() throws SQLException;

    // Method to drop the users table if it exists
    void dropTable();

    // Method to retrieve a user by their employee ID
    AbstractUser getUserById(Integer employeeId);

    // Method to retrieve an employee ID based on the user's full name
    Integer getEmployeeIdByFullName(String fullName) throws SQLException;

    // Method to add a new user to the database
    void newUser(AbstractUser user);

    // Method to check if the users table is empty
    boolean isTableEmpty();

    // Method to get the maximum employee ID from the users table
    Integer getMaxEmployeeId() throws SQLException;
}