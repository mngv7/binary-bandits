package com.example.protrack.users;

import com.example.protrack.databaseutil.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

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
