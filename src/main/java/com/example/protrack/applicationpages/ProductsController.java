package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.observers.Observer;
import com.example.protrack.observers.ProductsTableSubject;
import com.example.protrack.products.Product;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.utility.LoggedInUserSingleton;
import javafx.application.Platform;
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
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;


public class ProductsController implements Observer {

    /**
     * Generates list of products from product database with price
     *
     * @return list of products with price
     */

    /*
    public List<Product> productDBtoTable() {
        //Connection connection;
        //connection = DatabaseConnection.getInstance();

        List<Product> products = new ArrayList<>();
        ProductDAO productDAO = new ProductDAO();

        products = productDAO.getAllPrice();

        // return list
        return products;
    }
    */

    private static final String TITLE = "Create Product";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 360;
    @FXML
    public Button addProductButton;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> colProductId;
    @FXML
    private TableColumn<Product, String> colProductName;
    @FXML
    private TableColumn<Product, java.sql.Date> colDateCreated;
    @FXML
    private TableColumn<Product, Double> colPrice;

    private ObservableList<Product> products;

    private ProductsTableSubject subject;

    /**
     * Initialise product page with values
     */
    public void initialize() {
        subject = new ProductsTableSubject();
        subject.registerObserver(this);

        Integer loggedInId = LoggedInUserSingleton.getInstance().getEmployeeId();
        UsersDAO usersDAO = new UsersDAO();

        // disable add product button if access level is not high
        addProductButton.setDisable(!usersDAO.getUserById(loggedInId).getAccessLevel().equals("HIGH"));

        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colDateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        products = FXCollections.observableArrayList();
        productTable.setItems(products);

        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // refresh product table
        subject.syncDataFromDB();
        subject.notifyObservers();

        Platform.runLater(() -> {
            Window window = addProductButton.getScene().getWindow();
            if (window instanceof Stage stage) {
                stage.setOnCloseRequest(event -> {
                    subject.deregisterObserver(this);
                });
            }
        });
    }

    /**
     * Refreshes the table with parts data
     */
    public void update() {
        products.clear();
        products.setAll(subject.getData());
    }

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
            subject.syncDataFromDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}