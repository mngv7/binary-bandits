package com.example.protrack.users;

import com.example.protrack.utility.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO implements IUsersDAO {
    private final Connection connection;

    public UsersDAO() {
        connection = DatabaseConnection.getInstance();
    }

    @Override
    public void createTable() {
        try {
            // Create a statement object for sending SQL queries to the database
            Statement createTable = connection.createStatement();

            // Execute the SQL query to create a table named 'users' if it does not already exist
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS users ("
                            // Create 'employeeId' as an INTEGER and the primary key with auto-increment
                            + "employeeId INTEGER PRIMARY KEY AUTOINCREMENT, "
                            // Create 'firstName' as a VARCHAR (string) and ensure it's not null
                            + "firstName VARCHAR NOT NULL, "
                            // Create 'lastName' as a VARCHAR (string) and ensure it's not null
                            + "lastName VARCHAR NOT NULL, "
                            // Create 'dob' (date of birth) as a DATE and ensure it's not null
                            + "dob DATE NOT NULL, "
                            // Create 'email' as a VARCHAR (string) and ensure it's not null
                            + "email VARCHAR NOT NULL, "
                            // Create 'phoneNo' as a VARCHAR (string) and ensure it's not null
                            + "phoneNo VARCHAR NOT NULL, "
                            // Create 'gender' as a VARCHAR (string) and ensure it's not null
                            + "gender VARCHAR NOT NULL, "
                            // Create 'password' as a VARCHAR (string) and ensure it's not null
                            + "password VARCHAR NOT NULL, "
                            // Create 'accessLevel' as a VARCHAR (string) and ensure it's not null
                            + "accessLevel VARCHAR NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }


    @Override
    public List<AbstractUser> getAllUsers() {
        // Initialize an empty list to store AbstractUser objects
        List<AbstractUser> users = new ArrayList<>();

        // SQL query to retrieve all rows from the 'users' table
        String query = "SELECT * FROM users";

        // Try-with-resources block to automatically close Statement and ResultSet
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Iterate over the result set (each row from the 'users' table)
            while (rs.next()) {
                // Retrieve values from each column in the current row
                int employeeId = rs.getInt("employeeId");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                Date dob = rs.getDate("dob");
                String email = rs.getString("email");
                String phoneNo = rs.getString("phoneNo");
                String gender = rs.getString("gender");
                String password = rs.getString("password");
                String accessLevel = rs.getString("accessLevel");

                // Use a switch expression to create the appropriate user object based on the access level
                AbstractUser userItem = switch (accessLevel) {
                    case "HIGH" ->
                        // Create a ManagerialUser if access level is HIGH
                            new ManagerialUser(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
                    case "MEDIUM" ->
                        // Create a WarehouseUser if access level is MEDIUM
                            new WarehouseUser(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
                    case "LOW" ->
                        // Create a ProductionUser if access level is LOW
                            new ProductionUser(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
                    default ->
                        // Throw an exception if access level is unknown
                            throw new IllegalArgumentException("Unknown access level: " + accessLevel);
                };
                // Add the created user object to the users list
                users.add(userItem);
            }
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that occur during the query execution
            System.err.println(ex);
        }
        // Return the list of users
        return users;
    }


    @Override
    public AbstractUser mapResultSetToUser(ResultSet resultSet) {

        try {
            String userType = resultSet.getString("accessLevel");

            return switch (userType) {
                case "HIGH" -> new ManagerialUser(
                        resultSet.getInt("employeeId"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getDate("dob"),
                        resultSet.getString("email"),
                        resultSet.getString("phoneNo"),
                        resultSet.getString("gender"),
                        resultSet.getString("password")
                );
                case "MEDIUM" -> new WarehouseUser(
                        resultSet.getInt("employeeId"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getDate("dob"),
                        resultSet.getString("email"),
                        resultSet.getString("phoneNo"),
                        resultSet.getString("gender"),
                        resultSet.getString("password")
                );
                case "LOW" -> new ProductionUser(
                        resultSet.getInt("employeeId"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getDate("dob"),
                        resultSet.getString("email"),
                        resultSet.getString("phoneNo"),
                        resultSet.getString("gender"),
                        resultSet.getString("password")
                );
                default -> throw new SQLException("Invalid user type: " + userType);
            };
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteUserById(Integer employeeId) throws SQLException {
        String query = "DELETE FROM users WHERE employeeId = ?";

        try (PreparedStatement deleteUser = connection.prepareStatement(query)) {
            deleteUser.setInt(1, employeeId);
            deleteUser.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<ManagerialUser> getManagerialUsers() {
        List<ManagerialUser> managerialUsers = new ArrayList<>();

        // Query to select users with 'HIGH' access level
        String query = "SELECT * FROM users WHERE accessLevel = 'HIGH'";

        try {
            PreparedStatement getManagerialUsers = connection.prepareStatement(query);

            ResultSet rs = getManagerialUsers.executeQuery();

            while (rs.next()) {
                AbstractUser managerialUser = mapResultSetToUser(rs);
                managerialUsers.add((ManagerialUser) managerialUser);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return managerialUsers;
    }

    public List<WarehouseUser> getWarehouseUsers() {
        List<WarehouseUser> warehouseUsers = new ArrayList<>();
        // Query to select users with 'MEDIUM' access level
        String query = "SELECT * FROM users WHERE accessLevel = 'MEDIUM'";

        // Use try-with-resources to automatically close PreparedStatement and ResultSet
        try (PreparedStatement getWarehouseUsers = connection.prepareStatement(query);
             ResultSet rs = getWarehouseUsers.executeQuery()) {

            // Map each result to a WarehouseUser and add it to the list
            while (rs.next()) {
                AbstractUser warehouseUser = mapResultSetToUser(rs);
                if (warehouseUser instanceof WarehouseUser) {
                    warehouseUsers.add((WarehouseUser) warehouseUser); // Cast to WarehouseUser
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return warehouseUsers;
    }

    public List<ProductionUser> getProductionUsers() {
        List<ProductionUser> productionUsers = new ArrayList<>();
        // Query to select users with 'LOW' access level
        String query = "SELECT * FROM users WHERE accessLevel = 'LOW'";

        try {
            PreparedStatement getProductionUsers = connection.prepareStatement(query);
            ResultSet rs = getProductionUsers.executeQuery();

            while (rs.next()) {
                AbstractUser productionUser = mapResultSetToUser(rs);
                productionUsers.add((ProductionUser) productionUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productionUsers;
    }


    @Override
    public void dropTable() {
        // SQL statement to drop the 'users' table if it exists
        String query = "DROP TABLE IF EXISTS users";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);  // Execute the SQL statement to drop the table
            System.out.println("Table 'users' dropped successfully.");
        } catch (SQLException ex) {
            // Print an error message if there is an issue with dropping the table
            System.err.println("Error dropping table 'users': " + ex.getMessage());
        }
    }

    public AbstractUser getUserById(Integer employeeId) {
        String query = "SELECT * FROM users WHERE employeeId = ?";

        try (PreparedStatement getUser = connection.prepareStatement(query)) {
            getUser.setInt(1, employeeId);

            try (ResultSet rs = getUser.executeQuery()) {
                if (rs.next()) {
                    String accessLevel = rs.getString("accessLevel");
                    return mapResultSetToUser(rs, accessLevel);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getEmployeeIdByFullName(String fullName) {
        String[] splitFullName = fullName.trim().split("\\s+");

        if (splitFullName.length < 2) {
            throw new IllegalArgumentException("Full name must contain both first and last name.");
        }

        String firstName = splitFullName[0];
        String lastName = splitFullName[1];

        // Query to retrieve employee ID based on first and last names
        String query = "SELECT employeeId FROM users WHERE firstName = ? AND lastName = ?";

        try (PreparedStatement getEmployeeId = connection.prepareStatement(query)) {
            getEmployeeId.setString(1, firstName);
            getEmployeeId.setString(2, lastName);

            try (ResultSet rs = getEmployeeId.executeQuery()) {
                // Return the employee ID if found, otherwise return null
                if (rs.next()) {
                    return rs.getInt("employeeId");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            // Wrap and rethrow the SQLException as a RuntimeException
            throw new RuntimeException(e);
        }
    }


    private AbstractUser mapResultSetToUser(ResultSet rs, String accessLevel) {

        try {
            Integer employeeId = rs.getInt("employeeId");
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            Date dob = rs.getDate("dob");
            String email = rs.getString("email");
            String phoneNo = rs.getString("phoneNo");
            String gender = rs.getString("gender");
            String password = rs.getString("password");

            switch (accessLevel) {
                case "HIGH":
                    return new ManagerialUser(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
                case "MEDIUM":
                    return new WarehouseUser(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
                case "LOW":
                    return new ProductionUser(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
                default:
                    throw new IllegalArgumentException("Unknown access level: " + accessLevel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public void newUser(AbstractUser user) {
        try {
            // Prepare SQL statement to insert a new user into the 'users' table
            PreparedStatement insertAccount = connection.prepareStatement(
                    "INSERT INTO users (employeeId, firstName, lastName, dob, email, phoneNo, gender, password, accessLevel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            // Hash the user's password before storing it
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            // Set parameters for the SQL statement
            insertAccount.setInt(1, user.getEmployeeId());
            insertAccount.setString(2, user.getFirstName());
            insertAccount.setString(3, user.getLastName());
            insertAccount.setDate(4, user.getDob());
            insertAccount.setString(5, user.getEmail());
            insertAccount.setString(6, user.getPhoneNo());
            insertAccount.setString(7, user.getGender());
            insertAccount.setString(8, hashedPassword);
            insertAccount.setString(9, user.getAccessLevel());

            // Execute the SQL statement to insert the user
            insertAccount.execute();
        } catch (SQLException ex) {
            // Print SQL error if the insertion fails
            System.err.println(ex);
        }
    }

@Override
public Integer getMaxEmployeeId() throws SQLException {
    // Query to get the maximum employee ID from the 'users' table
    String query = "SELECT MAX(employeeID) AS maxId FROM users";

    try (PreparedStatement getMaxId = connection.prepareStatement(query);
         ResultSet rs = getMaxId.executeQuery()) {
        // Check if a result is returned and get the maximum employee ID
        if (rs.next()) {
            return rs.getInt("maxId");
        } else {
            // Return -1 if no results are found
            return -1;
        }
    } catch (SQLException e) {
        // Wrap and rethrow the SQLException as a RuntimeException
        throw new RuntimeException(e);
    }
}

    @Override
    public boolean isTableEmpty() {
        try {
            // Create a statement to execute the query
            Statement stmt = connection.createStatement();
            // Query to count the number of rows in the 'users' table
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM users");
            rs.next();
            int count = rs.getInt("rowcount");
            rs.close();
            // Return true if there are no rows, otherwise false
            return count == 0;
        } catch (SQLException ex) {
            // Print SQL error if the query fails
            System.err.println(ex);
        }
        return false;
    }
}
