package com.example.protrack.profile;

import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAO.WorkOrdersDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

public class ProfileWorkOrdersController {

    private ListView pendingOrdersListView;

    private WorkOrdersDAOImplementation workOrdersDAOimpl;

    @FXML
    private void displayPendingWorkOrders() {
        ArrayList<WorkOrder> pendingWorkOrders = null;
        try {
            pendingWorkOrders = workOrdersDAOimpl.getWorkOrderByStatus();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        pendingOrdersListView.getItems().setAll(pendingWorkOrders);
    }

    private void acceptWorkOrder() {

    }
}