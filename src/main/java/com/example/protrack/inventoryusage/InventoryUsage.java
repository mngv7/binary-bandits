package com.example.protrack.inventoryusage;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.products.BillOfMaterialsDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import com.example.protrack.workorderproducts.WorkOrderProductsDAOImplementation;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

public class InventoryUsage {
    HashMap<Integer, Integer> sumOfParts = new HashMap<>();

    public HashMap<String, Integer> sortMonthlyUsage(HashMap<String, Integer> monthlyTotalUsage) {
        // Create a list from the entry set of the HashMap
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(monthlyTotalUsage.entrySet());

        Map<String, Integer> monthOrder = new HashMap<>();
        monthOrder.put("January", 1);
        monthOrder.put("February", 2);
        monthOrder.put("March", 3);
        monthOrder.put("April", 4);
        monthOrder.put("May", 5);
        monthOrder.put("June", 6);
        monthOrder.put("July", 7);
        monthOrder.put("August", 8);
        monthOrder.put("September", 9);
        monthOrder.put("October", 10);
        monthOrder.put("November", 11);
        monthOrder.put("December", 12);

        // Sort the entry list by month order
        entryList.sort(Comparator.comparingInt(entry -> monthOrder.get(entry.getKey())));

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public HashMap<String, Integer> loadPartUsageStatistics() {
        // Get the current month
        YearMonth currentMonth = YearMonth.now();

        // Create a map to store total part usage for each month
        HashMap<String, Integer> monthlyTotalUsage = new HashMap<>();

        // Loop through the last 6 months
        for (int i = 0; i < 6; i++) {
            YearMonth month = currentMonth.minusMonths(i);
            String monthName = month.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            int monthValue = month.getMonthValue();

            // Reset the sumOfParts for each month
            initializeHashMap();

            populateHashMap(monthValue);

            // Calculate the total usage for the current month
            int totalUsageForMonth = sumOfParts.values().stream().mapToInt(Integer::intValue).sum();

            // Store the total usage for the current month
            monthlyTotalUsage.put(monthName, totalUsageForMonth);
        }

        return monthlyTotalUsage;
    }

    public HashMap<Integer, Integer> initializeHashMap() {
        PartsDAO partsDAO = new PartsDAO();

        for (Parts part : partsDAO.getAllParts()) {
            sumOfParts.put(part.getPartsId(), 0);
        }

        return sumOfParts;
    }

    public HashMap<Integer, Integer> populateHashMap(Integer month) {
        List<ProductionUser> productionUsers = new UsersDAO().getProductionUsers();
        List<Customer> customers = new CustomerDAOImplementation().getAllCustomers();

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
        return sumOfParts;
    }
}
