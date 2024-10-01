package com.example.protrack.applicationpages;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.products.BillOfMaterialsDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import com.example.protrack.workorderproducts.WorkOrderProductsDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.Month;
import java.util.HashMap;
import java.util.List;


public class DashboardController {
    @FXML
    public ComboBox<String> monthComboBox;

    @FXML
    public VBox partsAndQuantity;

    HashMap<Integer, Integer> sumOfParts = new HashMap<>();

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
            displayParts();
        }
    }

    private void populateHashMap(Integer month) {
        List<ProductionUser> productionUsers = new UsersDAO().getProductionUsers();
        List<Customer> customers = new CustomerDAO().getAllCustomers();

        // Initialize DAOs
        WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);
        WorkOrderProductsDAOImplementation productOrders = new WorkOrderProductsDAOImplementation();
        BillOfMaterialsDAO bomDAO = new BillOfMaterialsDAO();

        // Loop through all work orders for the given month
        for (WorkOrder workOrder : workOrdersDAO.getWorkOrderByMonth(month)) {
            // Loop through all products in the current work order
            for (WorkOrderProduct productOrder : productOrders.getWorkOrderProductsByWorkOrderId(workOrder.getWorkOrderId())) {
                int productId = productOrder.getProductId();
                int quantity = productOrder.getQuantity();

                HashMap<Integer, Integer> productIdParts = bomDAO.getPartsAndAmountsForProduct(productId);

                // Multiply each part's required amount by the product order's quantity
                for (HashMap.Entry<Integer, Integer> entry : productIdParts.entrySet()) {
                    int partsId = entry.getKey();
                    int requiredAmount = entry.getValue();
                    int totalRequiredAmount = requiredAmount * quantity;

                    // Add the total amount to the sum HashMap, updating it if the part already exists
                    sumOfParts.put(partsId, sumOfParts.getOrDefault(partsId, 0) + totalRequiredAmount);
                }
            }
        }
    }

    public void displayParts() {
        PartsDAO partsDAO = new PartsDAO();
        partsAndQuantity.getChildren().clear();

        // Loop through the sumOfParts HashMap
        for (HashMap.Entry<Integer, Integer> entry : sumOfParts.entrySet()) {
            int partsId = entry.getKey();
            int totalRequiredAmount = entry.getValue();

            // Only print if the value is not zero
            if (totalRequiredAmount > 0) {
                Label label = new Label();
                label.setText(partsDAO.getPartById(partsId).getName() + " " + totalRequiredAmount);
                partsAndQuantity.getChildren().add(label);
            }
        }
        initializeHashMap();
    }
}
