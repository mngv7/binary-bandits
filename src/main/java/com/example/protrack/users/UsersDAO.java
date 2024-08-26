package com.example.protrack.users;

import java.sql.*;

import com.example.protrack.DatabaseConnection;

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
}
