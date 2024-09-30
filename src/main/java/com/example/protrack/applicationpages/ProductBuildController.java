package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.productbuild.ProductBuild;
import com.example.protrack.productbuild.ProductBuildDAO;
import com.example.protrack.products.TestRecord;
import com.example.protrack.products.TestRecordDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductBuildController {


    @FXML
    private Button SearchPBButton;

    @FXML
    private ListView<String> productBuildListView;

    @FXML
    private VBox productBuildVBox;


    @FXML
    private VBox productBuildTRVBox;

    //private Workstation currentWorkstation = null;

    private Integer currentWorkstationId = -1;

    private Integer currentProductBuild = -1;

    private ObservableList<ProductBuild> builds = FXCollections.observableArrayList();

    @FXML
    public Button closePopupButton;

    public void setWorkStation(int value) {

        //currentWorkstation = value;
        //System.out.println("This is ws of pb " + currentWorkstation.getWorkstationName());
        currentWorkstationId = value;
        System.out.println("WS ID HERE of pb " + currentWorkstationId);
        //refreshTable();
    }


    public void initialize() {
        loadBuildsFromDB();
        //setupListCellFactory();
    }


    private void loadBuildsFromDB() {

        try {
            ProductBuildDAO productBuildDAO = new ProductBuildDAO();
            List<ProductBuild> buildList = productBuildDAO.getAllProductBuilds();

            for (ProductBuild build : buildList) {
                int buildId = build.getBuildId();
                int productOrderId = build.getProductOrderId();
                float buildCompletion = build.getBuildCompletion();
                int productId = build.getProductId();
                builds.add(new ProductBuild(buildId, productOrderId, buildCompletion, productId));
                System.out.println("Got this build Id" + buildId);

                VBox newRow = new VBox();

                Label idLabel = new Label("Build ID: " + buildId);
                Label idLabel2 = new Label("Product Order ID: " + productOrderId);
                Label idLabel3 = new Label("Build Completion: " + buildCompletion);
                Label idLabel4 = new Label("ProductID: " + productId);

                newRow.getChildren().addAll(idLabel, idLabel2, idLabel3, idLabel4);

                newRow.setOnMouseClicked(event -> selectProductBuild(productId));

                productBuildVBox.getChildren().add(newRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*productBuildListView.setItems(FXCollections.observableArrayList(builds.stream()
                .map(ProductBuild::toString)
                .toArray(String[]::new)));

         */
    }

    /*
    private void setupListCellFactory() {
        productBuildListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #d0d0d0; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-size: 14px;");
                    setOnMouseEntered(e -> setStyle("-fx-background-color: #e0e0e0;"));
                    setOnMouseExited(e -> setStyle("-fx-background-color: #f0f0f0;"));

                    // Handle item click
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1) {
                            int selectedIndex = getIndex();
                            if (selectedIndex >= 0) {
                                ProductBuild selectedItem = builds.get(selectedIndex);
                                selectProductBuild(selectedItem);
                            }
                        }
                    });
                }
            }
        });
    }*/

    private void selectProductBuild(int productId) {
        System.out.println("This is productID in pb " + productId);

        productBuildTRVBox.getChildren().clear();
        List<TestRecord> testRecordsList;
        testRecordsList = loadTestRecord(productId);
        generateTestRecord(testRecordsList);

    }

    private List<TestRecord> loadTestRecord(int productId) {
        List<TestRecord> testRecordsList = new ArrayList<>();
        try {
            TestRecordDAO testRecordDAO = new TestRecordDAO();
            testRecordsList = testRecordDAO.getAllTRFromProductID(productId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return testRecordsList;
    }

    private void generateTestRecord(List<TestRecord> testRecordList) {
        for (TestRecord testRecord : testRecordList) {

            int stepId = testRecord.getStepId();
            int productId = testRecord.getProductId();
            int stepNum = testRecord.getStepNumber();
            String stepDescription = testRecord.getStepDescription();
            String stepCheckType = testRecord.getStepCheckType();
            String stepCheckCriteria = testRecord.getStepCheckCriteria();

            VBox newRow = new VBox();

            Label idLabel = new Label("Step " + stepNum + ":");
            Label idLabel2 = new Label(stepDescription);

            if (stepCheckType.equals("CheckBox")) {
                CheckBox checkBox = new CheckBox("Checkbox");
                newRow.getChildren().addAll(idLabel, idLabel2, checkBox);
            } else {

                TextField textfield = new TextField("Enter stuff here");
                newRow.getChildren().addAll(idLabel, idLabel2, textfield);
            }

            productBuildTRVBox.getChildren().add(newRow);
        }
    }

    public void onClosePopupButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Exit Product Build Page");
        alert.setContentText("Are you sure you want to exit?");
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

    public void onAddPartButton(ActionEvent actionEvent) {
    }


    /*
    @FXML
    protected void PBSearch(ActionEvent actionEvent) {
        for (ProductBuild build : builds) {
            int buildId = build.getBuildId();
            int productOrderId = build.getProductOrderId();
            float buildCompletion = build.getBuildCompletion();
            int productId = build.getProductId();
            //builds.add(new ProductBuild(buildId, productOrderId, buildCompletion, productId));
            System.out.println("Got this build Id" + buildId);

            VBox newRow = new VBox();

            Label idLabel = new Label("Build ID: " + buildId);
            Label idLabel2 = new Label("Product Order ID: " + productOrderId);
            Label idLabel3 = new Label("Build Completion: " + buildCompletion);
            Label idLabel4 = new Label("ProductID: " + productId);

            newRow.getChildren().addAll(idLabel, idLabel2, idLabel3, idLabel4);

            newRow.setOnMouseClicked(event -> selectProductBuild(newRow));

            productBuildVBox.getChildren().add(newRow);
        }
    }*/
}
