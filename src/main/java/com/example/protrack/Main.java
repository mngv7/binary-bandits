package com.example.protrack;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.ManagerialUser;
import com.example.protrack.users.UsersDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static final String TITLE = "ProTrack";
    public static final int WIDTH = 640*2;
    public static final int HEIGHT = 360*2;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
        scene.getStylesheets().add(stylesheet);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance();
        UsersDAO usersDAO = new UsersDAO();

        //Insert a new user (comment out this code once it has been run).
        //usersDAO.newUser(new ManagerialUser(100, "John", "Doe", "password"));

        usersDAO.createTable();
        launch();
    }
}