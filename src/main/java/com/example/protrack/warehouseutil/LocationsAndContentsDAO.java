package com.example.protrack.warehouseutil;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.parts.Parts;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationsAndContentsDAO {
    private Connection connection;

    public LocationsAndContentsDAO() { connection = DatabaseConnection.getInstance(); }
    public void createTables () {
        try {
            /* NOTE: locationCapacity has been made an integer as a percentage makes zero sense here.
             *       This deviates from the design by quite a bit.
             */
            Statement createTableForLocations = connection.createStatement();
            createTableForLocations.execute("CREATE TABLE IF NOT EXISTS locations (" +
                                                    "locationID INTEGER PRIMARY KEY, " +
                                                    "locationAlias VARCHAR NOT NULL, " +
                                                    "locationType VARCHAR NOT NULL, " +
                                                    "locationCapacity INTEGER NOT NULL" +
                                                    ")");
            Statement createTableForLocationContents = connection.createStatement();
            /* TODO: Try and fulfill PK1 and PK2 requirements. (Maybe a composite primary key?) */
            createTableForLocationContents.execute("CREATE TABLE IF NOT EXISTS locationContents (" +
                                                            "locationID INTEGER PRIMARY KEY, " +
                                                            "partID INTEGER NOT NULL, " +
                                                            "quantity INTEGER NOT NULL" +
                                                            ")");
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void newWarehouse (Warehouse warehouse) {
        try {
            PreparedStatement insertWarehouse = connection.prepareStatement(
                    "INSERT INTO locations (locationID, locationAlias, locationType, locationCapacity) VALUES (?, ?, ?, ?)"
            );

            insertWarehouse.setInt(1, warehouse.getWarehouseId());
            insertWarehouse.setString(2, warehouse.getWarehouseName());
            insertWarehouse.setString(3, "WAREHOUSE");
            insertWarehouse.setInt(4, warehouse.getMaxParts());

            insertWarehouse.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void newWorkstation (Workstation workstation) {
        try {
            PreparedStatement insertWorkstation = connection.prepareStatement(
                    "INSERT INTO locations (locationID, locationAlias, locationType, locationCapacity) VALUES (?, ?, ?, ?)"
            );

            insertWorkstation.setInt(1, workstation.getWorkstationId());
            insertWorkstation.setString(2, workstation.getWorkstationName());
            insertWorkstation.setString(3, "WORKSTATION");
            insertWorkstation.setInt(4, workstation.getWorkstationMaxParts());

            insertWorkstation.execute();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void insertPartsIdWithQuantityIntoLocation (Integer locationID, partIdWithQuantity newPart) {
        try {
            String query = "SELECT * FROM locationContents WHERE locationID = ? AND partID = ?";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            int i = 0;
            if (rs.wasNull()) {
                /*
                 * Insert new locationContents record.
                 * TODO: An exception handler in an exception handler... pray it doesn't explode.
                 */
                try {
                    String insertQuery = "INSERT INTO locationContents (locationID, partID, quantity) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = connection.prepareStatement(insertQuery);

                    insertStmt.setInt(1, locationID);
                    insertStmt.setInt(2, newPart.partsId);
                    insertStmt.setInt(3, newPart.quantity);

                    insertStmt.execute();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            } else {
                while (rs.next()) {
                    /* There should be only one result here but just in case... */
                    if (i > 0) {
                        System.out.println("Warning: Duplicate partID at location; contents may not update properly.");
                        break;
                    }

                    int quantity = rs.getInt(3);
                    quantity += newPart.quantity;
                    /*
                     * Update new locationContents record.
                     * TODO: An exception handler in an exception handler... pray it doesn't explode.
                     */
                    try {
                        String updateQuery = "UPDATE locationContents SET quantity = ? WHERE locationID = ? AND partID = ?";
                        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);

                        updateStmt.setInt(1, quantity);
                        updateStmt.setInt(2, locationID);
                        updateStmt.setInt(3, newPart.partsId);

                        updateStmt.execute();
                    } catch (SQLException e) {
                        System.out.println(e);
                    }

                    i += 1;
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void removePartsIdWithQuantityFromLocation (Integer locationID, partIdWithQuantity partToRemove) {
        try {
            String query = "SELECT * FROM locationContents WHERE locationID = ? AND partID = ?";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            int i = 0;
            if (rs.wasNull()) {
                System.out.println("Warning: Tried to remove a nonexistent parts location.");
            } else {
                while (rs.next()) {
                    /* There should be only one result here but just in case... */
                    if (i > 0) {
                        System.out.println("Warning: Duplicate partID at location; contents may not update properly.");
                        break;
                    }

                    int quantity = rs.getInt(3);
                    if (quantity <= partToRemove.quantity) {
                        /*
                         * Delete locationContents record as the partsID for that location is now empty.
                         * TODO: An exception handler in an exception handler... pray it doesn't explode.
                         */
                        try {
                            String deleteQuery = "DELETE FROM locationContents WHERE locationID = ? AND partID = ?";
                            PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);

                            deleteStmt.setInt(1, quantity);
                            deleteStmt.setInt(2, locationID);

                            deleteStmt.execute();
                        } catch (SQLException e) {
                            System.out.println(e);
                        }
                    } else {
                        quantity -= partToRemove.quantity;
                        /*
                         * Update new locationContents record.
                         * TODO: An exception handler in an exception handler... pray it doesn't explode.
                         */
                        try {
                            String updateQuery = "UPDATE locationContents SET quantity = ? WHERE locationID = ? AND partID = ?";
                            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);

                            updateStmt.setInt(1, quantity);
                            updateStmt.setInt(2, locationID);
                            updateStmt.setInt(3, partToRemove.partsId);

                            updateStmt.execute();
                        } catch (SQLException e) {
                            System.out.println(e);
                        }
                    }



                    i += 1;
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public List<Warehouse> getAllWarehouses () {
        List<Warehouse> warehouses = new ArrayList<>();

        String query = "SELECT * FROM locations WHERE locationType = \"WAREHOUSE\"";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int locationID = rs.getInt("locationID");
                String locationAlias = rs.getString("locationAlias");
                int capacity = rs.getInt("locationCapacity");

                Warehouse warehouseItem = new RealWarehouse();
                warehouses.add(warehouseItem);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return warehouses;
    }

    public List<Workstation> getAllWorkstations () {
        List<Workstation> workstations = new ArrayList<>();

        String query = "SELECT * FROM locations WHERE locationType = \"WORKSTATION\"";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int locationID = rs.getInt("locationID");
                String locationAlias = rs.getString("locationAlias");
                int capacity = rs.getInt("locationCapacity");

                Workstation workstationItem = new RealWorkstation(locationID, locationAlias, capacity);
                workstations.add(workstationItem);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        return workstations;
    }

    public void getAllPartsInWarehouse () {
        System.out.println("FIXME: getAllPartsInWarehouse not implemented.");
    }

    public void getAllPartsForWorkstation () {
        System.out.println("FIXME: getAllPartsForWorkstation not implemented.");
    }

    public boolean isLocationsTableEmpty () {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM locations");
            rs.next();
            int count = rs.getInt("rowcount");
            rs.close();
            return count == 0;
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return false;
    }

    public boolean isLocationContentsTableEmpty () {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM locationContents");
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
