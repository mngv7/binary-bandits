package com.example.protrack.applicationpages;

import com.example.protrack.database.ProductDBTable;
import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsController {

    @FXML
    private TableView<ProductDBTable> productTable;

    @FXML
    private TableColumn<ProductDBTable, Integer> colProductId;

    @FXML
    private TableColumn<ProductDBTable, String> colProductName;

    @FXML
    private TableColumn<ProductDBTable, java.sql.Date> colDateCreated;

    @FXML
    private TableColumn<ProductDBTable, Double> colPrice;

    private ObservableList<ProductDBTable> productList;

    public void initialize() {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colDateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        productList = FXCollections.observableArrayList();
        productTable.setItems(productList);
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();
    }

    public void refreshTable() {
        //ProductDAO productDAO = new ProductDAO();
        productList.clear();
        productList.addAll(productDBtoTable());
    }

    public List<ProductDBTable> productDBtoTable() {
        Connection connection;
        connection = DatabaseConnection.getInstance();

        List<ProductDBTable> products = new ArrayList<>();

        String query = "SELECT * FROM products";

        //pstmt = connection.prepareStatement("SELECT * FROM bankAccounts WHERE bankBalance > ?");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int productId = rs.getInt("productId");
                String productName = rs.getString("productName");
                Date dateCreated = rs.getDate("dateCreated");
                double price = 0.0;

                /*String getCostQuery = "SELECT SUM(a.requiredAmount * b.cost) AS TotalValue ";
                getCostQuery += "FROM requiredParts a ";
                getCostQuery += "JOIN parts b ON a.PartsId = b.PartsId ";
                getCostQuery += "WHERE a.productId = ";
                */

                try {
                    PreparedStatement getPrice = connection.prepareStatement(
                            "SELECT SUM (a.requiredAmount * b.cost) AS TotalValue " +
                                    "FROM requiredParts a " +
                                    "JOIN parts b ON a.PartsId = b.PartsId " +
                                    "WHERE a.productId = ?");
                    getPrice.setInt(1, productId);
                    ResultSet rs2 = getPrice.executeQuery();

                    if (rs2.next()) {
                        price += rs2.getDouble("TotalValue");
                        System.out.println("Price " + price);
                    }

                } catch (SQLException ex) {
                    System.err.println(ex);
                }

                //Product product = new Product(productId, productName, dateCreated);
                ProductDBTable product = new ProductDBTable(productId, productName, dateCreated, price);

                products.add(product);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return products;
    }

    private static final String TITLE = "Create Product";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 360;

    public void openCreateProductPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/create-product-view.fxml"));
            Parent createProductRoot = fxmlLoader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(TITLE);

            Scene scene = new Scene(createProductRoot, WIDTH, HEIGHT);
            popupStage.setScene(scene);

            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}