package com.example.protrack.applicationpages;

import com.example.protrack.warehouseutil.Workstation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;


public class WorkStationController {
    private Workstation selectedWorkstation;

    @FXML
    public Label workStationName;

    @FXML
    public Label usernameField;

    @FXML
    private ListView<String> orderList;

    @FXML
    private VBox stepsBox;

    @FXML
    private Button completeButton;

    public void setSelectedWorkstation (Workstation selectedWorkstation) {
        this.selectedWorkstation = selectedWorkstation;
    }

    public void initialize() {
        // Initialize the list of orders
        orderList.setItems(FXCollections.observableArrayList("P-0001-1", "P-0001-2", "P-0001-3", "P-0001-4", "P-0002"));



    }


    private void toggleStep(int step, boolean isSelected) {
        if (isSelected) {
            System.out.println("Step " + step + " completed.");
        } else {
            // Handle the step being marked as incomplete
            System.out.println("Step " + step + " not completed.");
        }


    }

    @FXML
    private void handleListClick(MouseEvent event) {
        String selectedItem = orderList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            updateStepsBasedOnSelection(selectedItem);
        }
    }

    private void updateStepsBasedOnSelection(String selectedItem) {
        stepsBox.getChildren().clear();
        switch (selectedItem) {
            case "P-0001-1":
                updateSteps("Step 67: Instruction 1", "Step 68: Instruction 2", "Step 69: Instruction 3", "This is a product test. Text entry required before moving on.");
                completeButton.setText("Complete P-0001-1");
                break;
            case "P-0001-2":
                updateSteps("Step 70: Instruction A", "Step 71: Instruction B", "Step 72: Instruction C", "This is another test. Enter some data.");
                completeButton.setText("Complete P-0001-2");
                break;
            case "P-0001-3":
                updateSteps("Step 73: Instruction X", "Step 74: Instruction Y", "Step 75: Instruction Z", "Enter additional information here.");
                completeButton.setText("Complete P-0001-3");
                break;
            case "P-0001-4":
                updateSteps("Step 76: Instruction Alpha", "Step 77: Instruction Beta", "Step 78: Instruction Gamma", "Final product test instructions.");
                completeButton.setText("Complete P-0001-4");
                break;
            case "P-0002":
                updateSteps("Step 79: Instruction Delta", "Step 80: Instruction Theta", "Step 81: Instruction Omega", "This is a final test.");
                completeButton.setText("Complete P-0002");
                break;
            default:
                updateSteps("", "", "", "");
                completeButton.setText("Complete");
                break;
        }
    }

    private void updateSteps(String step1Text, String step2Text, String step3Text, String textAreaContent) {
        HBox step1 = new HBox();
        Label step1Label = new Label(step1Text);
        step1Label.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
        CheckBox step1CheckBox = new CheckBox();
        step1CheckBox.setSelected(true);
        step1.getChildren().addAll(step1Label, step1CheckBox);

        // Create Step 2
        HBox step2 = new HBox();
        Label step2Label = new Label(step2Text);
        step2Label.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
        CheckBox step2CheckBox = new CheckBox();
        step2CheckBox.setSelected(true);
        step2.getChildren().addAll(step2Label, step2CheckBox);
//
//        // Create Step 3 with TextArea
//        HBox step3 = new HBox();
//        Label step3Label = new Label(step3Text);
//        step3Label.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
//        VBox step3Box = new VBox();
//        TextArea textArea = new TextArea(textAreaContent);
//        textArea.setPrefHeight(80);
//        CheckBox step3CheckBox = new CheckBox();
//        step3Box.getChildren().addAll(textArea, step3CheckBox);
//        step3.getChildren().addAll(step3Label, step3Box);

        // Add steps to the VBox
        stepsBox.getChildren().addAll(step1, step2);//step3
    }

    @FXML
    private void handleClose() {
        /* TODO: Load warehouse page again. */
        System.out.println("Returning to warehouse page.");
    }
}

