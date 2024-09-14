package com.example.protrack.applicationpages;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.products.*;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class CreateTestRecordController {
    /*
    private static final String TITLE = "Create Test Record";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 360;
    */

    private int numSteps = 0;

    @FXML
    public Button closePopupButton;

    @FXML
    public Button addTestRecordButton;

    @FXML
    private VBox testRecordsVBox;

    @FXML
    private VBox removeAllTRButtonContainer;

    @FXML
    public Button createProductButton;

    private String productIdLabel;

    public void initialize() {

        VBox newRow = new VBox();
        HBox newColumn = new HBox();
        numSteps++;
        Label label = new Label("Step " + numSteps + ": ");

        TextField textField = new TextField();
        textField.setPromptText("Description");

        ComboBox<Object> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Checkbox 1", "Text Entry");

        TextField textField2 = new TextField();
        textField2.setPromptText("Check Criteria");

        Button removeButton = new Button("Remove Step");
        removeButton.setOnAction(event -> removeRow(newColumn));

        newColumn.getChildren().addAll(label, textField, comboBox, textField2, removeButton);

        testRecordsVBox.getChildren().add(newColumn);

        testRecordsVBox.getChildren().addListener((ListChangeListener<? super Node>) c -> {
            updateButtonVisibility();
        });

    }

    public void setProductId(String value) {
        productIdLabel = value;
    }

    private void updateButtonVisibility() {
        Button removeAllButton = new Button("Remove all Steps");
        removeAllButton.setOnAction(event -> {
            //Do things here
            testRecordsVBox.getChildren().clear();
            removeAllTRButtonContainer.getChildren().clear();
            numSteps = 0;
        });
        if (testRecordsVBox.getChildren().isEmpty()) {
            // If no rows, remove the button if it exists
            if (removeAllTRButtonContainer.getChildren().contains(removeAllButton)) {
                removeAllTRButtonContainer.getChildren().remove(removeAllButton);
            }
        } else {
            // If there are rows, ensure the button is added
            if (!removeAllTRButtonContainer.getChildren().contains(removeAllButton)) {
                removeAllTRButtonContainer.getChildren().clear();
                removeAllTRButtonContainer.getChildren().add(removeAllButton);
            }
        }
    }

    //Adds Test record to page
    @FXML
    protected void addTestRecord() {
        VBox newRow = new VBox();
        HBox newColumn = new HBox();
        numSteps++;
        Label label = new Label("Step " + numSteps + ": ");

        TextField textField = new TextField();
        textField.setPromptText("Description");

        ComboBox<Object> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Checkbox 1", "Text Entry");

        TextField textField2 = new TextField();
        textField2.setPromptText("Check Criteria");

        Button removeButton = new Button("Remove Step");
        removeButton.setOnAction(event -> removeRow(newColumn));

        newColumn.getChildren().addAll(label, textField, comboBox, textField2, removeButton);

        testRecordsVBox.getChildren().add(newColumn);

        /*
        Make Vbox
            Make HBox

         */
    }

    private void removeRow(HBox newColumn) {
        testRecordsVBox.getChildren().remove(newColumn);

        //Remove 1 from numSteps
        numSteps--;

        int numNewSteps = 0;

        //Redo each column
        for (int i = 0; i < testRecordsVBox.getChildren().size(); i++) {
            var node = testRecordsVBox.getChildren().getFirst();

            if (node instanceof  HBox column) {
                HBox newColumn2 = new HBox();

                numNewSteps++;
                Label label = new Label("Step " + numNewSteps + ": ");

                // Get the labels and text field from the HBox
                TextField textField1 = (TextField) column.getChildren().get(1);

                //ComboBox<Object> comboBox = new ComboBox<>();
                //comboBox.setItems(FXCollections.observableArrayList("Checkbox 1", "Text Entry");
                //comboBox.getItems().addAll("Checkbox 1", "Text Entry");
                //comboBox.setValue();

                TextField textField2 = (TextField) column.getChildren().get(3);

                Button removeButton = new Button("Remove Step");
                removeButton.setOnAction(event -> removeRow(column));

                newColumn2.getChildren().addAll(label, textField1, column.getChildren().get(2), textField2, removeButton);
                testRecordsVBox.getChildren().remove(column);
                testRecordsVBox.getChildren().add(newColumn2);

            }
        }

        /*
        for (var node : testRecordsVBox.getChildren()) {
            if (node instanceof HBox column) {

                numNewSteps++;
                Label label = new Label("Step " + numNewSteps + ": ");

                // Get the labels and text field from the VBox
                TextField textField1 = (TextField) column.getChildren().get(1);

                ComboBox<Object> comboBox = new ComboBox<>();
                //comboBox.setItems(FXCollections.observableArrayList("Checkbox 1", "Text Entry");
                comboBox.getItems().addAll("Checkbox 1", "Text Entry");
                comboBox.setValue(column.getChildren().get(2));

                TextField textField2 = (TextField) column.getChildren().get(3);



                String partsId = idLabel.getText().replace("Part ID: ", "");

                TextField amountField = (TextField) column.getChildren().get(2); // Assuming the third element is the TextField for required amount
                String requiredAmount = amountField.getText();

                RequiredPartsDAO requiredPartsDAO = new RequiredPartsDAO();
                requiredPartsDAO.newRequiredParts(new RequiredParts(Integer.parseInt(partsId), productId, Integer.parseInt(requiredAmount)));


            }
        }
        */

    }


    @FXML
    protected void onCreateProductButton() {
        insertTestRecordsToDB();
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }

    private void insertTestRecordsToDB() {
        //Got productId

        int productIdValue = Integer.parseInt(productIdLabel);
        //System.out.println("This is productValue " + productIdValue);

        Connection connection;
        connection = DatabaseConnection.getInstance();

        int currentRow = 0;
        try {
            connection.setAutoCommit(false); // Use transaction to handle multiple inserts
            for (var node : testRecordsVBox.getChildren()) {
                if (node instanceof HBox column) {

                    currentRow++;

                    int stepId = currentRow;
                    int stepNum = currentRow;

                    TextField textField1 = (TextField) column.getChildren().get(1);
                    String description = textField1.getText();

                    ComboBox<Object> comboBox = (ComboBox<Object>) column.getChildren().get(2);
                    String checkType = (String) comboBox.getValue();
                    //System.out.println("CheckType " + checkType);

                    TextField textField2 = (TextField) column.getChildren().get(3);
                    String checkCriteria = textField2.getText();

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

    @FXML
    protected void onClosePopupButton() {
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }

}
