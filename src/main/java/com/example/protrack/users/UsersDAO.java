package com.example.protrack.users;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.parts.Parts;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TooManyListenersException;

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

    public List<AbstractUser> getAllUsers() {
        List<AbstractUser> users = new ArrayList<>();

        String query = "SELECT * FROM users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int employeeId = rs.getInt("employeeId");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                Date dob = rs.getDate("dob");
                String email = rs.getString("email");
                String phoneNo = rs.getString("phoneNo");
                String gender = rs.getString("gender");
                String password = rs.getString("password");
                String accessLevel = rs.getString("accessLevel");

                AbstractUser userItem = switch (accessLevel) {
                    case "HIGH" ->
                            new ManagerialUser(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
                    case "MEDIUM" ->
                            new WarehouseUser(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
                    case "LOW" ->
                            new ProductionUser(employeeId, firstName, lastName, dob, email, phoneNo, gender, password);
                    default -> throw new IllegalArgumentException("Unknown access level: " + accessLevel);
                };
                users.add(userItem);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return users;
    }


    public AbstractUser mapResultSetToUser(ResultSet resultSet) throws SQLException {
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
    }

    public List<ManagerialUser> getManagerialUsers() throws SQLException {
        List<ManagerialUser> managerialUsers = new ArrayList<>();
        String query = "SELECT * FROM users WHERE accessLevel = 'HIGH'";

        PreparedStatement getManagerialUsers = connection.prepareStatement(query);

        ResultSet rs = getManagerialUsers.executeQuery();

        while (rs.next()) {
            AbstractUser managerialUser = mapResultSetToUser(rs);
            managerialUsers.add((ManagerialUser) managerialUser);
        }

        return managerialUsers;
    }

    public List<WarehouseUser> getWarehouseUsers() throws SQLException {
        List<WarehouseUser> warehouseUsers = new ArrayList<>();
        String query = "SELECT * FROM users WHERE accessLevel = 'MEDIUM'";

        PreparedStatement getWarehouseUsers = connection.prepareStatement(query);

        ResultSet rs = getWarehouseUsers.executeQuery();

        while (rs.next()) {
            AbstractUser warehouseUser = mapResultSetToUser(rs);
            warehouseUsers.add((WarehouseUser) warehouseUser);
        }

        return warehouseUsers;
    }

    public List<ProductionUser> getProductionUsers() throws SQLException {
        List<ProductionUser> productionUsers = new ArrayList<>();
        String query = "SELECT * FROM users WHERE accessLevel = 'LOW'";

        PreparedStatement getProductionUsers = connection.prepareStatement(query);
        ResultSet rs = getProductionUsers.executeQuery();

        while (rs.next()) {
            AbstractUser productionUser = mapResultSetToUser(rs);
            productionUsers.add((ProductionUser) productionUser);
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
