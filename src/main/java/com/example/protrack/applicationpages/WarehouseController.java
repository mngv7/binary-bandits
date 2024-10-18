package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.database.WorkstationPartDBTable;
import com.example.protrack.requests.Requests;
import com.example.protrack.requests.RequestsDAO;
import com.example.protrack.warehouseutil.LocationsAndContentsDAO;
import com.example.protrack.warehouseutil.MockWorkstation;
import com.example.protrack.warehouseutil.Workstation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* TODO: Now that the database structure is sort of present, how do we implement this well? */
public class WarehouseController {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    public LocationsAndContentsDAO locationsAndContents;
    private MainController mainController;
    @FXML
    private TableView warehousePartTable;
    @FXML
    private TableView<Workstation> workstationTable;
    private List<Workstation> workstations; /* Loaded workstations from a database. */
    @FXML
    private TableColumn<WorkstationPartDBTable, Integer> colWSPartId;
    @FXML
    private TableColumn<WorkstationPartDBTable, String> colWSPartName;
    @FXML
    private TableColumn<WorkstationPartDBTable, Integer> colWSPartQuantity;
    private ObservableList<WorkstationPartDBTable> whPartDBTable;
    private final int warehouseId = 0;
    @FXML
    private Button allocateWorkstationButton;

    public MainController getMainControllerInstance() {
        return this.mainController;
    }

    public void setMainControllerInstance(MainController controller) {
        this.mainController = controller;
    }

    @FXML
    public void initialize() {
        locationsAndContents = new LocationsAndContentsDAO();
        loadWorkstationData(); /* TODO: Could probably remove this part and populate workstations directly. */

        // Create and set the context menu for the workstationTable
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().add(deleteMenuItem);

        deleteMenuItem.setOnAction(event -> handleDeleteWorkstation());

        workstationTable.setOnContextMenuRequested(event -> {
            contextMenu.show(workstationTable, event.getScreenX(), event.getScreenY());
        });
        workstationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colWSPartId.setCellValueFactory(new PropertyValueFactory<>("partID"));
        colWSPartName.setCellValueFactory(new PropertyValueFactory<>("partName"));
        colWSPartQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        whPartDBTable = FXCollections.observableArrayList();
        warehousePartTable.setItems(whPartDBTable);
        warehousePartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshTable();
    }

    /* Mock workstation data load. Intended for testing purposes only. */
    private void mockLoadWorkstationData() {
        if (workstations == null) {
            workstations = new ArrayList<>();

            /* TODO: load from mock/real DB later. For now, make dummy data. */
            workstations.add(new MockWorkstation(0, "Workstation 1", 100));
            workstations.add(new MockWorkstation(1, "Workstation 2", 125));
            workstations.add(new MockWorkstation(2, "Workstation 3", 256));
            workstations.add(new MockWorkstation(3, "Workstation 4", 64));
        }
        workstationTable.setItems(FXCollections.observableArrayList(workstations));
    }

    /*
     * This loads workstation data from the DAO into the workstationTable UI.
     *
     * TODO: Explicit overriding of the workstations == null statement to
     *       allow functions to reload from the DAO.
     */
    private void loadWorkstationData() {
        if (workstations == null) {
            workstations = locationsAndContents.getAllWorkstations();
        }
        workstationTable.setItems(FXCollections.observableArrayList(workstations));
    }

    /* Basically just a getter for functions that only need the in-RAM copy of the DAO data. */
    public List<Workstation> getAllWorkstationsInRAM() {
        return this.workstations;
    }

    /* Handles the delete dialog that removes a workstation. */
    private void handleDeleteWorkstation() {
        Workstation selectedWorkstation = workstationTable.getSelectionModel().getSelectedItem();
        if (selectedWorkstation != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete this workstation?");
            alert.setContentText("This action cannot be undone.");

            if (alert.showAndWait().get() == ButtonType.OK) {
                // Remove the selected item from the table
                workstationTable.getItems().remove(selectedWorkstation);
                // Remove it from the database or data source here
                // TODO: Warehouse shouldn't be null but for some
                //       reason the app isn't using a Warehouse interface anywhere.
                locationsAndContents.removeWorkstation(null, selectedWorkstation);
                workstations.remove(selectedWorkstation);
            }
        }
    }

    /* TODO: Workstation DB and related code is very incomplete, attempt to resolve with the database guys later. */
    @FXML
    private void handleAddWorkstation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/CreateWorkstation.fxml"));
            Parent root = loader.load();

            CreateWorkstationController controller = loader.getController();
            if (controller != null) {
                controller.setParentWarehouseController(this);
            }

            Stage stage = new Stage();
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            root.getStylesheets().add(stylesheet);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Create Workstation");
            stage.setScene(new Scene(root, 400, 220));
            stage.showAndWait();

            loadWorkstationData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWorkstation(Workstation workstation) {
        workstations.add(workstation);
    }

    public void openWorkstation(Workstation workstation) {
        /* TODO: Connect selected workstation to new page. */
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/WorkStation.fxml"));
            Parent root = loader.load();

            // Switch to the Workstation UI.
            /* HACK: Pull the VBox used in MainController from here to clear; layout changes will likely break this. */
            VBox contentPane = (VBox) workstationTable.getParent();
            contentPane.getChildren().clear();
            contentPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace(); // Log the error if FXML loading fails
            System.out.println("Failed to load FXML file: /com/example/protrack/WorkStation.fxml");
        }
    }

    @FXML
    private void handleAllocateWorkstation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/protrack/AllocateWorkstation.fxml"));
        Parent root = loader.load();

        AllocateWorkstationController controller = loader.getController();
        if (controller != null) {
            controller.setParentWarehouseController(this);
        }

        Scene rootScene = new Scene(root, 300, 155);
        String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
        rootScene.getStylesheets().add(stylesheet);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Allocate Workstation");
        stage.setScene(rootScene);
        stage.showAndWait();
    }


    public void refreshTable() {
        whPartDBTable.clear();
        whPartDBTable.addAll(workstationPartDBTableList());
    }

    public List<WorkstationPartDBTable> workstationPartDBTableList() {
        List<WorkstationPartDBTable> wsDBParts = new ArrayList<>();
        LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();


        System.out.println("This is WS ID" + warehouseId);

        wsDBParts = locationsAndContentsDAO.getAllWSParts(warehouseId);

        return wsDBParts;
    }


    /**
     * TODO ID 20 - Process requests. Requires an FXML pop-up.
     *
     * @param actionEvent
     */
    public void handlePartRequests(ActionEvent actionEvent) {
        RequestsDAO currentRequestsDao = new RequestsDAO(); /* TODO: Cache this somewhere? */
        List<Requests> requestsToPassToPage = currentRequestsDao.getAllRequests();
        for (int i = 0; i < requestsToPassToPage.size(); ++i) {
            System.out.println("Request ID " + requestsToPassToPage.get(i).getRequestId() + " is in list.");
        }


        /* TODO: Spawn new FXML page and set items on init with the list above. */
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/ViewPartRequests.fxml"));
            Parent ViewPartsRoot = fxmlLoader.load();

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Part Requests");


            // Set the scene for the pop-up
                Scene scene = new Scene(ViewPartsRoot, 600, HEIGHT);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
