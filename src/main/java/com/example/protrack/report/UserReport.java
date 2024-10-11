package com.example.protrack.report;

import com.example.protrack.utility.LoggedInUserSingleton;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAO;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import com.example.protrack.workorderproducts.WorkOrderProductsDAO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserReport implements Report {

    private final WorkOrdersDAO workOrdersDAO;
    private final WorkOrderProductsDAO workOrderProductsDAO;
    private final int loggedInUserId;

    public UserReport(WorkOrdersDAO workOrdersDAO, WorkOrderProductsDAO workOrderProductsDAO) {
        this.workOrdersDAO = workOrdersDAO;
        this.workOrderProductsDAO = workOrderProductsDAO;
        this.loggedInUserId = LoggedInUserSingleton.getInstance().getEmployeeId();
    }

    @Override
    public Map<Double, Double> forecastWorkOrderChartValues() {
        Map<Double, Double> forecastData = new HashMap<>();
        List<WorkOrder> userWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(loggedInUserId);

        for (WorkOrder order : userWorkOrders) {
            double forecastRate = estimateProduction(order);
            forecastData.put((double) order.getOrderDate().toEpochSecond(ZoneOffset.of("+10:00")), forecastRate); // Use epoch seconds
        }
        return forecastData;
    }

    @Override
    public Map<Double, Double> calculateOrdersExpectedCycleTimeChartValues() {
        Map<Double, Double> expectedCycleTimes = new HashMap<>();
        List<WorkOrder> userWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(loggedInUserId);

        for (WorkOrder order : userWorkOrders) {
            double expectedTime = calculateExpectedCycleTime(order);
            expectedCycleTimes.put((double) order.getOrderDate().toEpochSecond(ZoneOffset.of("+10:00")), expectedTime); // Use epoch seconds
        }
        return expectedCycleTimes;
    }

    @Override
    public Map<Double, Double> calculateOrdersActualCycleTimeChartValues() {
        Map<Double, Double> actualCycleTimes = new HashMap<>();
        List<WorkOrder> userWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(loggedInUserId);

        for (WorkOrder order : userWorkOrders) {
            double actualTime = calculateActualCycleTime(order);
            if (order.getStatus().equals("Completed")) {
                actualCycleTimes.put((double) order.getDeliveryDate().toEpochSecond(ZoneOffset.of("+10:00")), actualTime); // Use epoch seconds
            }
        }
        return actualCycleTimes;
    }

    @Override
    public Map<Double, Double> calculateThroughputChartValues() {
        Map<Double, Double> throughputData = new HashMap<>();
        List<WorkOrder> userWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(loggedInUserId);

        for (WorkOrder order : userWorkOrders) {
            double throughput = calculateThroughput(order);
            throughputData.put((double) order.getOrderDate().toEpochSecond(ZoneOffset.of("+10:00")), throughput); // Use epoch seconds
        }
        return throughputData;
    }

    @Override
    public Double calculateOnScheduleRate() {
        List<WorkOrder> userWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(loggedInUserId);
        int onScheduleOrders = 0;
        int totalOrders = userWorkOrders.size();

        for (WorkOrder order : userWorkOrders) {
            if (isOnSchedule(order)) {
                onScheduleOrders++;
            }
        }

        return (totalOrders > 0) ? (double) onScheduleOrders / totalOrders * 100 : 0.0;
    }

    @Override
    public Map<String, Integer> calculateTotalOrdersByStatus() {
        List<WorkOrder> userWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(loggedInUserId);
        Map<String, Integer> orderStatusCount = new HashMap<>();

        for (WorkOrder order : userWorkOrders) {
            String status = order.getStatus();
            orderStatusCount.put(status, orderStatusCount.getOrDefault(status, 0) + 1);
        }

        return orderStatusCount;
    }

    @Override
    public Integer calculateTotalOrders() {
        List<WorkOrder> userWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(loggedInUserId);
        return userWorkOrders.size();
    }

    @Override
    public Integer calculateTotalProductsProduced() {
        List<WorkOrder> userWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(loggedInUserId);
        int totalProducts = 0;

        for (WorkOrder order : userWorkOrders) {
            List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(order.getWorkOrderId());
            for (WorkOrderProduct product : products) {
                totalProducts += product.getQuantity();
            }
        }

        return totalProducts;
    }

    @Override
    public Integer calculateTotalPartsUsed() {
        List<WorkOrder> userWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(loggedInUserId);
        int totalParts = 0;

        for (WorkOrder order : userWorkOrders) {
            List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(order.getWorkOrderId());
            for (WorkOrderProduct product : products) {
                totalParts += getPartsUsed(product);
            }
        }

        return totalParts;
    }

    @Override
    public Double calculateTotalProductionCost() {
        List<WorkOrder> userWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(loggedInUserId);
        double totalCost = 0.0;

        for (WorkOrder order : userWorkOrders) {
            List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(order.getWorkOrderId());
            for (WorkOrderProduct product : products) {
                totalCost += product.getTotal(); // Assuming total is already calculated in WorkOrderProduct
            }
        }

        return totalCost;
    }

    private double calculateExpectedCycleTime(WorkOrder order) {
        List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(order.getWorkOrderId());
        double totalExpectedTime = 0;

        for (WorkOrderProduct product : products) {
            int quantity = product.getQuantity();
            totalExpectedTime += (quantity * 10.0) / 60.0; // 10 minutes per part converted to hours
        }

        return totalExpectedTime; // Total expected time in hours
    }

    private double calculateActualCycleTime(WorkOrder order) {
        if (!"Completed".equals(order.getStatus())) {
            return 0.0; // Not completed orders have no actual cycle time
        }

        LocalDateTime completionTime = order.getDeliveryDate(); // Assuming deliveryDate is when the order is completed
        LocalDateTime orderDate = order.getOrderDate();
        Duration duration = Duration.between(orderDate, completionTime);
        return duration.toHours(); // Returning hours; change as necessary
    }

    private boolean isOnSchedule(WorkOrder order) {
        return order.getDeliveryDate().isAfter(LocalDateTime.now()) && order.getStatus().equals("In Progress");
    }

    private int getPartsUsed(WorkOrderProduct product) {
        return product.getQuantity(); // Assuming this returns the quantity of parts used
    }

    private double estimateProduction(WorkOrder order) {
        List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(order.getWorkOrderId());
        double totalProduced = 0;

        for (WorkOrderProduct product : products) {
            int quantity = product.getQuantity(); // Total quantity for this product
            double productionRateForProduct = (quantity * 10.0) / 60.0; // Convert minutes to hours
            totalProduced += productionRateForProduct; // Sum of production rates per hour for each product
        }

        return totalProduced; // Total production rate in hours
    }

    private double calculateThroughput(WorkOrder order) {
        if (!"Completed".equals(order.getStatus())) {
            return 0.0; // Only completed orders contribute to throughput
        }
        return calculateTotalProductsProduced(); // Total produced units, could be optimized
    }
}
