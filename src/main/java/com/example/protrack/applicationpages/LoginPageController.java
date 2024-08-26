package com.example.protrack.applicationpages;

import com.example.protrack.Main;

import com.example.protrack.users.UsersDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginPageController {

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button loginButton;

    @FXML
    protected void onLoginButtonClick() throws IOException {
        String firstName = usernameTextField.getText();

        if (checkLoginDetails(firstName)) {
            loadHomePage();
        } else {
            System.out.println("Invalid first name or password.");
        }
    }

    private void loadHomePage() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Main.WIDTH, Main.HEIGHT);
        stage.setScene(scene);
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
            return Objects.equals(usersPassword, passwordTextField.getText());
        }
        return false;
    }
}