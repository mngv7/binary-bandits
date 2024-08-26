package com.example.protrack.applicationpages;

import com.example.protrack.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Button loginButton;

    @FXML
    protected void onLoginButtonClick() throws IOException {
        if (isInputValid()) {
            loadHomePage();
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
}