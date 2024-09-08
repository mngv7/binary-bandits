package com.example.protrack.applicationpages;

import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPartsController {

    @FXML
    private TextField  partIdField;

    @FXML
    private TextField partNameField;

    @FXML
    private TextField partDescField;

    @FXML
    private TextField partTypeField;

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
                partTypeField.getText().trim().isEmpty() ||
                partSupplierIdField.getText().trim().isEmpty() ||
                partCostField.getText().trim().isEmpty(),
                partIdField.textProperty(),
                partNameField.textProperty(),
                partDescField.textProperty(),
                partTypeField.textProperty(),
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
            String partType = partTypeField.getText();
            int partSupplierId = Integer.parseInt(partSupplierIdField.getText());
            double partCost = Integer.parseInt(partCostField.getText());

            partsDAO.newPart(new Parts(partId, partName, partDesc, partType, partSupplierId, partCost));

            partIdField.setText("");
            partNameField.setText("");
            partDescField.setText("");
            partTypeField.setText("");
            partSupplierIdField.setText("");
            partCostField.setText("");

        }  catch (NumberFormatException e) {
            System.out.println("Invalid part ID. Please enter a valid number.");
        }
    }

    @FXML
    protected void onClosePopupButton() {
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }
}
