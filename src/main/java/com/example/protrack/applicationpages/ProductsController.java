package com.example.protrack.applicationpages;

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

public class ProductsController {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> colProductId;

    @FXML
    private TableColumn<Product, String> colProductName;

    @FXML
    private TableColumn<Product, java.sql.Date> colDateCreated;

    private ObservableList<Product> productList;

    public void initialize() {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colDateCreated.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));

        productList = FXCollections.observableArrayList();
        productTable.setItems(productList);
        refreshTable();
    }

    public void refreshTable() {
        ProductDAO productDAO = new ProductDAO();
        productList.clear();  // Clear the current list
        productList.addAll(productDAO.getAllProducts());  // Add the retrieved products to the list
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