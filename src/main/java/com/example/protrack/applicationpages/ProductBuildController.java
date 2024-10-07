package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.database.ProductBuildWSAmt;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.productbuild.ProductBuild;
import com.example.protrack.productbuild.ProductBuildDAO;
import com.example.protrack.products.BillOfMaterials;
import com.example.protrack.products.BillOfMaterialsDAO;
import com.example.protrack.products.TestRecord;
import com.example.protrack.products.TestRecordDAO;
import com.example.protrack.utility.DatabaseConnection;
import com.example.protrack.warehouseutil.LocationsAndContentsDAO;
import com.example.protrack.warehouseutil.partIdWithQuantity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductBuildController {

    @FXML
    private Button commitButton;

    @FXML
    private TableView<ProductBuildWSAmt> PBWSRequirementTableView;

    @FXML
    private TableColumn<ProductBuildWSAmt, Integer> colPBWSpartId;

    @FXML
    private TableColumn<ProductBuildWSAmt, String > colPBWSpartName;

    @FXML
    private TableColumn<ProductBuildWSAmt, Integer> colPBWSreqAmt;

    @FXML
    private TableColumn<ProductBuildWSAmt, Integer> colWorkstationAmt;

    @FXML
    private Button SearchPBButton;

    @FXML
    private ListView<String> productBuildListView;

    @FXML
    private VBox productBuildVBox;

    @FXML
    private VBox productBuildTRVBox;

    @FXML
    private VBox PBWSRequirementBox;

    //private Workstation currentWorkstation = null;

    private Integer currentWorkstationId = -1;

    private Integer currentProductBuild = -1;

    private ObservableList<ProductBuild> builds = FXCollections.observableArrayList();

    private List<ProductBuildWSAmt> currentBuilds;

    @FXML
    public Button closePopupButton;

    public void setWorkStation(int value) {
        currentWorkstationId = value;
        System.out.println("WS ID HERE of pb " + currentWorkstationId);
    }


    public void initialize() {
        colPBWSpartId.setCellValueFactory(new PropertyValueFactory<>("partId"));
        colPBWSpartName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        colPBWSreqAmt.setCellValueFactory(new PropertyValueFactory<>("reqAmount"));
        colWorkstationAmt.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadBuildsFromDB();
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
                newRow.getStyleClass().add("dynamic-vbox");

                newRow.setOnMouseClicked(event -> selectProductBuild(newRow, productId));

                productBuildVBox.getChildren().add(newRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  void refreshReqTable() {
        PBWSRequirementTableView.getItems().clear();
        PBWSRequirementTableView.getItems().addAll(currentBuilds);
    }

    private void selectProductBuild(VBox vBox, int productId) {
        System.out.println("This is productID in pb " + productId);

        productBuildTRVBox.getChildren().clear();
        List<TestRecord> testRecordsList = loadTestRecord(productId);
        generateTestRecord(testRecordsList);

        if (!(currentBuilds == null)) {
            currentBuilds.clear();
        }

        List<BillOfMaterials> productBoM = loadRequiredParts(productId);
        List<ProductBuildWSAmt> productBuildWSAmtList = loadWorkstationPartsUsingReqParts(productBoM);
        currentBuilds = productBuildWSAmtList;

        PBWSRequirementTableView.getItems().clear();
        PBWSRequirementTableView.getItems().addAll(productBuildWSAmtList);
        //Now using the productBoM generate the table.

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

    /**
     * Gets parts required by product
     */
    private List<BillOfMaterials> loadRequiredParts(int productId) {
        BillOfMaterialsDAO billOfMaterialsDAO = new BillOfMaterialsDAO();
        return billOfMaterialsDAO.getBillOfMaterialsForProduct(productId);
    }

    /**
     * TODO Using the list of req parts get parts from WS
     * TODO If DNE, set to zero
     */
    private List<ProductBuildWSAmt> loadWorkstationPartsUsingReqParts(List<BillOfMaterials> productBoM) {
        List<ProductBuildWSAmt> productBuildWSAmtList = new ArrayList<>();

        for (BillOfMaterials boM : productBoM) {

            final Connection connection;
            connection = DatabaseConnection.getInstance();

            int partsId = boM.getPartsId();

            String partName;

            int requiredAmount = boM.getRequiredAmount();

            //
            try {
                PreparedStatement getWSParts = connection.prepareStatement(
                        "SELECT quantity " +
                                "FROM locationContents a " +
                                "WHERE a.partID = ? " +
                                "AND a.locationID = ? ");
                getWSParts.setInt(1, partsId);
                getWSParts.setInt(2, currentWorkstationId);
                ResultSet rs = getWSParts.executeQuery();

                PartsDAO partsDAO = new PartsDAO();
                Parts parts = partsDAO.getPartById(partsId);
                partName = parts.getName();

                if (rs.next()) {
                    int quantity = rs.getInt("quantity");
                    ProductBuildWSAmt productBuildWSAmt = new ProductBuildWSAmt(partsId, partName, requiredAmount, quantity);
                    productBuildWSAmtList.add(productBuildWSAmt);
                } else {
                    int quantity = 0;
                    ProductBuildWSAmt productBuildWSAmt = new ProductBuildWSAmt(partsId, partName, requiredAmount, quantity);
                    productBuildWSAmtList.add(productBuildWSAmt);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return productBuildWSAmtList;
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

    public void onCommitButton(ActionEvent actionEvent) {
        int canCommit = 1;

        for (ProductBuildWSAmt build : currentBuilds) {
            if (build.getQuantity() < build.getReqAmount()) {
                canCommit = 0;
                break;
            }
        }

        /*
        If there is a lack parts for a build and records are not filled
        shade out commit button
         */
        if (canCommit == 0) {
            System.out.println("Cannot commit");
        } else {
            LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();

            //Remove parts from WS
            for (ProductBuildWSAmt build : currentBuilds) {

                partIdWithQuantity partToRemove = new partIdWithQuantity();
                partToRemove.partsId = build.getPartId();
                partToRemove.quantity = build.getReqAmount();

                int currentPartVal = build.getQuantity() - build.getReqAmount();
                build.setQuantity(currentPartVal);

                refreshReqTable();

                locationsAndContentsDAO.removePartsIdWithQuantityFromLocation(currentWorkstationId, partToRemove);
                //locationsAndContentsDAO.removePartsIdWithQuantityFromLocation();

            }
        }
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
