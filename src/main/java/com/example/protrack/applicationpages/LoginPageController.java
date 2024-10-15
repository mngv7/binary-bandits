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

/**
 * Controller for managing the login functionality for the application.
 * Handles user authentication and transitions to the home page upon successful login.
 */
public class LoginPageController {

    /** Label for displaying error messages related to login attempts. */
    @FXML
    private Label loginErrorMessage;

    /** Field for entering the user's password. */
    @FXML
    private PasswordField passwordTextField;

    /** Field for entering the user's full name. */
    @FXML
    private TextField fullNameTextField;

    /** Button to initiate the login process. */
    @FXML
    private Button loginButton;

    /** Counter for the number of failed login attempts. */
    private Integer loginAttempts = 0;

    /**
     * Handles the login button click event.
     * Validates the user input and loads the home page if authentication is successful.
     *
     * @throws IOException  if there is an issue loading the home page.
     * @throws SQLException if there is an issue retrieving user details from the database.
     */
    @FXML
    protected void onLoginButtonClick() throws IOException, SQLException {
        UsersDAO usersDAO = new UsersDAO();
        String fullName = fullNameTextField.getText();

        if (checkLoginDetails(fullName)) {
            fullNameTextField.getStyleClass().remove("login-error");
            passwordTextField.getStyleClass().remove("login-error");
            loginAttempts = 0;

            Integer employeeId = usersDAO.getEmployeeIdByFullName(fullName);
            LoggedInUserSingleton.getInstance().setEmployeeId(employeeId);

            loadHomePage();
        } else {
            loginErrorMessage.setText("Invalid first name or password.");
            fullNameTextField.getStyleClass().add("login-error");
            passwordTextField.getStyleClass().add("login-error");
            loginAttempts++;
        }

        if (loginAttempts >= 3) {
            disableLoginButton();
        }
    }

    /**
     * Disables the login button after too many failed login attempts.
     */
    @FXML
    protected void disableLoginButton() {
        loginButton.setDisable(true);
        loginErrorMessage.setText("Too many incorrect login attempts, please contact supervisor.");
    }

    /**
     * Loads the home page upon successful login.
     *
     * @throws IOException if there is an issue loading the home page.
     */
    private void loadHomePage() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.hide();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/main_view.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();

        UsersDAO usersDAO = new UsersDAO();
        String fullName = fullNameTextField.getText();
        mainController.setEmployeeName(fullName);
        Integer employeeId = LoggedInUserSingleton.getInstance().getEmployeeId();
        mainController.setEmployeeTitle(usersDAO.getUserById(employeeId).getAccessLevel());

        Scene scene = new Scene(root, Main.getWidth(), Main.getHeight());
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/protrack/stylesheet.css")).toExternalForm());
    }

    /**
     * Validates the user's full name and password.
     *
     * @return true if the user input is valid, false otherwise.
     */
    private boolean isUsernamePasswordValid() {
        String fullName = fullNameTextField.getText();
        String password = passwordTextField.getText();

        if (fullName.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }

        UsersDAO usersDAO = new UsersDAO();
        String[] nameParts = fullName.trim().split("\\s+");
        if (nameParts.length < 2) {
            return false;
        }

        return usersDAO.getEmployeeIdByFullName(fullName) != null;
    }

    /**
     * Verifies the login details by checking the full name and password against the database.
     *
     * @param fullName the full name entered by the user.
     * @return true if the login details are correct, false otherwise.
     * @throws SQLException if there is an issue retrieving user details from the database.
     */
    private boolean checkLoginDetails(String fullName) throws SQLException {
        if (isUsernamePasswordValid()) {
            UsersDAO usersDAO = new UsersDAO();
            Integer employeeId = usersDAO.getEmployeeIdByFullName(fullName);
            String usersPassword = usersDAO.getUserById(employeeId).getPassword();

            if (usersPassword != null) {
                return BCrypt.checkpw(passwordTextField.getText(), usersPassword);
            }
        }
        return false;
    }

    /**
     * Initializes the login page by disabling focus traversal for input fields and login button.
     * Sets up event handlers to enable focus traversal on mouse click.
     */
    public void initialize() {
        toggleFocusTraversal(false);

        fullNameTextField.setOnMouseClicked(event -> toggleFocusTraversal(true));
        passwordTextField.setOnMouseClicked(event -> toggleFocusTraversal(true));
        loginButton.setOnMouseClicked(event -> toggleFocusTraversal(true));
    }

    /**
     * Toggles the focus traversal for the input fields and login button.
     *
     * @param status whether focus traversal should be enabled or disabled.
     */
    private void toggleFocusTraversal(boolean status) {
        fullNameTextField.setFocusTraversable(status);
        passwordTextField.setFocusTraversable(status);
        loginButton.setFocusTraversable(status);
    }
}
