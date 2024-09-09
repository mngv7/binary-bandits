    package com.example.protrack.applicationpages;

import com.example.protrack.databaseutil.DatabaseConnection;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileWorkOrdersController {

    Connection connection = DatabaseConnection.getInstance();

    private HashMap<Integer, ArrayList<Product>> getPendingWorkOrders() throws SQLException {
        return new HashMap<>();
    }

    @FXML
    private void displayPendingWorkOrders() {

    }

    private void acceptWorkOrder() {

    }
}