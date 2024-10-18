package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.products.TestRecord;
import com.example.protrack.products.TestRecordDAO;
import com.example.protrack.utility.DatabaseConnection;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class CreateTestRecordController {

    @FXML
    public Button closePopupButton;
    @FXML
    public Button addTestRecordButton;
    @FXML
    public Button createProductButton;
    private int numSteps = 0;
    @FXML
    private VBox testRecordsVBox;
    @FXML
    private String productIdLabel;

    public void initialize() {
        HBox newColumn = new HBox();

        numSteps++;
        Label label = new Label("Step " + numSteps + ": ");
        label.getStyleClass().add("parts-table-label");

        TextField textField = new TextField();
        textField.setPromptText("Description");

        ComboBox<Object> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Checkbox", "Text");
        comboBox.setPromptText("Criterion");
        comboBox.setMaxWidth(100);

        Region spacer1 = new Region();
        spacer1.setMinWidth(10);

        TextField textField2 = new TextField();
        textField2.setPromptText("Enter Check Criteria");
        textField2.setMaxWidth(140);

        Region spacer2 = new Region();
        spacer2.setMinWidth(10);

        // for each row add a button that removes that row.
        Button removeButton = new Button("Remove Step");
        removeButton.getStyleClass().add("create-product-button-small");
        removeButton.setStyle("-fx-background-color: red;");
        removeButton.setOnAction(event -> removeRow(newColumn));

        newColumn.getChildren().addAll(label, textField, spacer1, comboBox, textField2, spacer2, removeButton);

        testRecordsVBox.getChildren().add(newColumn);

        testRecordsVBox.setAlignment(Pos.CENTER_LEFT);
    }

    public void setProductId(String value) {
        productIdLabel = value;
    }

    /**
     * Add test record to page
     */
    @FXML
    protected void addTestRecord() {
        HBox newColumn = new HBox();

        numSteps++;
        Label label = new Label("Step " + numSteps + ": ");
        label.getStyleClass().add("parts-table-label");

        TextField textField = new TextField();
        textField.setPromptText("Description");

        ComboBox<Object> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Checkbox", "Text");
        comboBox.setPromptText("Criterion");
        comboBox.setMaxWidth(100);

        Region spacer1 = new Region();
        spacer1.setMinWidth(10);

        TextField textField2 = new TextField();
        textField2.setPromptText("Enter Check Criteria");
        textField2.setMaxWidth(140);

        Region spacer2 = new Region();
        spacer2.setMinWidth(10);

        // for each row add a button that removes that row.
        Button removeButton = new Button("Remove Step");
        removeButton.getStyleClass().add("create-product-button-small");
        removeButton.setStyle("-fx-background-color: red;");
        removeButton.setOnAction(event -> removeRow(newColumn));

        newColumn.getChildren().addAll(label, textField, spacer1, comboBox, textField2, spacer2, removeButton);

        testRecordsVBox.getChildren().add(newColumn);
    }

    /**
     * Delete the selected row from test records table,
     * rearrange the table to account for deleted row.
     *
     * @param newColumn column being deleted
     */
    private void removeRow(HBox newColumn) {
        // Find column being removed and remove it.
        testRecordsVBox.getChildren().remove(newColumn);

        //Remove 1 from numSteps
        numSteps--;

        int numNewSteps = 0;

        //Redo each column while accounting for removed row
        for (int i = 0; i < testRecordsVBox.getChildren().size(); i++) {
            var node = testRecordsVBox.getChildren().getFirst();

            if (node instanceof HBox column) {
                HBox newColumn2 = new HBox();

                numNewSteps++;
                Label label = new Label("Step " + numNewSteps + ": ");

                TextField textField1 = (TextField) column.getChildren().get(1);


                TextField textField2 = (TextField) column.getChildren().get(4);

                Button removeButton = new Button("Remove Step");

                removeButton.setOnAction(event -> removeRow(column));

                newColumn2.getChildren().addAll(label, textField1, column.getChildren().get(2), textField2, removeButton);
                testRecordsVBox.getChildren().remove(column);
                testRecordsVBox.getChildren().add(newColumn2);

            }
        }
    }


    /**
     * Upon an attempt at closing the tab
     * close the tab
     */
    @FXML
    protected void onCreateProductButton() {
        insertTestRecordsToDB();
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Inserts test record to test record database
     */
    private void insertTestRecordsToDB() {
        //Got productId

        // Converts productIdLabel to integer
        int productIdValue = Integer.parseInt(productIdLabel);

        Connection connection;
        connection = DatabaseConnection.getInstance();

        int currentRow = 0;
        try {
            connection.setAutoCommit(false); // Use transaction to handle multiple inserts
            // for each column in testRecordsVBox
            for (var node : testRecordsVBox.getChildren()) {
                if (node instanceof HBox column) {

                    // Add 1 to current row / step
                    currentRow++;

                    int stepId = currentRow;
                    int stepNum = currentRow;

                    // Get Description from first column
                    TextField textField1 = (TextField) column.getChildren().get(1);
                    String description = textField1.getText();

                    // Get Check type from second column
                    ComboBox<Object> comboBox = (ComboBox<Object>) column.getChildren().get(2);
                    String checkType = (String) comboBox.getValue();

                    // Get Check criteria from third column
                    TextField textField2 = (TextField) column.getChildren().get(3);
                    String checkCriteria = textField2.getText();

                    // Create new test record using previous values
                    TestRecordDAO testRecordDAO = new TestRecordDAO();
                    testRecordDAO.newTestRecordStep(new TestRecord(stepId, productIdValue, stepNum, description, checkType, checkCriteria));
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
     * During an attempt at closing the pop-up, ask the user whether
     * they really would like the close the tab
     */
    @FXML
    protected void onClosePopupButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Cancel Test Record Creation");
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
