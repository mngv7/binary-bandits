package com.example.protrack.applicationpages;

import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import com.example.protrack.products.RequiredParts;
import com.example.protrack.products.RequiredPartsDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateProductController implements Initializable {

    @FXML
    public TextField partId1;

    @FXML
    public TextField requireAmount1;

    @FXML
    public TextField partId2;

    @FXML
    public TextField requireAmount2;

    @FXML
    public TextField partId3;

    @FXML
    public TextField requireAmount3;

    @FXML
    public Button createProductButton;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField productIdField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Create a binding to check if any field is empty
        BooleanBinding fieldsEmpty = Bindings.createBooleanBinding(() ->
                        productIdField.getText().trim().isEmpty() ||
                                productNameField.getText().trim().isEmpty() ||
                                partId1.getText().trim().isEmpty() ||
                                requireAmount1.getText().trim().isEmpty() ||
                                partId2.getText().trim().isEmpty() ||
                                requireAmount2.getText().trim().isEmpty() ||
                                partId3.getText().trim().isEmpty() ||
                                requireAmount3.getText().trim().isEmpty(),
                productIdField.textProperty(),
                productNameField.textProperty(),
                partId1.textProperty(),
                requireAmount1.textProperty(),
                partId2.textProperty(),
                requireAmount2.textProperty(),
                partId3.textProperty(),
                requireAmount3.textProperty()
        );
        createProductButton.disableProperty().bind(fieldsEmpty);
    }

    @FXML
    protected void onCreateProductButton() {

        ProductDAO productDAO = new ProductDAO();
        RequiredPartsDAO requiredPartsDAO = new RequiredPartsDAO();

        try {
            int productId = Integer.parseInt(productIdField.getText());
            String productName = productNameField.getText();

            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);

            productDAO.newProduct(new Product(productId, productName, date));

            requiredPartsDAO.newRequiredParts(new RequiredParts(Integer.parseInt(partId1.getText()), productId, Integer.parseInt(requireAmount1.getText())));
            requiredPartsDAO.newRequiredParts(new RequiredParts(Integer.parseInt(partId2.getText()), productId, Integer.parseInt(requireAmount2.getText())));
            requiredPartsDAO.newRequiredParts(new RequiredParts(Integer.parseInt(partId3.getText()), productId, Integer.parseInt(requireAmount3.getText())));
            partId1.setText("");
            partId2.setText("");
            partId3.setText("");
            requireAmount1.setText("");
            requireAmount2.setText("");
            requireAmount3.setText("");
            productIdField.setText("");
            productNameField.setText("");
        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID. Please enter a valid number.");
        }
    }
}
