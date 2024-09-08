package com.example.protrack.applicationpages;

import com.example.protrack.database.ProductDBTable;
import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import com.example.protrack.products.RequiredParts;
import com.example.protrack.products.RequiredPartsDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CreateProductController {
    private static final String TITLE = "Create Product";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 360;

    /*
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
    */

    @FXML
    private TextField partIdSearchField;

    @FXML
    private VBox partResultVBox;


    @FXML
    public Button createProductButton;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField productIdField;

    public void initialize() {
        // Create a binding to check if any field is empty
        /*
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
        */

        //createProductButton.disableProperty().bind(fieldsEmpty);
    }

    @FXML
    protected void partSearch() {
        Connection connection;
        connection = DatabaseConnection.getInstance();

        String partIdStr = partIdSearchField.getText();

        if (partIdStr != null && !partIdStr.trim().isEmpty()) {
            // Clear previous results
            // partResultVBox.getChildren().clear();

            int partIdInt = Integer.parseInt(partIdStr);
            try {
                PreparedStatement getPartId = connection.prepareStatement(
                        "SELECT * " +
                                "FROM parts a " +
                                "WHERE a.partsId = ?");
                getPartId.setInt(1, partIdInt);
                ResultSet rs = getPartId.executeQuery();

                if (rs.next()) {
                    //Make the new vbox with those partid
                    // Create a new row with the productID and a TextField
                    VBox newRow = new VBox();
                    Label idLabel = new Label("Part ID: " + rs.getString("partsId"));
                    Label idLabel2 = new Label("Part Name: " + rs.getString("name"));
                    TextField textField = new TextField();
                    newRow.getChildren().addAll(idLabel, idLabel2, textField);

                    // Add the new row to the result VBox
                    partResultVBox.getChildren().add(newRow);
                }

            } catch(SQLException ex) {
                System.err.println(ex);

            }
        }
    }

    /*TODO
    When clicking on the create product button, it sets the time as current time.
    Then it Adds the current Product to the ProductDB
    Then it reads all the parts and assigns it to RequiredPartsDB
    Then the test record pop up occurs

    1. Make it so that you can search for Parts.
    2. Generate new row for said part, where you can add number of parts
    3. Ensure that when create product is pressed, it generates a new input in product table
    4. "" For requiredParts table using the parts generated in (2)
    5. Open test records table.
     */

    /*
    When clicking on the create product button, it sets the time as current time.
    Then it Adds the current Product to the ProductDB
    Then it reads all the parts and assigns it to RequiredPartsDB
    Then the test record pop up occurs

    Requires:
    ProductDB
    RequiredPartsDB
    TestRecordsDB
     */
    @FXML
    protected void onCreateProductButton() {

        ProductDAO productDAO = new ProductDAO();
        RequiredPartsDAO requiredPartsDAO = new RequiredPartsDAO();

        int rowCount = partResultVBox.getChildren().size();
        System.out.println("Number of rows: " + rowCount);

        try {
            int productId = Integer.parseInt(productIdField.getText());

            String productName = productNameField.getText();

            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);

            productDAO.newProduct(new Product(productId, productName, date));

            insertProductsIntoDB(productId, productName, date);

            /*
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
            openCreateTestRecordPopup();
            */

            insertReqPartsFromVbox(productId);

        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID. Please enter a valid number.");
        }
    }

    private void insertProductsIntoDB(int productId, String productName, java.sql.Date date) {
        Connection connection;
        connection = DatabaseConnection.getInstance();
        try {
            PreparedStatement insertProduct = connection.prepareStatement(
                    "INSERT INTO products (productId, productName, dateCreated ) " +
                            "VALUES (?, ?, ?)");
            insertProduct.setInt(1, productId);
            insertProduct.setString(2, productName);
            insertProduct.setDate(3, date);

            System.out.println("productId" + productId + " productName " + productName + " date " + date);

        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    private void insertReqPartsFromVbox(int productId) {
        Connection connection;
        connection = DatabaseConnection.getInstance();

        try {
            connection.setAutoCommit(false); // Use transaction to handle multiple inserts
            for (var node : partResultVBox.getChildren()) {
                if (node instanceof VBox row) {
                    // Get the labels and text field from the VBox
                    Label idLabel = (Label) row.getChildren().get(0);
                    String partsId = idLabel.getText().replace("Part ID: ", "");

                    TextField amountField = (TextField) row.getChildren().get(2); // Assuming the third element is the TextField for required amount
                    String requiredAmount = amountField.getText();

                    // Logging for debugging purposes
                    System.out.println("---------------");
                    System.out.println("PartsID: " + partsId);
                    System.out.println("RequiredAmount: " + requiredAmount);
                    System.out.println("---------------");

                    RequiredPartsDAO requiredPartsDAO = new RequiredPartsDAO();
                    requiredPartsDAO.newRequiredParts(new RequiredParts(Integer.parseInt(partsId), productId, Integer.parseInt(requiredAmount)));
                }
            }
            connection.commit();

        } catch (SQLException ex) {
            System.err.println(ex);
            try {
                connection.rollback(); // Rollback in case of error
            } catch (SQLException rollbackEx) {
                System.err.println(rollbackEx);
            }
        }
    }


    public void openCreateTestRecordPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/create-test-record-view.fxml"));
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
