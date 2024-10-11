package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.users.ManagerialUser;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.users.WarehouseUser;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

public class CreateNewUserController {

    // FXML annotations to bind UI elements
    @FXML
    public Button closePopupButton;

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField lastNameField;

    @FXML
    public DatePicker dobPicker;

    @FXML
    public TextField emailField;

    @FXML
    public TextField phoneNumberField;

    @FXML
    public ComboBox<String> genderComboBox;

    @FXML
    public ComboBox<String> accessLevelComboBox;

    @FXML
    public Button addUserButton;

    private EmployeesController employeesController;  // Reference to EmployeesController

    public void setEmployeesController(EmployeesController employeesController) {
        this.employeesController = employeesController;
    }

    /**
     * Method to handle the action when the close popup button is clicked.
     * Displays a confirmation dialog asking if the user is sure they want to cancel.
     */
    public void onClosePopupButton() {
        // Create a confirmation alert dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED); // Set the style of the alert
        alert.setHeaderText("Cancel User Creation");
        alert.setContentText("Are you sure you want to cancel?");
        alert.setGraphic(null);

        DialogPane dialogPane = alert.getDialogPane();
        // Load custom stylesheet for the alert dialog
        String stylesheet = Objects.requireNonNull(Main.class.getResource("cancelAlert.css")).toExternalForm();
        dialogPane.getStyleClass().add("cancelDialog");
        dialogPane.getStylesheets().add(stylesheet);

        // Define buttons for the alert dialog
        ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
        ButtonType backBtn = new ButtonType("Back", ButtonBar.ButtonData.NO);

        // Set the buttons in the alert dialog
        alert.getButtonTypes().setAll(confirmBtn, backBtn);
        Stage stage = (Stage) closePopupButton.getScene().getWindow(); // Get the current stage
        Node confirmButton = dialogPane.lookupButton(confirmBtn); // Find the confirm button
        ButtonBar.setButtonData(confirmButton, ButtonBar.ButtonData.LEFT);
        confirmButton.setId("confirmBtn"); // Set ID for styling
        Node backButton = dialogPane.lookupButton(backBtn); // Find the back button
        ButtonBar.setButtonData(backButton, ButtonBar.ButtonData.RIGHT);
        backButton.setId("backBtn"); // Set ID for styling
        alert.showAndWait(); // Show the alert dialog and wait for user response

        // Handle user response
        if (alert.getResult().getButtonData() == ButtonBar.ButtonData.YES) {
            alert.close(); // Close the alert
            stage.close(); // Close the stage (popup window)
        } else if (alert.getResult().getButtonData() == ButtonBar.ButtonData.NO) {
            alert.close(); // Just close the alert without closing the stage
        }
    }

    /**
     * Method to create a new user based on the input fields and selected access level.
     * Throws SQLException if there is an issue with database operations.
     */
    public void createNewUser() throws SQLException {
        UsersDAO usersDAO = new UsersDAO(); // Create an instance of UsersDAO for database operations

        // Retrieve input values from UI elements
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneNumberField.getText();
        String gender = genderComboBox.getValue();
        String password = "default"; // Default password for new users, implementation for new users to set their own passwords coming later.
        LocalDate localDate = dobPicker.getValue();
        Date sqlDate = Date.valueOf(localDate); // Convert LocalDate to SQL Date
        Integer employeeId = usersDAO.getMaxEmployeeId() + 1; // Get next available employee ID

        // Create new user based on selected access level
        switch (accessLevelComboBox.getValue()) {
            case "HIGH":
                usersDAO.newUser(new ManagerialUser(employeeId, firstName, lastName, sqlDate, email, phone, gender, password));
                break;
            case "MEDIUM":
                usersDAO.newUser(new WarehouseUser(employeeId, firstName, lastName, sqlDate, email, phone, gender, password));
                break;
            case "LOW":
                usersDAO.newUser(new ProductionUser(employeeId, firstName, lastName, sqlDate, email, phone, gender, password));
                break;
            default:
                System.out.println("Unknown access level."); // Handle unknown access level
                break;
        }

        if (employeesController != null) {
            employeesController.loadAllUsers();  // Refresh the employee list
        }

        // Close the popup window after user creation
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }
}
