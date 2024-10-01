package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.utility.LoggedInUserSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class PartsController {

    @FXML
    public Button addPartsButton;

    @FXML
    private TableView<Parts> partsTable;

    @FXML
    private TableColumn<Parts, Integer> colPartsId;

    @FXML
    private TableColumn<Parts, String> colName;

    @FXML
    private TableColumn<Parts, String> colDescription;

    @FXML
    private TableColumn<Parts, Integer> colSupplierId;

    @FXML
    private TableColumn<Parts, Double> colCost;

    private ObservableList<Parts> partsList;

    /**
     * Initialize parts page with values
     */
    public void initialize() {
        Integer loggedInId = LoggedInUserSingleton.getInstance().getEmployeeId();
        UsersDAO usersDAO = new UsersDAO();

        // Disable add parts button if access level is low
        addPartsButton.setDisable(usersDAO.getUserById(loggedInId).getAccessLevel().equals("LOW"));

        // Set cell value factories for table columns
        colPartsId.setCellValueFactory(new PropertyValueFactory<>("partsId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        partsList = FXCollections.observableArrayList();
        partsTable.setItems(partsList);
        partsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Refresh parts table
        refreshTable();
    }

    /**
     * Refreshes the table with parts data
     */
    public void refreshTable() {
        PartsDAO partsDAO = new PartsDAO();
        partsList.clear();
        partsList.addAll(partsDAO.getAllParts());
    }

    private static final String TITLE = "Add Parts";
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    /**
     * Create pop-up when "Add Parts" is pressed
     */
    public void openAddPartsPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/Parts/add-parts-view.fxml"));
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
}
