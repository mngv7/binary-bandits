package org.example;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {
    // Constants
    public static final String TITLE = "ProTrack Inventory Management";

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setMaximized(true);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_layout.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add(getClass().getResource("main_app").toExternalForm());
    }

    public static void main(String[] args) {
        launch();
    }
}