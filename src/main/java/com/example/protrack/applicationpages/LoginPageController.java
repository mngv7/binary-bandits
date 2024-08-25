package com.example.protrack.applicationpages;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LoginPageController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("This code runs upon logging in.");
    }
}