package com.example.protrack.applicationpages;

import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import com.example.protrack.products.RequiredParts;
import com.example.protrack.products.RequiredPartsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateTestRecordController {
    /*
    private static final String TITLE = "Create Test Record";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 360;
    */

    int numSteps = 0;

    @FXML
    public Button addTestRecordButton;

    @FXML
    private VBox testRecordsVBox;

    @FXML
    private VBox removeAllTRButtonContainer;

    @FXML
    public Button createProductButton;

    //private Button removeAllButton;

    public void initialize() {



        testRecordsVBox.getChildren().addListener((ListChangeListener<? super Node>) c -> {
            updateButtonVisibility();
        });

        //updateButtonVisibility();
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

        //Redo each column

        numSteps--;

        int numNewSteps = 0;

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

    }





}
