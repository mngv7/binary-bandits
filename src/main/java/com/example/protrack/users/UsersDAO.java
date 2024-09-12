package com.example.protrack.users;

import com.example.protrack.databaseutil.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.HashMap;

public class UsersDAO {
    private final Connection connection;

    public UsersDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS users ("
                            + "employeeId INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "firstName VARCHAR NOT NULL, "
                            + "lastName VARCHAR NOT NULL, "
                            + "dob DATE NOT NULL, "
                            + "email VARCHAR NOT NULL, "
                            + "phoneNo VARCHAR NOT NULL, "
                            + "gender VARCHAR NOT NULL, "
                            + "password VARCHAR NOT NULL, "
                            + "accessLevel VARCHAR NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public HashMap<Integer, AbstractUser> getAllUsers() throws SQLException {

        HashMap<Integer, AbstractUser> allUsers = new HashMap<>();
        String query = "SELECT * FROM users";

        try {
            PreparedStatement getAllUsers = connection.prepareStatement(query);


            ResultSet rs = getAllUsers.executeQuery();

            while (rs.next()) {
                AbstractUser user = mapResultSetToUser(rs);
                allUsers.put(user.getEmployeeId(), user);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return allUsers;
    }

    public AbstractUser mapResultSetToUser(ResultSet resultSet) throws SQLException {
        String userType = resultSet.getString("accessLevel");

        switch (userType) {
            case "HIGH":
                return new ManagerialUser(
                    resultSet.getInt("employeeId"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getDate("dob"),
                    resultSet.getString("email"),
                    resultSet.getString("phoneNo"),
                    resultSet.getString("gender"),
                    resultSet.getString("password")
                );
            case "MEDIUM":
                return new WarehouseUser(
                    resultSet.getInt("employeeId"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getDate("dob"),
                    resultSet.getString("email"),
                    resultSet.getString("phoneNo"),
                    resultSet.getString("gender"),
                    resultSet.getString("password")
                );
            case "LOW":
                return new ProductionUser(
                    resultSet.getInt("employeeId"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getDate("dob"),
                    resultSet.getString("email"),
                    resultSet.getString("phoneNo"),
                    resultSet.getString("gender"),
                    resultSet.getString("password")
                );
            default:
                throw new SQLException("Invalid user type: " + userType);
        }
    }

    public HashMap<Integer, ManagerialUser> getManagerialUsers() throws SQLException {
        HashMap<Integer, ManagerialUser> managerialUsers = new HashMap<>();
        String query = "SELECT * FROM users WHERE accessLevel = 'HIGH'";

        PreparedStatement getManagerialUsers = connection.prepareStatement(query);

        ResultSet rs = getManagerialUsers.executeQuery();

        while (rs.next()) {
            AbstractUser managerialUser = mapResultSetToUser(rs);
            managerialUsers.put(managerialUser.getEmployeeId(), (ManagerialUser) managerialUser);
        }

        return managerialUsers;
    }

    public HashMap<Integer, WarehouseUser> getWarehouseUsers() throws SQLException {
        HashMap<Integer, WarehouseUser> warehouseUsers = new HashMap<>();
        String query = "SELECT * FROM users WHERE accessLevel = 'MEDIUM'";

        PreparedStatement getWarehouseUsers = connection.prepareStatement(query);

        ResultSet rs = getWarehouseUsers.executeQuery();

        while (rs.next()) {
            AbstractUser warehouseUser = mapResultSetToUser(rs);
            warehouseUsers.put(warehouseUser.getEmployeeId(), (WarehouseUser) warehouseUser);
        }

        return warehouseUsers;
    }

    public HashMap<Integer, ProductionUser> getProductionUsers() throws SQLException {
        HashMap<Integer, ProductionUser> productionUsers = new HashMap<>();
        String query = "SELECT * FROM users WHERE accessLevel = 'LOW'";

        PreparedStatement getProductionUsers = connection.prepareStatement(query);


        ResultSet rs = getProductionUsers.executeQuery();

        while (rs.next()) {
            AbstractUser productionUser = mapResultSetToUser(rs);
            productionUsers.put(productionUser.getEmployeeId(), (ProductionUser) productionUser);
        }

        return productionUsers;
    }

    public void dropTable() {
        String query = "DROP TABLE IF EXISTS users";  // SQL statement to drop the work_orders table

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);    // executes SQL deletion statement
            System.out.println("Table 'users' dropped successfully.");
        } catch (SQLException ex) {
            System.err.println("Error dropping table 'users': " + ex.getMessage());
        }
    }

    public String getPasswordByFirstName(String firstName) {
        try {
            PreparedStatement getAccount = connection.prepareStatement("SELECT password FROM users WHERE firstName = ?");
            getAccount.setString(1, firstName);

            ResultSet rs = getAccount.executeQuery();

            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }

    public String getAccessLevelByFirstName(String firstName) {
        try {
            PreparedStatement getAccount = connection.prepareStatement("SELECT accessLevel FROM users WHERE firstName = ?");
            getAccount.setString(1, firstName);

            ResultSet rs = getAccount.executeQuery();

            if (rs.next()) {
                return rs.getString("accessLevel");
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }

    public void newUser(AbstractUser user) {
        try {
            PreparedStatement insertAccount = connection.prepareStatement(
                    "INSERT INTO users (employeeId, firstName, lastName, dob, email, phoneNo, gender, password, accessLevel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            insertAccount.setInt(1, user.getEmployeeId());
            insertAccount.setString(2, user.getFirstName());
            insertAccount.setString(3, user.getLastName());
            insertAccount.setDate(4, user.getDob());
            insertAccount.setString(5, user.getEmail());
            insertAccount.setString(6, user.getPhoneNo());
            insertAccount.setString(7, user.getGender());
            insertAccount.setString(8, hashedPassword);
            insertAccount.setString(9, user.getAccessLevel());

            insertAccount.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM users");
            rs.next();
            int count = rs.getInt("rowcount");
            rs.close();
            return count == 0;
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return false;
    }
}
