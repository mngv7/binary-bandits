package com.example.protrack.workorder;

import com.example.protrack.customer.Customer;
import com.example.protrack.users.ProductionUser;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class WorkOrdersDAO {

    public interface WorkOrdersDAOInterface {
        ArrayList<WorkOrder> getAllWorkOrders() throws SQLException;

        WorkOrder getWorkOrder(Integer id) throws SQLException;

        ArrayList<WorkOrder> getWorkOrderByStatus(String status) throws SQLException;

        boolean createWorkOrder(WorkOrder workOrder) throws SQLException;

        boolean updateWorkOrder(WorkOrder workOrder) throws SQLException;

        boolean deleteWorkOrder(Integer id) throws SQLException;
    }
}
