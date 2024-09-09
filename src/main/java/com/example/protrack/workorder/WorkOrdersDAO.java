package com.example.protrack.workorder;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.AbstractUser;

import java.sql.*;
import java.util.HashMap;

public class WorkOrdersDAO {
    private Connection connection;

    public WorkOrdersDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS workorders ("
                            + "workOrderId INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "workOrderOwner VARCHAR, "
                            + "customerName VARCHAR NOT NULL, "
                            + "orderDate DATETIME NOT NULL, "
                            + "deliveryDate DATETIME, "
                            + "shippingAddress VARCHAR NOT NULL, "
                            + "productsId INTEGER NOT NULL , "
                            + "status VARCHAR NOT NULL , "
                            + "subtotal DOUBLE NOT NULL , "
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void newWorkOrder(AbstractUser user) {

    }

    public void getWorkOrder(Integer workOrderId) {

    }

    public HashMap<Integer, WorkOrder> getWorkOrderByStatus(){

        return null;
    }

}
