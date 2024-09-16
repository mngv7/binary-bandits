package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.users.UsersDAO;
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

public class LoginPageController {

    private Integer loggedInId;

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

        // DO NOT MERGE TO MAIN WITH (true) IN THE IF CHECK.
        if (checkLoginDetails(fullName)) {
            fullNameTextField.getStyleClass().remove("login-error");
            passwordTextField.getStyleClass().remove("login-error");
            loginAttempts = 0;
            loggedInId = usersDAO.getEmployeeIdByFullName(fullName);
            loadHomePage();
        } else {
            loginErrorMessage.setText("Invalid first name or password.");
            fullNameTextField.getStyleClass().add("login-error");
            passwordTextField.getStyleClass().add("login-error");
            loginAttempts++;
        }

        if (loginAttempts >= 3) {
            disableLogin();
        }
    }

    @FXML
    protected void disableLogin() {
        loginButton.setDisable(true);
        loginErrorMessage.setText("Too many incorrect login attempts, please contact supervisor.");
    }

    private void loadHomePage() throws IOException, SQLException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.hide();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/main_view.fxml"));
        Parent root = fxmlLoader.load();

        MainController mainController = fxmlLoader.getController();
        UsersDAO usersDAO = new UsersDAO();

        String fullName = fullNameTextField.getText();

        mainController.setEmployeeName(fullName);
        Integer employeeId = usersDAO.getEmployeeIdByFullName(fullName);
        mainController.setEmployeeTitle(usersDAO.getUserById(employeeId).getAccessLevel());

        Scene scene = new Scene(root, Main.getWidth(), Main.getHeight());
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(getClass().getResource("/com/example/protrack/stylesheet.css").toExternalForm());
    }


    private boolean isInputValid() throws SQLException {
        String fullName = fullNameTextField.getText();
        String password = passwordTextField.getText();

        UsersDAO usersDAO = new UsersDAO();

        return !fullName.trim().isEmpty() &&
                !password.trim().isEmpty() &&
                usersDAO.getEmployeeIdByFullName(fullName) != null;
    }

    private boolean checkLoginDetails(String fullName) throws SQLException {
        if (isInputValid()) {
            UsersDAO usersDAO = new UsersDAO();

            Integer employeeId = usersDAO.getEmployeeIdByFullName(fullName);

            String usersPassword = usersDAO.getUserById(employeeId).getPassword();

            if (usersPassword != null) {
                return BCrypt.checkpw(passwordTextField.getText(), usersPassword);
            }
        }
        return false;
    }

    public void initialize() {
        toggleFocusTraversal(false);

        fullNameTextField.setOnMouseClicked(event -> toggleFocusTraversal(true));
        passwordTextField.setOnMouseClicked(event -> toggleFocusTraversal(true));
        loginButton.setOnMouseClicked(event -> toggleFocusTraversal(true));
    }

    private void toggleFocusTraversal(boolean status) {
        fullNameTextField.setFocusTraversable(status);
        passwordTextField.setFocusTraversable(status);
        loginButton.setFocusTraversable(status);
    }

    public Integer getLoggedInId() {
        return loggedInId;
    }
}