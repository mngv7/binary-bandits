package com.example.protrack.applicationpages;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.time.Month;
import java.util.HashMap;
import java.util.List;


public class DashboardController {
    @FXML
    public ComboBox<String> monthComboBox;

    HashMap<Integer, Integer> sumOfParts = new HashMap<Integer, Integer>();

    public void initialize() {
        initializeHashMap();
    }

    private void initializeHashMap() {
        PartsDAO partsDAO = new PartsDAO();

        for (Parts part : partsDAO.getAllParts()) {
            sumOfParts.put(part.getPartsId(), 0);
        }
    }

    public void loadMonthlyReport() {
        if (monthComboBox.getValue() != null) {
            populateHashMap((Month.valueOf(monthComboBox.getValue().toUpperCase())).getValue());

        }
    }

    private void populateHashMap(Integer month) {
        List<ProductionUser> productionUsers = new UsersDAO().getProductionUsers();
        List<Customer> customers = new CustomerDAO().getAllCustomers();

        WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);

        for (WorkOrder workOrder : workOrdersDAO.getWorkOrderByMonth(month)) {
            System.out.println(workOrder.getWorkOrderId());
        }
    }
}
