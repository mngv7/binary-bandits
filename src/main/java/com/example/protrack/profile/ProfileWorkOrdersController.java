    package com.example.protrack.profile;

import com.example.protrack.applicationpages.Product;
import com.example.protrack.databaseutil.DatabaseConnection;
import javafx.fxml.FXML;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileWorkOrdersController {

    Connection connection = DatabaseConnection.getInstance();



    @FXML
    private void displayPendingWorkOrders() {

    }

    private void acceptWorkOrder() {

    }
}