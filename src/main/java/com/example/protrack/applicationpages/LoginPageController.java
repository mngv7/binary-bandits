package com.example.protrack.applicationpages;

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

public class LoginPageController {

    @FXML
    public Label loginErrorMessage;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button loginButton;

    private Integer loginAttempts = 0;

    @FXML
    protected void onLoginButtonClick() throws IOException {
        String firstName = usernameTextField.getText();

        if (checkLoginDetails(firstName)) {
            usernameTextField.getStyleClass().remove("login-error");
            passwordTextField.getStyleClass().remove("login-error");
            loginAttempts = 0;
            loadHomePage();
        } else {
            loginErrorMessage.setText("Invalid first name or password.");
            usernameTextField.getStyleClass().add("login-error");
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

    private void loadHomePage() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.hide();
        stage.setMaximized(true);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/main_view.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(getClass().getResource("/org/example/main_view").toExternalForm());
    }

    private boolean isInputValid() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        return !username.trim().isEmpty() && !password.trim().isEmpty();
    }

    private boolean checkLoginDetails(String firstName) {
        if (isInputValid()) {
            UsersDAO usersDAO = new UsersDAO();
            String usersPassword = usersDAO.getPasswordByFirstName(firstName);

            if (usersPassword != null) {
                return BCrypt.checkpw(passwordTextField.getText(), usersPassword);
            }
        }
        return false;
    }
}