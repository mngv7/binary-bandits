package com.example.protrack;

import com.example.protrack.products.*;
import com.example.protrack.users.UsersDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        ProductDAO productDAO = new ProductDAO();
        UsersDAO usersDAO = new UsersDAO();
        RequiredPartsDAO requiredPartsDAO = new RequiredPartsDAO();
        StepsDAO stepsDAO = new StepsDAO();
        TestRecordDAO testRecordDAO = new TestRecordDAO();

        productDAO.createTable();
        usersDAO.createTable();
        stepsDAO.createTable();
        testRecordDAO.createTable();
        requiredPartsDAO.createTable();


        //Insert a new user (comment out this code once it has been run).
        //usersDAO.newUser(new ManagerialUser(100, "John", "Doe", "password"));

        //(Integer productId, String name, Date dateCreated, Integer employeeId, Integer reqPartsId, Integer PIId, String status)
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        /*
        productDAO.newProduct(new Product(102, "Test Product", date, 3, 15, 1, "In progress"));
        productDAO.newProduct(new Product(103, "Test Product", date, 3, 16, 1, "In progress"));
        productDAO.newProduct(new Product(104, "Test Product", date, 3, 17, 1, "In progress"));

        // Steps(Integer stepsId, String partsId, Integer stepNum, String stepDescription, String checkType, String checkCriteria)
        stepsDAO.newSteps(new Steps(7, "P2", 1, "Do X", "Checkbox", "False"));
        stepsDAO.newSteps(new Steps(10, "P127", 2, "Do Y", "TextBox", "Did it Lol"));
        stepsDAO.newSteps(new Steps(8, "P65", 3, "Do Z", "Checkbox", "False"));

        // newTestRecord(TestRecords testRecords)
        // Has all the steps of product 10 (7, 10, 8);
        testRecordDAO.newTestRecord(new TestRecords(12, 102, 7));
        testRecordDAO.newTestRecord(new TestRecords(10, 102, 10));
        testRecordDAO.newTestRecord(new TestRecords(15, 102, 8));

        // RequiredParts(Integer reqPartsId, String partsId, Integer requiredAmt, Integer currentAmt)
        requiredPartsDAO.newRequiredParts(new RequiredParts(15, "P2", 7, 0));
        requiredPartsDAO.newRequiredParts(new RequiredParts(16, "P127", 3, 2));
        requiredPartsDAO.newRequiredParts(new RequiredParts(17, "P65", 1, 1));
        */

        // Dummy data for the products:


        launch();
    }
}