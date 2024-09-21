package com.example.protrack.applicationpages;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.example.protrack.Main;
import com.example.protrack.employees.SelectedEmployeeSingleton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.example.protrack.utility.LoggedInUserSingleton;
import com.example.protrack.users.AbstractUser;
import com.example.protrack.users.UsersDAO;
import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmployeesController {

    @FXML
    public VBox employeeNames; // VBox to display the list of employee names and titles

    @FXML
    public Button newUserButton; // Button to create a new user

    @FXML
    public VBox employeeIcons;

    // Initializes the controller and sets up UI components
    public void initialize() {
        Integer loggedInId = LoggedInUserSingleton.getInstance().getEmployeeId();
        UsersDAO usersDAO = new UsersDAO();

        // Disable the 'New User' button if the logged-in user does not have 'HIGH' access level
        newUserButton.setDisable(!usersDAO.getUserById(loggedInId).getAccessLevel().equals("HIGH"));

        // Load and display all users
        loadAllUsers();
    }

    // Loads all users from the database and adds them to the VBox
    public void loadAllUsers() {
        UsersDAO usersDAO = new UsersDAO();
        List<AbstractUser> allUsers = usersDAO.getAllUsers();

        // Iterate through the list of all users and add their information to the UI
        for (AbstractUser user : allUsers) {
            VBox columns = new VBox();
            VBox rows = new VBox();

            // Determine the title of the employee based on their access level
            String employeeTitle = switch (user.getAccessLevel()) {
                case "HIGH" -> "Manager";
                case "MEDIUM" -> "Stock Controller";
                case "LOW" -> "Production Worker";
                default -> "Unknown";
            };

            // Create labels for the employee's name and title
            Button employeeNameButton = new Button();
            employeeNameButton.setText(user.getFirstName() + " " + user.getLastName());
            employeeNameButton.getStyleClass().add("text-button");
            employeeNameButton.setOnAction(event -> {
                handleButtonPress(user.getFirstName(), user.getLastName());
            });

            Label employeeTitleLabel = new Label(employeeTitle);
            Label spacing = new Label(" "); // Spacer to separate labels

            String initials = user.getFirstName().charAt(0) + user.getLastName().substring(0,1);
            Label initialsIcon = new Label(initials);

            // Add labels to the newRow VBox
            rows.getChildren().addAll(employeeNameButton, employeeTitleLabel, spacing);

            columns.getChildren().add(initialsIcon);

            // Add the newRow VBox to the employeeNames VBox
            employeeNames.getChildren().add(rows);
            employeeIcons.getChildren().add(columns);
        }
    }

    private MainController mainController;  // Store the MainController instance

    // Setter for MainController
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void loadEmployeeProfile() {
        if (mainController != null) {
            mainController.employeesExpanded();  // Use the injected MainController instance
        }
    }

    private void handleButtonPress(String firstName, String lastName) {
        SelectedEmployeeSingleton.getInstance().setEmployeeFirstName(firstName);
        SelectedEmployeeSingleton.getInstance().setEmployeeLastName(lastName);
        loadEmployeeProfile();
    }

    // Constants for the popup window
    private static final String TITLE = "Create User";
    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;

    // Opens a popup window to create a new user
    public void openCreateNewUserPopup() {
        try {
            // Load the FXML file for the popup window
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/create-user-view.fxml"));
            Parent addPartsRoot = fxmlLoader.load();

            // Create and configure the popup window
            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED); // Remove window decorations
            popupStage.initModality(Modality.APPLICATION_MODAL); // Make the popup modal
            popupStage.setTitle(TITLE);

            // Create a new scene and add the stylesheet
            Scene scene = new Scene(addPartsRoot, WIDTH, HEIGHT);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);

            // Set the position and show the popup window
            popupStage.setY(150);
            popupStage.setX(390);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace(); // Handle potential I/O errors
        }
    }
}
