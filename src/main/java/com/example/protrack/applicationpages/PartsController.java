package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public void initialize() {
        colPartsId.setCellValueFactory(new PropertyValueFactory<>("partsId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        partsList = FXCollections.observableArrayList();
        partsTable.setItems(partsList);
        partsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();
    }

    public void refreshTable() {
        PartsDAO partsDAO = new PartsDAO();
        partsList.clear();
        partsList.addAll(partsDAO.getAllParts());
    }

    private static final String TITLE = "Add Parts";
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    public void openAddPartsPopup () {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/add-parts-view.fxml"));
            Parent addPartsRoot = fxmlLoader.load();

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(TITLE);

            Scene scene = new Scene(addPartsRoot, WIDTH, HEIGHT);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("main_app.css")).toExternalForm();
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
