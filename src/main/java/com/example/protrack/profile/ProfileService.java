package com.example.protrack.profile;

import com.example.protrack.databaseutil.DatabaseConnection;
import com.example.protrack.users.AbstractUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAO;

import java.sql.Connection;
import java.util.ArrayList;

public class ProfileService {
    private WorkOrdersDAO workOrderDAO;
    private UsersDAO usersDAO;

    public ProfileService(WorkOrdersDAO workOrderDAO, UsersDAO usersDAO) {
        this.workOrderDAO = workOrderDAO;
        this.usersDAO = usersDAO;
    }

    public ProfileService() {
        Connection connection = DatabaseConnection.getInstance();
    }

    public ArrayList<WorkOrder> getPendingWorkOrders() {
        return null;
    }

    public AbstractUser getUser() {
        return null;
    }

}
