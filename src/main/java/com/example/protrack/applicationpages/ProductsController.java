package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.database.ProductDBTable;
import com.example.protrack.products.ProductDAO;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.utility.DatabaseConnection;
import com.example.protrack.utility.LoggedInUserSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}

public class ProductsController implements Subject {

    @FXML
    public Button addProductButton;

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

    private List <Observer> observers;

    /**
     * Initialise product page with values
     */
    public void initialize() {
        Integer loggedInId = LoggedInUserSingleton.getInstance().getEmployeeId();
        UsersDAO usersDAO = new UsersDAO();

        // disable add product button if access level is not high
        addProductButton.setDisable(!usersDAO.getUserById(loggedInId).getAccessLevel().equals("HIGH"));

        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colDateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        productList = FXCollections.observableArrayList();
        productTable.setItems(productList);
        notifyObservers();


        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // refresh product table
        refreshTable();
    }

    /**
     * Refreshes the table
     */
    public void refreshTable() {

        productList.clear();
        productList.addAll(productDBtoTable());
        notifyObservers();
    }

    /**
     * Generates list of products from product database with price
     * @return list of products with price
     */
    public List<ProductDBTable> productDBtoTable() {
        //Connection connection;
        //connection = DatabaseConnection.getInstance();

        List<ProductDBTable> products = new ArrayList<>();
        ProductDAO productDAO = new ProductDAO();

        products = productDAO.getAllCost();

        // return list
        return products;
    }

    private static final String TITLE = "Create Product";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 360;

    /**
     * Create pop-up when "Create Product" is pressed
     */
    public void openCreateProductPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/create-product-view.fxml"));
            Parent addPartsRoot = fxmlLoader.load();

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(TITLE);

            Scene scene = new Scene(addPartsRoot, WIDTH, HEIGHT);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);
            popupStage.setY(150);
            popupStage.setX(390);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ProductsController() {
        observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(productList);
        }
    }
}