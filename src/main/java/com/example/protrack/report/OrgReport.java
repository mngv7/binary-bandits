package com.example.protrack.report;

import com.example.protrack.users.UsersDAO;
import com.example.protrack.utility.LoggedInUserSingleton;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAO;
import com.example.protrack.workorderproducts.WorkOrderProductsDAO;
import com.example.protrack.workorderproducts.WorkOrderProduct;

import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgReport implements Report {

    private final UsersDAO usersDAO;
    private final WorkOrdersDAO workOrdersDAO;
    private final WorkOrderProductsDAO workOrderProductsDAO;

    private Integer currentEmployeeId = LoggedInUserSingleton.getInstance().getEmployeeId();

    public OrgReport(UsersDAO usersDAO, WorkOrdersDAO workOrdersDAO, WorkOrderProductsDAO workOrderProductsDAO) {
        this.usersDAO = usersDAO;
        this.workOrdersDAO = workOrdersDAO;
        this.workOrderProductsDAO = workOrderProductsDAO;
    }

    // Forecasting production based on parts and production rates
    public Map<Long, Double> forecastWorkOrderChartValues() {

        Map<Long, Double> forecastData = new HashMap<>();
        List<WorkOrder> workOrders = workOrdersDAO.getAllWorkOrders();

        for (WorkOrder order : workOrders) {

            double productionRate = calculateProductionRate(order);
            // Use order dates to populate the forecastData
            forecastData.put(order.getOrderDate().toEpochSecond(ZoneOffset.of("+10:00")), productionRate);
        }
        return forecastData;
    }

    // Calculate expected cycle time for orders
    public Map<Long, Double> calculateOrdersExpectedCycleTimeChartValues() {
        Map<Long, Double> expectedCycleTimes = new HashMap<>();
        List<WorkOrder> workOrders = workOrdersDAO.getAllWorkOrders();

        for (WorkOrder order : workOrders) {
            double expectedTime = calculateExpectedCycleTime(order);
            expectedCycleTimes.put(order.getOrderDate().toEpochSecond(ZoneOffset.of("+10:00")), expectedTime);
        }
        return expectedCycleTimes;
    }

    // Calculate actual cycle time for orders
    public Map<Long, Double> calculateOrdersActualCycleTimeChartValues() {
        Map<Long, Double> actualCycleTimes = new HashMap<>();
        List<WorkOrder> workOrders = workOrdersDAO.getAllWorkOrders();

        for (WorkOrder order : workOrders) {
            double actualTime = calculateActualCycleTime(order);
            actualCycleTimes.put(order.getOrderDate().toEpochSecond(ZoneOffset.of("+10:00")), actualTime);
        }
        return actualCycleTimes;
    }

    // Calculate throughput chart values
    public Map<Long, Double> calculateThroughputChartValues() {
        Map<Long, Double> throughputData = new HashMap<>();
        List<WorkOrder> workOrders = workOrdersDAO.getAllWorkOrders();

        for (WorkOrder order : workOrders) {
            double throughput = calculateThroughput(order);
            throughputData.put(order.getOrderDate().toEpochSecond(ZoneOffset.of("+10:00")), throughput);
        }
        return throughputData;
    }

    // Calculate defect rate
    public Double calculateDefectRate() {
        List<WorkOrder> allWorkOrders = workOrdersDAO.getAllWorkOrders();
        int totalDefects = 0;
        int totalProducts = 0;

        for (WorkOrder workOrder : allWorkOrders) {
            List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(workOrder.getWorkOrderId());
            for (WorkOrderProduct product : products) {
                totalProducts += product.getQuantity();

                totalDefects += getDefectCount(product);
            }
        }

        return (totalProducts > 0) ? (double) totalDefects / totalProducts * 100 : 0.0;
    }

    // Calculate perfect order rate
    public Double calculatePerfectRate() {
        List<WorkOrder> allWorkOrders = workOrdersDAO.getAllWorkOrders();
        int perfectOrders = 0;
        int totalOrders = allWorkOrders.size();

        for (WorkOrder order : allWorkOrders) {

            if (isPerfectOrder(order)) {
                perfectOrders++;
            }
        }

        return (totalOrders > 0) ? (double) perfectOrders / totalOrders * 100 : 0.0;
    }

    // Calculate total orders by status
    public Map<String, Integer> calculateTotalOrdersByStatus() {
        List<WorkOrder> allWorkOrders = workOrdersDAO.getAllWorkOrders();
        Map<String, Integer> orderStatusCount = new HashMap<>();

        for (WorkOrder order : allWorkOrders) {
            String status = order.getStatus();
            orderStatusCount.put(status, orderStatusCount.getOrDefault(status, 0) + 1);
        }

        return orderStatusCount;
    }

    // Calculate total number of orders
    public Integer calculateTotalOrders() {
        List<WorkOrder> allWorkOrders = workOrdersDAO.getAllWorkOrders();
        return allWorkOrders.size();
    }

    // Calculate total products produced
    public Integer calculateTotalProductsProduced() {
        List<WorkOrder> allWorkOrders = workOrdersDAO.getAllWorkOrders();
        int totalProducts = 0;

        for (WorkOrder order : allWorkOrders) {
            List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(order.getWorkOrderId());
            for (WorkOrderProduct product : products) {
                totalProducts += product.getQuantity();
            }
        }

        return totalProducts;
    }

    // Calculate total parts used
    public Integer calculateTotalPartsUsed() {
        List<WorkOrder> allWorkOrders = workOrdersDAO.getAllWorkOrders();
        int totalParts = 0;

        for (WorkOrder order : allWorkOrders) {
            List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(order.getWorkOrderId());
            for (WorkOrderProduct product : products) {

                totalParts += getPartsUsed(product);
            }
        }

        return totalParts;
    }

    // Calculate total production time
    public Double calculateTotalProductionTime() {
        List<WorkOrder> allWorkOrders = workOrdersDAO.getAllWorkOrders();
        double totalTime = 0.0;

        for (WorkOrder order : allWorkOrders) {

            totalTime += calculateTimeForOrder(order);
        }

        return totalTime;
    }

    // Calculate total production cost
    public Double calculateTotalProductionCost() {
        List<WorkOrderProduct> allWorkOrderProducts = workOrderProductsDAO.getAllWorkOrderProducts();
        double totalCost = 0.0;

        for (WorkOrderProduct product : allWorkOrderProducts) {
            totalCost += product.getQuantity() * product.getPrice(); // Assuming totalPrice is already calculated
        }

        return totalCost;
    }

    // calculate production rate
    private double calculateProductionRate(WorkOrder order) {
        return 0.0;
    }

    // calculate expected cycle time
    private double calculateExpectedCycleTime(WorkOrder order) {
        return 0.0;
    }

    // calculate actual cycle time
    private double calculateActualCycleTime(WorkOrder order) {
        return 0.0; // Placeholder
    }

    // calculates throughput
    private double calculateThroughput(WorkOrder order) {
        return 0.0;
    }

    /* to be implemented later
    // determine the defect count for the product
    private int getDefectCount(WorkOrderProduct product) {
        return 0; // Placeholder
    }

    // determine if an order is perfect
    private boolean isPerfectOrder(WorkOrder order) {
        return false;
    }


    private double calculateTimeForOrder(WorkOrder order) {
        return 0.0;
    } */

    // get the number of parts used for the product
    private int getPartsUsed(WorkOrderProduct product) {
        return 0;
    }
}
