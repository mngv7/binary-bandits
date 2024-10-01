package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.productbuild.ProductBuild;
import com.example.protrack.productbuild.ProductBuildDAO;
import com.example.protrack.timesheets.Timesheets;
import com.example.protrack.timesheets.TimesheetsDAO;
import com.example.protrack.users.ProductionUser;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.lang.invoke.StringConcatFactory;
import java.text.Format;
import java.time.LocalDateTime;
import java.util.Objects;

public class TimesheetsController {

    @FXML
    private Label employeeId;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;
    @FXML
    private ComboBox<ProductBuild> productBuildComboBox;

    // Buttons for adding timesheet information and closing the popup
    @FXML
    public Button addTimesheet;
    @FXML
    public Button closePopupButton;

    public void setEmployeeId(String employeeId) {this.employeeId.setText(employeeId);}

    public void initialize() {
        // Populate the ComboBoxes with data from the database
        productBuildComboBox.getItems().setAll(new ProductBuildDAO().getAllProductBuilds());

        // Create a binding to check if any essential field is empty
        BooleanBinding emptyFields = Bindings.createBooleanBinding(() ->
            startTimeField.getText().trim().isEmpty() ||
                endTimeField.getText().trim().isEmpty() ||
                startDatePicker.getEditor().getText() == null ||
                endDatePicker.getEditor().getText() == null ||
                productBuildComboBox.getSelectionModel().isEmpty(),
                startTimeField.textProperty(),
                endTimeField.textProperty()
        );

        addTimesheet.disableProperty().bind(emptyFields);
    }

    // Method to clear input fields
    private void clearPartInputFields() {
        productBuildComboBox.getSelectionModel().clearSelection();
        startDatePicker.getEditor().clear();
        endDatePicker.getEditor().clear();
        startTimeField.clear();
        endTimeField.clear();
    }

    /**
     * Event handler for the "Close Popup" button.
     * Displays a confirmation dialog asking the user if they want to cancel part creation.
     */
    @FXML
    protected void onClosePopupButton() {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Cancel Part Creation");
        alert.setContentText("Are you sure you want to cancel?");
        alert.setGraphic(null);

        // Apply custom stylesheet to the alert dialog
        DialogPane dialogPane = alert.getDialogPane();
        String stylesheet = Objects.requireNonNull(Main.class.getResource("cancelAlert.css")).toExternalForm();
        dialogPane.getStyleClass().add("cancelDialog");
        dialogPane.getStylesheets().add(stylesheet);

        // Define the confirm and back buttons
        ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
        ButtonType backBtn = new ButtonType("Back", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(confirmBtn, backBtn);

        // Get the current stage (popup window)
        Stage stage = (Stage) closePopupButton.getScene().getWindow();

        // Set button data for confirm and back buttons
        Node confirmButton = dialogPane.lookupButton(confirmBtn);
        ButtonBar.setButtonData(confirmButton, ButtonBar.ButtonData.LEFT);
        confirmButton.setId("confirmBtn");
        Node backButton = dialogPane.lookupButton(backBtn);
        ButtonBar.setButtonData(backButton, ButtonBar.ButtonData.RIGHT);
        backButton.setId("backBtn");

        // Show the alert and handle the user's response
        alert.showAndWait();
        if (alert.getResult().getButtonData() == ButtonBar.ButtonData.YES) {
            // Close the stage if user confirms cancellation
            alert.close();
            stage.close();
        } else if (alert.getResult().getButtonData() == ButtonBar.ButtonData.NO) {
            // Close the alert if user decides to go back
            alert.close();
        }
    }

    /**
     * Event handler for the "Add Timesheet" button.
     * Parses the input fields to create a new Timesheet object and adds it to the database.
     */
    @FXML
    protected void onAddTimesheetButton() {
        // Create a TimesheetDAO to handle database connection
        TimesheetsDAO timesheetsDAO = new TimesheetsDAO();

        try {
            // Get the selected items from the ComboBox
            Integer selectedPO = Integer.parseInt(productBuildComboBox.getSelectionModel().getSelectedItem().toString());

            Integer employee = Integer.parseInt(employeeId.getText());

            // Get the selected dates from the DatePicker
            String startDate = startDatePicker.getValue().toString();
            String endDate = endDatePicker.getValue().toString();

            String startTime = startTimeField.getText();
            String endTime = endTimeField.getText();

            String startDateTimeString = startDate + "T" + startTime + ":00.00";
            String endDateTimeString = endDate + "T" + endTime + ":00.00";

            // Combine date and time strings to get DateTime
            LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeString);
            LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeString);

            timesheetsDAO.newTimesheet(new Timesheets(startDateTime, endDateTime, employee, selectedPO));

            clearPartInputFields();
        } catch (NumberFormatException e) {
            // Alert handles invalid number formats for quantity
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Quantity");
            alert.setContentText("Please enter a valid number for quantity.");
            alert.showAndWait();
        }
    }

}
