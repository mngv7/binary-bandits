package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.employees.SelectedEmployeeSingleton;
import com.example.protrack.users.AbstractUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.utility.LoggedInUserSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Controller class for managing the employees page.
 * Handles displaying the list of users and opening the user creation popup.
 */
public class EmployeesController {

    // Constants for the popup window
    private static final String TITLE = "Create User";
    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;

    @FXML
    public VBox employeeNames; // VBox to display the list of employee names and titles
    @FXML
    public Button newUserButton; // Button to create a new user
    @FXML
    public VBox employeeIcons; // VBox for displaying employee icons
    @FXML
    public GridPane employeesGridPane; // GridPane for arranging employee icons and names
    @FXML
    public VBox iconAndNameGroup; // Group containing employee icon and name together

    private MainController mainController;  // Store the MainController instance

    /**
     * Initializes the controller and sets up UI components.
     * Disables the 'New User' button for users without 'HIGH' access level.
     */
    public void initialize() {
        Integer loggedInId = LoggedInUserSingleton.getInstance().getEmployeeId();
        UsersDAO usersDAO = new UsersDAO();

        // Disable the 'New User' button if the logged-in user does not have 'HIGH' access level
        newUserButton.setDisable(!usersDAO.getUserById(loggedInId).getAccessLevel().equals("HIGH"));

        // Load and display all users
        loadAllUsers();
    }

    /**
     * Loads all users from the database and populates the GridPane with their names and icons.
     */
    public void loadAllUsers() {
        UsersDAO usersDAO = new UsersDAO();
        List<AbstractUser> allUsers = usersDAO.getAllUsers();

        // Initialize column and row indexes for arranging the employee details
        int columnIndex = 0;
        int rowIndex = 0;

        // Iterate through the list of users and add their details to the UI
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

            // Create a button with the employee's full name and handle selection on click
            Button employeeNameButton = new Button();
            employeeNameButton.setText(user.getFirstName() + " " + user.getLastName());
            employeeNameButton.getStyleClass().add("text-button");
            employeeNameButton.setOnAction(event -> handleButtonPress(user.getFirstName(), user.getLastName()));

            // Create labels for employee title and a spacer
            Label employeeTitleLabel = new Label(employeeTitle);
            Label spacing = new Label(" ");

            // Create initials label for the employee icon
            String initials = user.getFirstName().charAt(0) + user.getLastName().substring(0, 1);
            Label initialsIcon = new Label(initials);
            initialsIcon.getStyleClass().add("initials-icon");

            // Add labels to the rows and columns
            rows.getChildren().addAll(employeeNameButton, employeeTitleLabel, spacing);
            columns.getChildren().add(initialsIcon);

            employeesGridPane.add(columns, columnIndex, rowIndex);
            employeesGridPane.add(rows, columnIndex + 1, rowIndex);

            // Update the row and column indexes for arranging users
            rowIndex++;
            if (rowIndex > 4) {
                rowIndex = 0;
                columnIndex += 2;
            }
        }
    }

    /**
     * Setter for injecting the MainController instance.
     *
     * @param mainController The MainController instance to be injected.
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Loads the expanded employee profile by notifying the MainController.
     */
    @FXML
    private void loadEmployeeProfile() {
        if (mainController != null) {
            mainController.employeesExpanded();
        }
    }

    /**
     * Handles the button press event for selecting an employee.
     * Stores the selected employee's first and last name in the Singleton and loads the profile.
     *
     * @param firstName The first name of the selected employee.
     * @param lastName  The last name of the selected employee.
     */
    private void handleButtonPress(String firstName, String lastName) {
        SelectedEmployeeSingleton.getInstance().setEmployeeFirstName(firstName);
        SelectedEmployeeSingleton.getInstance().setEmployeeLastName(lastName);
        loadEmployeeProfile();
    }

    /**
     * Opens a popup window for creating a new user.
     */
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

            // Inject EmployeesController instance to the popup's controller
            CreateNewUserController createNewUserController = fxmlLoader.getController();
            createNewUserController.setEmployeesController(this);

            // Set the position and show the popup window
            popupStage.setY(150);
            popupStage.setX(390);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
