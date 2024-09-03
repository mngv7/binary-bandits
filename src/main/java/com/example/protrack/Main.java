package com.example.protrack;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.products.RequiredPartsDAO;
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

    private static final String TITLE = "ProTrack";
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

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

    public static int getWidth() {
        return WIDTH;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getInstance();
        UsersDAO usersDAO = new UsersDAO();

        RequiredPartsDAO requiredPartsDAO = new RequiredPartsDAO();

        //Insert a new user (comment out this code once it has been run).
        //usersDAO.newUser(new ManagerialUser(100, "John", "Doe", "password"));

        usersDAO.createTable();
        requiredPartsDAO.createTables();
        launch();
    }
}