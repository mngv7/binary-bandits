package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.database.WorkstationPartDBTable;
import com.example.protrack.warehouseutil.LocationsAndContentsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewWorkstation2 {
    @FXML
    private TableView wsPartTable;

    @FXML
    private TableColumn<WorkstationPartDBTable, Integer> colWSPartId;

    @FXML
    private TableColumn<WorkstationPartDBTable, String> colWSPartName;

    @FXML
    private TableColumn<WorkstationPartDBTable, Integer> colWSPartQuantity;

    @FXML
    private TableColumn<WorkstationPartDBTable, String> colAddPart;

    @FXML
    public Button closePopupButton;

    private ObservableList<WorkstationPartDBTable> wsPartDBTable;

    private int workStationId = -1;

    public void initialize() {
        colWSPartId.setCellValueFactory(new PropertyValueFactory<>("partID"));
        colWSPartName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        colWSPartQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        colAddPart.setCellFactory(new Callback<TableColumn<WorkstationPartDBTable, String>, TableCell<WorkstationPartDBTable, String>>() {
            @Override
            public TableCell<WorkstationPartDBTable, String> call(TableColumn<WorkstationPartDBTable, String> param) {
                return new TableCell<>() {
                    private final Button addPartToPB = new Button("Add to Product Build");

                    {
                        addPartToPB.getStyleClass().add("add-to-product-build-button");

                        // Handles row deletion
                        addPartToPB.setOnAction(event -> {
                            WorkstationPartDBTable wsPartDBTableItem = getTableView().getItems().get(getIndex());
                            //getTableView().getItems().remove(product);  // Remove from table

                            System.out.println("Add this part to Product build " + wsPartDBTableItem.getPartName());
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {

                            setGraphic(addPartToPB);  // Set the button to trash icon if there is an active row
                        }
                    }
                };
            }
        });

        wsPartDBTable = FXCollections.observableArrayList();
        wsPartTable.setItems(wsPartDBTable);
        wsPartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // refresh WS part table
        refreshTable();
    }

    public void refreshTable() {
        wsPartDBTable.clear();
        wsPartDBTable.addAll(workstationPartDBTableList());
    }

    public List<WorkstationPartDBTable> workstationPartDBTableList() {
        List<WorkstationPartDBTable> wsDBParts = new ArrayList<>();
        LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();

        //TODO Temp var, need to ensure that setWorkstationV2 works
        //workStationId = 1;
        System.out.println("This is WS ID" + workStationId);

        wsDBParts = locationsAndContentsDAO.getAllWSParts(workStationId);

        return wsDBParts;
    }

    /**
     * Grabs WS ID from Allocate WS
     * @param value WS ID
     */
    public void setWorkStationIdV3(Integer value) {
        workStationId = value;
        System.out.println("WS ID HERE " + workStationId);
        refreshTable();
    }

    @FXML
    protected void onClosePopupButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Exit Workstation");
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

    private static final String TITLE = "Parts Request Form";
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    /**
     * Create pop-up when "Create Part Request" is pressed
     */
    public  void createPartRequest() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/request-stock-transfer.fxml"));
            Parent addPartsRoot = fxmlLoader.load();

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(TITLE);

            // Set the scene for the pop-up
            Scene scene = new Scene(addPartsRoot, WIDTH, HEIGHT);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);
            popupStage.setY(150);
            popupStage.setX(390);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTableButton(ActionEvent actionEvent) {
        refreshTable();
    }
}
