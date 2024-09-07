package com.example.protrack.products;

import com.example.protrack.databaseutil.DatabaseConnection;
import java.sql.*;

public class PartsDAO {
    private Connection connection;

    public PartsDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS parts ("
                            + "partsId INTEGER PRIMARY KEY, "
                            + "name VARCHAR NOT NULL, "
                            + "description VARCHAR NOT NULL"
                            + "type VARCHAR NOT NULL"
                            + "supplierId INTEGER NOT NULL"
                            + "cost INTEGER NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void newParts(Parts parts) {
        try {
            PreparedStatement insertParts = connection.prepareStatement(
                    "INSERT INTO parts (partsId, name, description, type, supplierId, cost) VALUES (?, ?, ?, ?, ?, ?)"
            );

            insertParts.setInt(1, parts.getPartsId());
            insertParts.setString(2, parts.getName());
            insertParts.setString(3, parts.getDescription());
            insertParts.setString(4, parts.getDescription());
            insertParts.setInt(5, parts.getSupplierId());
            insertParts.setInt(6, parts.getCost());

            insertParts.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM parts");
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
