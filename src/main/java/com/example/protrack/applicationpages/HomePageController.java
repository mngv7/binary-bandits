package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomePageController {
    @FXML
    private AnchorPane mainContentPane;

    @FXML
    private Button loadContentPaneButton;

    @FXML protected void onLoadContentPaneButtonClick () {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("embedded-view-example.fxml"));
        try {
            AnchorPane mainContent = (AnchorPane) fxmlLoader.load();
            mainContentPane.getChildren().clear();
            mainContentPane.getChildren().add(mainContent);
        } catch (IOException e) {
            System.out.println("Failed to load content pane!");
        }
    }
}
