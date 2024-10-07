package com.example.protrack.warehouseutil;

import com.example.protrack.database.ProductDBTable;
import com.example.protrack.database.WorkstationPartDBTable;
import com.example.protrack.utility.DatabaseConnection;
import com.example.protrack.parts.Parts;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationsAndContentsDAO {
    private Connection connection; /* This database connection instance. */

    public LocationsAndContentsDAO() { connection = DatabaseConnection.getInstance(); }

    /*
     * Unlike other DAOs in ProTrack, this one manipulates two database tables.
     * Incidentally we create them both here.
     */
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
                    "locationID INTEGER NOT NULL, " +
                    "partID INTEGER NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "PRIMARY KEY (locationID, partID)" +
                    ")");
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    /*
     * Inserts a new Warehouse into the locations table.
     * In ProTrack, we only have one global warehouse but this code was written with the old
     * Warehouse interface design in mind before the database ERD changed.
     * Incidentally, it is now possible to support multiple warehouses in future!
     */
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

    /*
     * Inserts a new Workstation into the locations table.
     * TODO: Maybe change method naming? With its current usage often following a call to
     *       operator new() on the implementation of Workstation in use, this could be renamed
     *       addWorkstation instead.
     *       The same can be said for Warehouse but typically in ProTrack we only use one.
     */
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

    public List<WorkstationPartDBTable> getAllWSParts(int locationId) {
        List<WorkstationPartDBTable> workstationPartDBTableList = new ArrayList<>();

        try {
            PreparedStatement getWSParts = connection.prepareStatement(
                    "SELECT * " +
                            "FROM locationContents a " +
                            "WHERE a.locationID = ?"
            );
            getWSParts.setInt(1, locationId);

            ResultSet rs = getWSParts.executeQuery();

            while (rs.next()) {
                int partId = rs.getInt("partID");

                //Get Part name using partID
                String partName = "";
                try {
                    PreparedStatement getPartName = connection.prepareStatement(
                            "SELECT * " +
                                    "FROM parts a " +
                                    "WHERE a.partsId = ?"
                    );
                    getPartName.setInt(1, partId);
                    ResultSet rs2 = getPartName.executeQuery();

                    while (rs2.next()) {
                        partName = rs2.getString("name");
                    }
                } catch (SQLException ex) {
                    System.err.println(ex);
                }

                int quantity = rs.getInt("quantity");

                if (partName.isEmpty()) {
                    System.out.println("Can't find name for some reason");
                } else {
                    WorkstationPartDBTable wsPartTable = new WorkstationPartDBTable(partId, partName, quantity);
                    workstationPartDBTableList.add(wsPartTable);
                }
            }

        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return workstationPartDBTableList;
    }

    public Integer getLocationIDFromAlias(String alias) {
        int locationId = -1;

        try {
            /*
            PreparedStatement getPrice = connection.prepareStatement(
                            "SELECT SUM (a.requiredAmount * b.cost) AS TotalValue " +
                                    "FROM requiredParts a " +
                                    "JOIN parts b ON a.PartsId = b.PartsId " +
                                    "WHERE a.productId = ?");
                    getPrice.setInt(1, productId);
                    ResultSet rs2 = getPrice.executeQuery();

                    if (rs2.next()) {
                        price += rs2.getDouble("TotalValue");
                    }
             */
            PreparedStatement getLocID = connection.prepareStatement(
                    "SELECT locationID " +
                            "FROM locations a " +
                            "WHERE a.locationAlias = ?");
            getLocID.setString(1, alias);
            ResultSet rs2 = getLocID.executeQuery();

            if (rs2.next()) {
                locationId = rs2.getInt("locationID");
                //System.out.println("This is locationID " + locationId);
                //return locationId;
            }

            //return locationId;

        } catch (SQLException e) {
            System.out.println(e);
        }

        return locationId;
    }

    /*
     * Inserts parts into a location by making links between the locationID and partID in the locationcontents table.
     * This is more or less generic but intended to be utilised by Warehouse and Workstation in specific ways.
     * If a locationID and partsID pair already exists, the quantity is added to the existing quantity instead.
     */
    public void insertPartsIdWithQuantityIntoLocation (Integer locationID, partIdWithQuantity newPart) {
        try {
            String query = "SELECT * FROM locationContents WHERE locationID = ? AND partID = ?";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int i = 0;
            if (!rs.next()) {
                /*
                 * Insert new locationContents record.
                 * TODO: An exception handler in an exception handler... pray it doesn't explode.
                 *  ... It exploded (Column 0 out of bounds [1,3]
                 *  Explosion fixed; turns out rs.wasNull() does NOT do what I thought it does.
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
                while (true) {
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
                    if (!rs.next()) break;
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    /*
     * Removes parts from a location by decreasing the quantity value for a given locationID/partID pair.
     * This is more or less generic but intended to be utilised by Warehouse and Workstation in specific ways.
     * If the quantity to be removed exceeds the remaining quantity in the database, the database entry is removed
     * entirely.
     */
    public void removePartsIdWithQuantityFromLocation (Integer locationID, partIdWithQuantity partToRemove) {
        try {
            String query = "SELECT * FROM locationContents WHERE locationID = " + locationID +" AND partID = " + partToRemove.partsId;

            //PreparedStatement removeWSPart = connection.prepareStatement(query);
            //removeWSPart.setInt(locationID, partToRemove.partsId);
            System.out.println("Location ID " + locationID);
            System.out.println("Part ID " + partToRemove.partsId);
            //TODO You forgot to set the ints for the above statement originally.
            // As a stopgap I've manually added them into the string.
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            //ResultSet rs = removeWSPart.executeQuery(query);

            int i = 0;
            if (!rs.next()) {
                System.out.println("Warning: Tried to remove a nonexistent parts location.");
            } else {
                while (true) {
                    /* There should be only one result here but just in case... */
                    if (i > 0) {
                        System.out.println("Warning: Duplicate partID at location; contents may not update properly.");
                        break;
                    }

                    int quantity = rs.getInt(3);
                    if (quantity < partToRemove.quantity) {
                        /*
                         * Delete locationContents record as the partsID for that location is now empty.
                         * TODO: An exception handler in an exception handler... pray it doesn't explode.
                         */
//                        try {
//                            String deleteQuery = "DELETE FROM locationContents WHERE locationID = ? AND partID = ?";
//                            PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
//
//                            deleteStmt.setInt(1, quantity);
//                            deleteStmt.setInt(2, locationID);
//
//                            deleteStmt.execute();
//                        } catch (SQLException e) {
//                            System.out.println(e);
//                        }
                        System.out.println("Tried to subtract more than what is available");
                    } else {
                        quantity -= partToRemove.quantity;

                        System.out.println("Quantity is " + quantity + " For " + partToRemove.partsId + " In " + locationID);
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
                    if (!rs.next()) break;
                }
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    /*
     * Retrieves all warehouses from the locations database and returns them as an ArrayList of type Warehouse.
     * This will likely only ever return 1 element due to the changes to the database ERD.
     */
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

    /*
     * Retrieve all workstations in the locations database and return them as an ArrayList of type Workstation.
     */
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

    /*
     * Retrieves all the parts in a warehouse.
     * Currently unimplemented.
     */
    public void getAllPartsInWarehouse () {
        System.out.println("FIXME: getAllPartsInWarehouse not implemented.");
    }

    /*
     * Retrieves all the parts allocated to a Workstation.
     * Currently unimplemented.
     */
    public void getAllPartsForWorkstation () {
        System.out.println("FIXME: getAllPartsForWorkstation not implemented.");
    }

    /* Self-explanatory. */
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

    /* Self-explanatory. */
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
