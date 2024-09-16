package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class AddPartsController {

    @FXML
    private TextField  partIdField;

    @FXML
    private TextField partNameField;

    @FXML
    private TextField partDescField;

    @FXML
    private TextField partSupplierIdField;

    @FXML
    private TextField partCostField;

    @FXML
    public Button addPartButton;

    @FXML
    public Button closePopupButton;

    public void initialize() {
        // Create a binding to check if any field is empty
        BooleanBinding emptyFields = Bindings.createBooleanBinding(() ->
                partIdField.getText().trim().isEmpty() ||
                partNameField.getText().trim().isEmpty() ||
                partDescField.getText().trim().isEmpty() ||
                partSupplierIdField.getText().trim().isEmpty() ||
                partCostField.getText().trim().isEmpty(),
                partIdField.textProperty(),
                partNameField.textProperty(),
                partDescField.textProperty(),
                partSupplierIdField.textProperty(),
                partCostField.textProperty()
        );
        addPartButton.disableProperty().bind(emptyFields);
    }

    @FXML
    protected void onAddPartButton() {

        PartsDAO partsDAO = new PartsDAO();

        try {
            int partId = Integer.parseInt(partIdField.getText());
            String partName = partNameField.getText();
            String partDesc = partDescField.getText();
            int partSupplierId = Integer.parseInt(partSupplierIdField.getText());
            double partCost = Double.parseDouble(partCostField.getText());

            partsDAO.newPart(new Parts(partId, partName, partDesc, partSupplierId, partCost));

            partIdField.setText("");
            partNameField.setText("");
            partDescField.setText("");
            partSupplierIdField.setText("");
            partCostField.setText("");

        }  catch (NumberFormatException e) {
            System.out.println("Invalid part ID. Please enter a valid number.");
        }
    }

    @FXML
    protected void onClosePopupButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Cancel Part Creation");
        alert.setContentText("Are you sure you want to cancel?");
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
}
