package com.example.protrack;

import com.example.protrack.users.UsersDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        String stylesheet = Main.class.getResource("stylesheet.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setTitle("ProTrack");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance();
        UsersDAO usersDAO = new UsersDAO();

        usersDAO.createTable();
        launch();
    }
}