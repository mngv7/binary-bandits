package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import com.example.protrack.products.BillOfMaterials;
import com.example.protrack.products.BillOfMaterialsDAO;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class CreateProductController {
    private static final String TITLE = "Create Product";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 360;

    @FXML
    public Button closePopupButton;

    @FXML
    public Button partSearchButton;

    @FXML
    private TextField partIdSearchField;

    @FXML
    private VBox partResultVBox;

    @FXML
    private VBox removeAllPartsButtonContainer;

    @FXML
    public Button createProductButton;

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

    @FXML
    protected void partSearch() {
        Connection connection;
        connection = DatabaseConnection.getInstance();

        String partIdStr = partIdSearchField.getText();

        if (partIdStr != null && !partIdStr.trim().isEmpty()) {

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
    @FXML
    protected void onCreateProductButton() {

        ProductDAO productDAO = new ProductDAO();
        BillOfMaterialsDAO billOfMaterial = new BillOfMaterialsDAO();

        try {
            int productId = Integer.parseInt(productIdField.getText());

            String productName = productNameField.getText();

            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);

            productDAO.newProduct(new Product(productId, productName, date));

            insertReqPartsFromVbox(productId);

            openCreateTestRecordPopup();

        } catch (NumberFormatException e) {
            System.out.println("Invalid product ID. Please enter a valid number.");
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

                    BillOfMaterialsDAO billOfMaterial = new BillOfMaterialsDAO();
                    billOfMaterial.newRequiredParts(new BillOfMaterials(Integer.parseInt(partsId), productId, Integer.parseInt(requiredAmount)));
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


    public void openCreateTestRecordPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/create-test-record-view.fxml"));
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();

            Parent createProductRoot = fxmlLoader.load();

            CreateTestRecordController createTestRecordController = fxmlLoader.getController();
            String productIdValue = productIdField.getText();
            createTestRecordController.setProductId(productIdValue);

            Stage stage = (Stage) productIdField.getScene().getWindow();

            Scene scene = new Scene(createProductRoot, WIDTH, HEIGHT);
            scene.getStylesheets().add(stylesheet);
            stage.setScene(scene);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateButtonVisibility() {
        Button removeAllButton = new Button("Remove all parts");
        removeAllButton.setOnAction(event -> {
            //Do things here
            partResultVBox.getChildren().clear();
            removeAllPartsButtonContainer.getChildren().clear();
        });
        if (partResultVBox.getChildren().isEmpty()) {
            // If no rows, remove the button if it exists
            if (removeAllPartsButtonContainer.getChildren().contains(removeAllButton)) {
                removeAllPartsButtonContainer.getChildren().remove(removeAllButton);
            }
        } else {
            // If there are rows, ensure the button is added
            if (!removeAllPartsButtonContainer.getChildren().contains(removeAllButton)) {
                removeAllPartsButtonContainer.getChildren().clear();
                removeAllPartsButtonContainer.getChildren().add(removeAllButton);
            }
        }
    }

    @FXML
    protected void onClosePopupButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Cancel Product Creation");
        alert.setContentText("Are you sure you want to cancel?");
        alert.setGraphic(null);

        DialogPane dialogPane = alert.getDialogPane();
        String stylesheet = Objects.requireNonNull(Main.class.getResource("cancelAlert.css")).toExternalForm();
        dialogPane.getStyleClass().add("cancelDialog");
        dialogPane.getStylesheets().add(stylesheet);

        ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
        ButtonType backBtn = new ButtonType("Back", ButtonBar.ButtonData.NO);



        alert.getButtonTypes().setAll(confirmBtn, backBtn);
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        Node confirmButton = dialogPane.lookupButton(confirmBtn);
        ButtonBar.setButtonData(confirmButton, ButtonBar.ButtonData.LEFT);
        confirmButton.setId("confirmBtn");
        Node backButton = dialogPane.lookupButton(backBtn);
        ButtonBar.setButtonData(backButton, ButtonBar.ButtonData.RIGHT);
        backButton.setId("backBtn");
        alert.showAndWait();
        if (alert.getResult().getButtonData() == ButtonBar.ButtonData.YES) {
            alert.close();
            stage.close();
        } else if (alert.getResult().getButtonData() == ButtonBar.ButtonData.NO) {
            alert.close();
        }
    }
}
