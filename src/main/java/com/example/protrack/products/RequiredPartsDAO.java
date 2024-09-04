package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.AbstractUser;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class RequiredPartsDAO {
    private Connection connection;

    public RequiredPartsDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS requiredParts ("
                            + "reqPartsId INTEGER PRIMARY KEY, "
                            + "partsId VARCHAR NOT NULL, "
                            + "requiredAmt INTEGER NOT NULL, "
                            + "currentAmt INTEGER NOT NULL "
                            + ")"
            );


        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void newRequiredParts(RequiredParts requiredParts) {
        try {
            PreparedStatement insertAccount = connection.prepareStatement(
                    "INSERT INTO requiredParts (reqPartsId, partsId, requiredAmt, currentAmt) VALUES (?, ?, ?, ?)"
            );

            insertAccount.setInt(1, requiredParts.getReqPartsId());
            insertAccount.setString(2, requiredParts.getPartsId());
            insertAccount.setInt(3, requiredParts.getRequiredAmt());
            insertAccount.setInt(4, requiredParts.getCurrentAmt());

            insertAccount.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }


}
