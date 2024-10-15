package com.example.protrack.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for Data Access Object (DAO) that defines methods for
 * interacting with the users database table.
 */
public interface IUsersDAO {

    /**
     * Creates the users table in the database if it doesn't exist.
     * This method is responsible for defining the table structure and
     * executing the SQL command to create the table.
     */
    void createTable();

    /**
     * Retrieves a list of all users from the database.
     *
     * @return A list of {@link AbstractUser} objects representing all users in the system.
     */
    List<AbstractUser> getAllUsers();

    /**
     * Maps a SQL {@link ResultSet} to an {@link AbstractUser} object.
     * This method is responsible for transforming a database row into a
     * user object.
     *
     * @param resultSet The {@link ResultSet} containing user data.
     * @return An {@link AbstractUser} object populated with the data from the {@link ResultSet}.
     * @throws SQLException If a database access error occurs.
     */
    AbstractUser mapResultSetToUser(ResultSet resultSet) throws SQLException;

    /**
     * Deletes a user from the database based on their employee ID.
     *
     * @param employeeId The ID of the employee to be deleted.
     * @throws SQLException If a database access error occurs or the operation fails.
     */
    void deleteUserById(Integer employeeId) throws SQLException;

    /**
     * Retrieves a list of users who are categorized as managerial users.
     *
     * @return A list of {@link ManagerialUser} objects.
     * @throws SQLException If a database access error occurs.
     */
    List<ManagerialUser> getManagerialUsers() throws SQLException;

    /**
     * Retrieves a list of users who are categorized as warehouse users.
     *
     * @return A list of {@link WarehouseUser} objects.
     * @throws SQLException If a database access error occurs.
     */
    List<WarehouseUser> getWarehouseUsers() throws SQLException;

    /**
     * Retrieves a list of users who are categorized as production users.
     *
     * @return A list of {@link ProductionUser} objects.
     * @throws SQLException If a database access error occurs.
     */
    List<ProductionUser> getProductionUsers() throws SQLException;

    /**
     * Drops the users table from the database if it exists.
     * This method is responsible for removing the entire users table and
     * all associated data.
     */
    void dropTable();

    /**
     * Retrieves a user from the database based on their employee ID.
     *
     * @param employeeId The ID of the employee to retrieve.
     * @return An {@link AbstractUser} object representing the user with the specified ID.
     */
    AbstractUser getUserById(Integer employeeId);

    /**
     * Retrieves the employee ID of a user based on their full name.
     *
     * @param fullName The full name of the user.
     * @return The employee ID corresponding to the user's full name.
     * @throws SQLException If a database access error occurs or the name does not exist.
     */
    Integer getEmployeeIdByFullName(String fullName) throws SQLException;

    /**
     * Adds a new user to the database.
     *
     * @param user An {@link AbstractUser} object representing the new user to be added.
     */
    void newUser(AbstractUser user);

    /**
     * Checks if the users table in the database is empty.
     *
     * @return {@code true} if the table is empty, {@code false} otherwise.
     */
    boolean isTableEmpty();

    /**
     * Retrieves the maximum employee ID from the users table.
     *
     * @return The highest employee ID in the users table, or {@code null} if the table is empty.
     * @throws SQLException If a database access error occurs.
     */
    Integer getMaxEmployeeId() throws SQLException;
}
