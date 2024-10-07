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
                productBuildComboBox.getSelectionModel().isEmpty(),
                startTimeField.textProperty(),
                endTimeField.textProperty()
        );

        addTimesheet.disableProperty().bind(emptyFields);
    }

    // Method to clear input fields
    private void clearPartInputFields() {
        productBuildComboBox.getSelectionModel().clearSelection();
        startTimeField.clear();
        endTimeField.clear();
    }

    /**
     * Event handler for the "Close Popup" button.
     * Displays a confirmation dialog asking the user if they want to cancel part creation.
     */
    @FXML
    protected void onClosePopupButton() {
        // Get the current stage (popup window) and close it
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
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

            String startTime = startTimeField.getText();
            String endTime = endTimeField.getText();

            // Checks if startTime needs a starting 0 (if hour is single digit)
            if (startTime.length() != 5){
                startTime = "0" + startTime;
            }

            String startDateTimeString = LocalDateTime.now().toLocalDate().toString() + "T" + startTime + ":00.00";
            String endDateTimeString = LocalDateTime.now().toLocalDate().toString() + "T" + endTime + ":00.00";

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
