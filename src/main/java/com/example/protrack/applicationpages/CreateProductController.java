package com.example.protrack.applicationpages;

import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CreateProductController {

    @FXML
    private TextField productNameField;

    @FXML
    private TextField productIdField;

    @FXML
    private Button createProductButton;

    @FXML
    protected void onCreateProductButton() {
        ProductDAO productDAO = new ProductDAO();

        try {
            int productId = Integer.parseInt(productIdField.getText());
            String productName = productNameField.getText();

            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);

            productDAO.newProduct(new Product(productId, productName, date));
        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID. Please enter a valid number.");
        }
    }
}
