package com.example.protrack.users;

import com.example.protrack.databaseutil.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
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
                            + "employee_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "first_name VARCHAR NOT NULL, "
                            + "last_name VARCHAR NOT NULL, "
                            + "password VARCHAR NOT NULL, "
                            + "access_level VARCHAR NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void dropTable() {
        String query = "DROP TABLE IF EXISTS users";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            System.out.println("Table 'users' dropped successfully.");
        } catch (SQLException ex) {
            System.err.println("Error dropping table 'users': " + ex.getMessage());
        }
    }

    public AbstractUser getUser(Integer employeeId) {
        AbstractUser user = null;
        String query = "SELECT * FROM users WHERE employee_id = ?";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            user = mapResultSetToUser(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public HashMap<Integer, ProductionUser> getAllUsers() {
        HashMap<Integer, ProductionUser> productionUsers = new HashMap<>();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productionUsers.put(mapResultSetToUser(rs).getEmployeeId(), mapResultSetToUser(rs)); // You'll need to implement this method
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productionUsers;
    }

    // Helper method to map a ResultSet row to an AbstractUser object
    private ProductionUser mapResultSetToUser(ResultSet rs) throws SQLException {
        int employeeId = rs.getInt("employee_id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String password = rs.getString("password");

        return new ProductionUser(employeeId, firstName, lastName, password);
    }

    public String getPasswordByFirstName(String firstName) {
        try {
            PreparedStatement getAccount = connection.prepareStatement("SELECT password FROM users WHERE first_name = ?");
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
                    "INSERT INTO users (employee_id, first_name, last_name, password, access_level) VALUES (?, ?, ?, ?, ?)"
            );
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            insertAccount.setInt(1, user.getEmployeeId());
            insertAccount.setString(2, user.getFirstName());
            insertAccount.setString(3, user.getLastName());
            insertAccount.setString(4, hashedPassword);
            insertAccount.setString(5, user.getAccessLevel());

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
