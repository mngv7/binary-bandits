package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.productbuild.ProductBuild;
import com.example.protrack.productbuild.ProductBuildDAO;
import com.example.protrack.productorders.ProductOrder;
import com.example.protrack.productorders.ProductOrderDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ProductOrderController {

    @FXML
    private Button closePopupButton;

    @FXML
    private ComboBox<ProductOrder> productOrderComboBox;

    @FXML
    private TableView<ProductBuild> productBuildTableView;

    @FXML
    private TableColumn<ProductBuild, Integer> colbuildID;

    @FXML
    private TableColumn<ProductBuild, Integer> colproductOrderID;

    @FXML
    private TableColumn<ProductBuild, Integer> colbuildCompletion;

    @FXML
    private TableColumn<ProductBuild, Integer> colproductID;

    private Integer currentWorkstationId = -1;

    //private Integer currentProductOrderId = -1;

    public void initialize() {
        // Populate the ComboBoxes with data from the database
        productOrderComboBox.getItems().setAll(new ProductOrderDAO().getAllProductOrder());

        productOrderComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Do something with new value
            setProductBuildTable(newValue);
        });

        colbuildID.setCellValueFactory(new PropertyValueFactory<>("buildId"));
        colproductOrderID.setCellValueFactory(new PropertyValueFactory<>("productOrderId"));
        colbuildCompletion.setCellValueFactory(new PropertyValueFactory<>("buildCompletion"));
        colproductID.setCellValueFactory(new PropertyValueFactory<>("productId"));

        //productBuildTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    public void setWorkStation(int value) {
        currentWorkstationId = value;
        //System.out.println("WS ID HERE of po " + currentWorkstationId);
    }

    private void setProductBuildTable(ProductOrder newValue) {
        ProductBuildDAO productBuildDAO = new ProductBuildDAO();
        List<ProductBuild> productBuildList = productBuildDAO.getAllProductBuildsWithPOID(newValue.getProductOrderID());

        productBuildTableView.getItems().clear();
        productBuildTableView.getItems().addAll(productBuildList);
    }

    public void onClosePopupButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Exit Product Order Page");
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

    public void goToProductBuildButton(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/product-build.fxml"));

        try {
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();

            Parent createAllocateWSRoot = fxmlLoader.load();

            ProductBuildController productBuildController = fxmlLoader.getController();
            //LocationsAndContentsDAO locationsAndContentsDAO = new LocationsAndContentsDAO();
            //int workstationId = locationsAndContentsDAO.getLocationIDFromAlias(workstationComboBox.getValue());
            productBuildController.setWorkStation(currentWorkstationId);
            ProductOrder productOrder = productOrderComboBox.getSelectionModel().getSelectedItem();
            //System.out.println("ProductORDERID " + productOrder.getProductOrderID());
            productBuildController.setProductOrder(productOrder.getProductOrderID());

            Scene scene = new Scene(createAllocateWSRoot, Main.getWidth(), Main.getHeight());
            scene.getStylesheets().add(stylesheet);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
