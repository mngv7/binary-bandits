    package com.example.protrack.profile;

import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.HashMap;

public class ProfileWorkOrdersController {

    private ListView pendingOrdersListView;

    private WorkOrdersDAO workOrdersDAO;

    @FXML
    private void displayPendingWorkOrders() {
        HashMap<Integer, WorkOrder> pendingWorkOrders = workOrdersDAO.getWorkOrderByStatus();
        pendingOrdersListView.getItems().setAll(pendingWorkOrders);
    }

    private void acceptWorkOrder() {

    }
}