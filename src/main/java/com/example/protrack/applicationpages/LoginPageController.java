package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.utility.LoggedInUserSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginPageController {

    @FXML
    private Label loginErrorMessage;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField fullNameTextField;

    @FXML
    private Button loginButton;

    private Integer loginAttempts = 0;

    @FXML
    protected void onLoginButtonClick() throws IOException, SQLException {
        UsersDAO usersDAO = new UsersDAO();
        String fullName = fullNameTextField.getText();

        // Check if the login details are correct
        if (checkLoginDetails(fullName)) {
            // Remove error styles if login is successful
            fullNameTextField.getStyleClass().remove("login-error");
            passwordTextField.getStyleClass().remove("login-error");
            loginAttempts = 0;

            // Retrieve and store the employee ID
            Integer employeeId = usersDAO.getEmployeeIdByFullName(fullName);
            LoggedInUserSingleton.getInstance().setEmployeeId(employeeId);

            loadHomePage();
        } else {
            // Show error message and apply error styles
            loginErrorMessage.setText("Invalid first name or password.");
            fullNameTextField.getStyleClass().add("login-error");
            passwordTextField.getStyleClass().add("login-error");
            loginAttempts++;
        }

        // Disable login after 3 failed attempts
        if (loginAttempts >= 3) {
            disableLogin();
        }
    }


    @FXML
    protected void disableLogin() {
        loginButton.setDisable(true);
        loginErrorMessage.setText("Too many incorrect login attempts, please contact supervisor.");
    }


    private void loadHomePage() throws IOException {
        // Hide the current stage
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.hide();

        // Load the home page FXML and get the controller
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/main_view.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();

        // Set employee details in the main controller
        UsersDAO usersDAO = new UsersDAO();
        String fullName = fullNameTextField.getText();
        mainController.setEmployeeName(fullName);
        Integer employeeId = LoggedInUserSingleton.getInstance().getEmployeeId();
        mainController.setEmployeeTitle(usersDAO.getUserById(employeeId).getAccessLevel());

        // Set up the new scene and show the stage
        Scene scene = new Scene(root, Main.getWidth(), Main.getHeight());
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/protrack/stylesheet.css")).toExternalForm());
    }


    private boolean isInputValid() {
        String fullName = fullNameTextField.getText();
        String password = passwordTextField.getText();

        // Check if full name and password are provided
        if (fullName.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        UsersDAO usersDAO = new UsersDAO();

        try {
            // Ensure full name contains at least two parts
            String[] nameParts = fullName.trim().split("\\s+");
            if (nameParts.length < 2) {
                return false;
            }

            // Check if employee ID exists for the given full name
            return usersDAO.getEmployeeIdByFullName(fullName) != null;
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }


    private boolean checkLoginDetails(String fullName) throws SQLException {
        // Check if the input fields are valid
        if (isInputValid()) {
            UsersDAO usersDAO = new UsersDAO();
            Integer employeeId = usersDAO.getEmployeeIdByFullName(fullName); // Get employee ID based on full name
            String usersPassword = usersDAO.getUserById(employeeId).getPassword(); // Retrieve the stored password for comparison

            // Verify the provided password matches the stored hashed password
            if (usersPassword != null) {
                return BCrypt.checkpw(passwordTextField.getText(), usersPassword);
            }
        }
        return false; // Return false if any validation fails or passwords don't match
    }

    public void initialize() {
        toggleFocusTraversal(false); // Disable focus traversal initially

        // Enable focus traversal for input fields and button on mouse click
        fullNameTextField.setOnMouseClicked(event -> toggleFocusTraversal(true));
        passwordTextField.setOnMouseClicked(event -> toggleFocusTraversal(true));
        loginButton.setOnMouseClicked(event -> toggleFocusTraversal(true));
    }

    private void toggleFocusTraversal(boolean status) {
        // Set focus traversal status for input fields and button
        fullNameTextField.setFocusTraversable(status);
        passwordTextField.setFocusTraversable(status);
        loginButton.setFocusTraversable(status);
    }

}
