package com.example.protrack.applicationpages;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WorkStationController {
    @FXML
    public Label workStationName;

    @FXML
    public Label usernameField;

    @FXML
    private ListView<String> orderList;

//    @FXML
//    private VBox step1Box;
//
//    @FXML
//    private VBox step2Box;
//
//    @FXML
//    private CheckBox step1CheckBox;

//    @FXML
//    private CheckBox step2CheckBox;
//
//    @FXML
//    private CheckBox step3CheckBox;
//
//    @FXML
//    private CheckBox step4CheckBox;
//
//    @FXML
//    private Label step1Label;
//
//    @FXML
//    private Label step2Label;
//
//    @FXML
//    private Label step3Label;
//
//    @FXML
//    private Label step4Label;
//
//    @FXML
//    private ScrollPane stepsScrollPane;

    public void initialize() {
        // Initialize the list of orders
        orderList.setItems(FXCollections.observableArrayList("P-0001-1", "P-0001-2", "P-0001-3", "P-0001-4", "P-0002"));

//        // Set event listeners or default states for checkboxes
//        step1CheckBox.setOnAction(e -> toggleStep(1, step1CheckBox.isSelected()));
//        step2CheckBox.setOnAction(e -> toggleStep(2, step2CheckBox.isSelected()));
//
//        // Initialize step 3 and step 4 as disabled by default
//        step3CheckBox.setDisable(true);
//        step4CheckBox.setDisable(true);
    }

    private void toggleStep(int step, boolean isSelected) {
        // Logic to handle step completion (e.g., updating the UI)
//        switch (step) {
//            case 1:
//                if (isSelected) {
//                    step1Label.setStyle("-fx-text-fill: green;");
//                } else {
//                    step1Label.setStyle("-fx-text-fill: black;");
//                }
//                break;
//            case 2:
//                if (isSelected) {
//                    step2Label.setStyle("-fx-text-fill: green;");
//                    // Enable step 3 when step 2 is completed
//                    step3CheckBox.setDisable(false);
//                    step3Label.setStyle("-fx-text-fill: black;");
//                } else {
//                    step2Label.setStyle("-fx-text-fill: black;");
//                    // Disable step 3 if step 2 is uncompleted
//                    step3CheckBox.setDisable(true);
//                    step3Label.setStyle("-fx-text-fill: gray;");
//                }
//                break;
//            default:
//                break;
//        }
    }
}

