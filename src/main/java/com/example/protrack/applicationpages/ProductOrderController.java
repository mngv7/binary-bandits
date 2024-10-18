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
    private Button goToProductBuildButton;

    @FXML
    private TableView<ProductBuild> productBuildTableView;

    @FXML
    private VBox productBuildVBox;

    @FXML
    private TableColumn<ProductBuild, Integer> colbuildID;

    @FXML
    private TableColumn<ProductBuild, Integer> colproductOrderID;

    @FXML
    private TableColumn<ProductBuild, Integer> colbuildCompletion;

    @FXML
    private TableColumn<ProductBuild, Integer> colproductID;

    private Integer currentWorkstationId = -1;

    private MainController mainController;

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

    public void setMainController(MainController controller) {
        this.mainController = controller;
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

    private void generateProductBuildTable(List<ProductBuild> productBuildList) {
        for (ProductBuild productBuild : productBuildList) {
            System.out.println("Test");
        }
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

    public void goToProductBuildButton() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/product-build.fxml"));
            Parent content = fxmlLoader.load();

            ProductBuildController productBuildController = fxmlLoader.getController();
            productBuildController.setWorkStation(currentWorkstationId);
            ProductOrder productOrder = productOrderComboBox.getSelectionModel().getSelectedItem();
            productBuildController.setProductOrder(productOrder.getProductOrderID());

            VBox dynamicVBox = mainController.getDynamicVBox();
            dynamicVBox.getChildren().clear();
            dynamicVBox.getChildren().add(content);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
