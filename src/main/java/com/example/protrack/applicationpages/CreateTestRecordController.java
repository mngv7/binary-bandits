package com.example.protrack.applicationpages;

import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import com.example.protrack.products.RequiredPartsDAO;
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

        newColumn.getChildren().addAll(label, textField, comboBox, textField2);

        testRecordsVBox.getChildren().add(newColumn);

        /*
        Make Vbox
            Make HBox

         */
    }


    @FXML
    protected void onCreateProductButton() {

    }





}
