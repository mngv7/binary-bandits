package com.example.protrack.parts;

import com.example.protrack.databaseutil.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                            + "description VARCHAR NOT NULL, "
                            + "type VARCHAR NOT NULL, "
                            + "supplierId INTEGER NOT NULL, "
                            + "cost DOUBLE NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void newPart(Parts parts) {
        try {
            PreparedStatement insertPart = connection.prepareStatement(
                    "INSERT INTO parts (partsId, name, description, type, supplierId, cost) VALUES (?, ?, ?, ?, ?, ?)"
            );

            insertPart.setInt(1, parts.getPartsId());
            insertPart.setString(2, parts.getName());
            insertPart.setString(3, parts.getDescription());
            insertPart.setString(4, parts.getType());
            insertPart.setInt(5, parts.getSupplierId());
            insertPart.setDouble(6, parts.getCost());

            insertPart.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public List<Parts> getAllParts() {
        List<Parts> parts = new ArrayList<>();

        String query = "SELECT * FROM parts";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int partsId = rs.getInt("partsId");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String type = rs.getString("type");
                int supplierId = rs.getInt("supplierId");
                Double cost = rs.getDouble("cost");

                //Date dateCreated = rs.getDate("dateCreated");

                Parts partsItem = new Parts(partsId, name, description, type, supplierId, cost);
                parts.add(partsItem);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return parts;
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
