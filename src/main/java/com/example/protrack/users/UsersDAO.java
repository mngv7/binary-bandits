package com.example.protrack.users;

import java.sql.*;
import com.example.protrack.databaseutil.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

public class UsersDAO {
    private Connection connection;

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
                            + "password VARCHAR NOT NULL, "
                            + "accessLevel VARCHAR NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
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

    public void newUser(AbstractUser user) {
        try {
            PreparedStatement insertAccount = connection.prepareStatement(
                    "INSERT INTO users (employeeId, firstName, lastName, password, accessLevel) VALUES (?, ?, ?, ?, ?)"
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
