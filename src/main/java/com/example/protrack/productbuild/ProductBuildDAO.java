package com.example.protrack.productbuild;

import com.example.protrack.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductBuildDAO {
    private final Connection connection;

    public ProductBuildDAO() {
        connection = DatabaseConnection.getInstance();
    }

    /**
     * Creates product build table
     */
    public void createTable() {
        try {
            // Create a statement object for sending SQL queries to the database
            Statement createTable = connection.createStatement();

            // Execute the SQL query to create a table named 'productBuild' if it does not already exist
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS productBuild ("
                            + "buildId INTEGER NOT NULL, "
                            + "productOrderId INTEGER NOT NULL, "
                            + "buildCompletion FLOAT NOT NULL, "
                            + "productId INTEGER NOT NULL, "
                            + "PRIMARY KEY (buildId)"
                            + ")"
            );
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
    }

    /**
     * Inserts inputted product build into product build table
     *
     * @param productBuild a productBuild
     */
    public void newProductBuild(ProductBuild productBuild) {
        try {
            PreparedStatement newPB = connection.prepareStatement(
                    "INSERT INTO productBuild (buildId, productOrderId, buildCompletion, productId) "
                            + "VALUES (?, ?, ?, ?)"
            );

            // sets test record value into statement
            newPB.setInt(1, productBuild.getBuildId());
            newPB.setInt(2, productBuild.getProductOrderId());
            newPB.setFloat(3, productBuild.getBuildCompletion());
            newPB.setInt(4, productBuild.getProductId());

            // executes statement and enters test record values into test record table
            newPB.execute();
        } catch (SQLException ex) {
            // print error if error occurs
            System.err.println(ex);
        }
    }

    /**
     * Deletes product build table
     */
    public void dropTable() {
        String query = "DROP TABLE IF EXISTS productBuild";  // SQL statement to drop the product build table

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);    // executes SQL deletion statement
            System.out.println("Table 'productBuild' dropped successfully.");
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println("Error dropping table 'productBuild': " + ex.getMessage());
        }
    }

    /**
     * Getter that gets all product builds of table
     *
     * @return all product builds in product build table
     */
    public List<ProductBuild> getAllProductBuilds() {
        // empty list of products
        List<ProductBuild> productBuilds = new ArrayList<>();

        // query being run, get all from products
        String query = "SELECT * FROM productBuild";

        // try running query
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // while there is a row, get those values and add it to the list
            while (rs.next()) {
                int buildId = rs.getInt("buildId");
                int productOrderId = rs.getInt("productOrderId");
                float buildCompletion = rs.getFloat("buildCompletion");
                int productId = rs.getInt("productId");


                ProductBuild productBuild = new ProductBuild(buildId, productOrderId, buildCompletion, productId);
                productBuilds.add(productBuild);
            }
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
        // return list of products in table
        return productBuilds;
    }

    public List<ProductBuild> getAllProductBuildsWithPBID(int currentProductBuildId) {
        // empty list of products
        List<ProductBuild> productBuilds = new ArrayList<>();

        // query being run, get all from products
        String query = "SELECT * FROM productBuild WHERE buildId = ?";

        // try running query
        try {
            PreparedStatement getProductBuild = connection.prepareStatement(query);
            getProductBuild.setInt(1, currentProductBuildId);
            ResultSet rs = getProductBuild.executeQuery();
            // while there is a row, get those values and add it to the list
            while (rs.next()) {
                int buildId = rs.getInt("buildId");
                int productOrderId = rs.getInt("productOrderId");
                float buildCompletion = rs.getFloat("buildCompletion");
                int productId = rs.getInt("productId");

                //System.out.println("GOT buildID " + buildId);

                ProductBuild productBuild = new ProductBuild(buildId, productOrderId, buildCompletion, productId);
                productBuilds.add(productBuild);
            }
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
        // return list of products in table
        return productBuilds;
    }

    public List<ProductBuild> getAllProductBuildsWithPOID(int currentProductOrderId) {
        // empty list of products
        List<ProductBuild> productBuilds = new ArrayList<>();

        // query being run, get all from products
        String query = "SELECT * FROM productBuild WHERE productOrderId = ?";

        //System.out.println("THIS IS CurrentPOID " + currentProductOrderId);

        // try running query
        try {
            PreparedStatement getProductBuild = connection.prepareStatement(query);
            getProductBuild.setInt(1, currentProductOrderId);
            ResultSet rs = getProductBuild.executeQuery();
            // while there is a row, get those values and add it to the list
            while (rs.next()) {
                int buildId = rs.getInt("buildId");
                int productOrderId = rs.getInt("productOrderId");
                float buildCompletion = rs.getFloat("buildCompletion");
                int productId = rs.getInt("productId");

                //System.out.println("GOT buildID " + buildId);

                ProductBuild productBuild = new ProductBuild(buildId, productOrderId, buildCompletion, productId);
                productBuilds.add(productBuild);
            }
        } catch (SQLException ex) {
            // Catch and print any SQL exceptions that may occur during table creation
            System.err.println(ex);
        }
        // return list of products in table
        return productBuilds;
    }

    public void updateBuildCompletion(int buildId, float percentage) {
        try {
            PreparedStatement updateBuild = connection.prepareStatement(
                    "UPDATE productBuild " +
                            "SET buildCompletion = ? " +
                            "WHERE buildId = ? "
            );
            updateBuild.setFloat(1, percentage);
            updateBuild.setInt(2, buildId);

            // executes statement and enters test record values into test record table
            updateBuild.execute();
        } catch (SQLException ex) {
            // print error if error occurs
            System.err.println(ex);
        }
    }

    /**
     * Checks if table is empty.
     *
     * @return true if empty, else returns false.
     */
    public boolean isTableEmpty() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM productBuild");
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
