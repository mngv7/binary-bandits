package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.products.BillOfMaterials;
import com.example.protrack.products.BillOfMaterialsDAO;
import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import com.example.protrack.utility.DatabaseConnection;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class CreateProductController {
    private static final String TITLE = "Create Product";
    private static final int WIDTH =590;
    private static final int HEIGHT = 360;

    @FXML
    public Button closePopupButton;

    @FXML
    public Button partSearchButton;
    @FXML
    public Button createProductButton;
    @FXML
    private TextField partIdSearchField;
    @FXML
    private VBox partResultVBox;
    @FXML
    private VBox removeAllPartsButtonContainer;
    @FXML
    private TextField productNameField;

    @FXML
    private TextField productIdField;

    /*
    TODO Ensure that checks to ensure that fields are not empty are added here.
     */
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

        partResultVBox.getChildren().addListener((ListChangeListener<? super Node>) c -> {
            updateButtonVisibility();
        });

        //createProductButton.disableProperty().bind(fieldsEmpty);
    }

    /**
     * Searches for part with partId from text field
     * If found, generate a new row with part's id and name
     */
    @FXML
    protected void partSearch() {
        Connection connection;
        connection = DatabaseConnection.getInstance();

        // get text value of text field "partIdSearchField"
        String partIdStr = partIdSearchField.getText();

        // if partIdStr is not null and not empty run
        if (partIdStr != null && !partIdStr.trim().isEmpty()) {

            // convert string into integer (PartId is always an integer)
            int partIdInt = parseInt(partIdStr);
            try {

                // Create statement
                // Get all from parts where PartsId = PartsId
                PreparedStatement getPartId = connection.prepareStatement(
                        "SELECT * " +
                                "FROM parts a " +
                                "WHERE a.partsId = ?");
                // Set PartsId with current value
                getPartId.setInt(1, partIdInt);

                //execute query and get a list of results
                ResultSet rs = getPartId.executeQuery();

                // While there is a next row of results
                // Generate a new row with partId and a partName
                if (rs.next()) {
                    //Make the new vbox with those partid
                    // Create a new row with the partId and a partName
                    VBox newRow = new VBox();

                    Label idLabel = new Label("Part ID: " + rs.getString("partsId"));
                    idLabel.setStyle("-fx-font-weight: bold;");

                    Label idLabel2 = new Label("Part Name: " + rs.getString("name"));

                    TextField textField = new TextField();

                    textField.setPromptText("Enter Quantity");
                    newRow.getChildren().addAll(idLabel, idLabel2, textField);

                    // Add the new row to the result VBox
                    partResultVBox.getChildren().add(newRow);
                    partIdSearchField.clear();
                }
            } catch (SQLException ex) {
                // Catch and print any SQL exceptions that may occur during table creation
                System.err.println(ex);
            }
        }
    }

    /*TODO
    When clicking on the create product button, it sets the time as current time.
    Then it Adds the current Product to the ProductDB
    Then it reads all the parts and assigns it to RequiredPartsDB
    Then the test record pop up occurs

    1. ~Make it so that you can search for Parts.
    2. ~Generate new row for said part, where you can add number of parts
    3. ~Ensure that when create product is pressed, it generates a new input in product table
    4. ~"" For requiredParts table using the parts generated in (2)
    5. ~Open test records table.
        All done~
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

    /**
     * Upon clicking "Create Product", create the product in the product table and generate
     * the product's BoM. Also proceed towards creating the product's test records through
     * a popup.
     */
    @FXML
    protected void onCreateProductButton() {

        ProductDAO productDAO = new ProductDAO();

        try {

            int productId = parseInt(productIdField.getText());

            String productName = productNameField.getText();

            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);

            double price = 0.0;

            // Create new product in product table with previous values
            productDAO.newProduct(new Product(productId, productName, date, price));

            // Creates new BoM using values
            insertReqPartsFromVbox(productId);

            price = productDAO.calculateProductPrice(productId);
            productDAO.updateProductPrice(productId, price);

            // Pop-up of create test records
            openCreateTestRecordPopup();

        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID. Please enter a valid number.");
        }
    }

    /**
     * Creates new BoM using values
     *
     * @param productId product id of product
     */
    private void insertReqPartsFromVbox(int productId) {
        Connection connection;
        connection = DatabaseConnection.getInstance();

        try {
            connection.setAutoCommit(false); // Use transaction to handle multiple inserts
            // for each child of partResultBox (Rows)
            for (var node : partResultVBox.getChildren()) {
                if (node instanceof VBox row) {
                    // Get the labels and text field from the VBox
                    // Get partId from label
                    Label idLabel = (Label) row.getChildren().get(0);
                    String partsId = idLabel.getText().replace("Part ID: ", "");

                    // Get part amount from label
                    TextField amountField = (TextField) row.getChildren().get(2); // Assuming the third element is the TextField for required amount
                    String requiredAmount = amountField.getText();

                    // Create new BoM using previous values
                    BillOfMaterialsDAO billOfMaterial = new BillOfMaterialsDAO();
                    billOfMaterial.newRequiredParts(new BillOfMaterials(parseInt(partsId), productId, parseInt(requiredAmount)));
                }
            }
            connection.commit();

        } catch (SQLException ex) {
            System.err.println(ex);
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println(rollbackEx);
            }
        }
    }


    /**
     * Opens and starts up Test record tab
     */
    public void openCreateTestRecordPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/create-test-record-view.fxml"));
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();

            Parent createProductRoot = fxmlLoader.load();

            //Transfers productID to create test controller page
            CreateTestRecordController createTestRecordController = fxmlLoader.getController();
            String productIdValue = productIdField.getText();
            createTestRecordController.setProductId(productIdValue);

            Stage stage = (Stage) productIdField.getScene().getWindow();
            stage.setX(stage.getX() - 120);
            stage.setY(stage.getY() + 100);

            Scene scene = new Scene(createProductRoot, WIDTH, HEIGHT);
            scene.getStylesheets().add(stylesheet);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * If there are values from parts search, show the delete all parts button.
     * Else, delete button.
     */
    private void updateButtonVisibility() {
        // Creates delete all parts button
        Button removeAllButton = new Button("Remove all parts");
        removeAllButton.getStyleClass().add("create-product-button-small");
        removeAllButton.setStyle("-fx-background-color: red;");

        removeAllButton.setOnAction(event -> {
            //Delete all children in partsResultVbox and container
            partResultVBox.getChildren().clear();
            removeAllPartsButtonContainer.getChildren().clear();
        });
        if (partResultVBox.getChildren().isEmpty()) {
            // If no rows, remove the button if it exists
            removeAllPartsButtonContainer.getChildren().remove(removeAllButton);
        } else {
            // If there are rows, ensure the button is added
            if (!removeAllPartsButtonContainer.getChildren().contains(removeAllButton)) {
                removeAllPartsButtonContainer.getChildren().clear();
                removeAllPartsButtonContainer.getChildren().add(removeAllButton);
            }
        }
    }

    /**
     * During an attempt at closing the pop-up, ask the user whether
     * they really would like the close the tab
     */
    @FXML
    protected void onClosePopupButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Cancel Product Creation");
        alert.setContentText("Are you sure you want to cancel?");
        alert.setGraphic(null);

        // Define dialog buttons
        ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
        ButtonType backBtn = new ButtonType("Back", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(confirmBtn, backBtn);

        Button confirmButton = (Button) alert.getDialogPane().lookupButton(confirmBtn);
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(backBtn);

        confirmButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-style: bold;");
        cancelButton.setStyle("-fx-background-color: #ccccff; -fx-text-fill: white; -fx-style: bold");

        // Load the CSS file
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/com/example/protrack/stylesheet.css").toExternalForm());

        // Show confirmation dialog and close if confirmed
        alert.showAndWait().ifPresent(result -> {
            if (result == confirmBtn) {
                ((Stage) closePopupButton.getScene().getWindow()).close();
            }
        });
    }
}
