package com.example.protrack.workorder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkOrdersDAO {

    public interface WorkOrdersDAOInterface {
        List<WorkOrder> getAllWorkOrders() throws SQLException;

        WorkOrder getWorkOrder(Integer id) throws SQLException;

        List<WorkOrder> getWorkOrderByStatus(String status) throws SQLException;

        boolean createWorkOrder(WorkOrder workOrder) throws SQLException;

        boolean updateWorkOrder(WorkOrder workOrder) throws SQLException;

        boolean deleteWorkOrder(Integer id) throws SQLException;
    }
}
