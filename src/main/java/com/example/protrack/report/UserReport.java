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

/**
 * UserReport generates reports specific to the logged-in user regarding work orders
 * and associated production metrics. It implements the Report interface and uses
 * DAOs to fetch necessary data.
 */
public class UserReport implements Report {

    private final WorkOrdersDAO workOrdersDAO;
    private final WorkOrderProductsDAO workOrderProductsDAO;
    private final int loggedInUserId;

    /**
     * Constructs a UserReport instance, initializing the DAO instances and
     * retrieving the logged-in user's ID.
     *
     * @param workOrdersDAO       Data Access Object for work orders.
     * @param workOrderProductsDAO Data Access Object for work order products.
     */
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
            forecastData.put((double) order.getOrderDate().toEpochSecond(ZoneOffset.of("+10:00")), forecastRate);
        }
        return forecastData;
    }

    @Override
    public Map<Double, Double> calculateOrdersExpectedCycleTimeChartValues() {
        Map<Double, Double> expectedCycleTimes = new HashMap<>();
        List<WorkOrder> userWorkOrders = workOrdersDAO.getWorkOrdersByEmployeeId(loggedInUserId);

        for (WorkOrder order : userWorkOrders) {
            double expectedTime = calculateExpectedCycleTime(order);
            expectedCycleTimes.put((double) order.getOrderDate().toEpochSecond(ZoneOffset.of("+10:00")), expectedTime);
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
                actualCycleTimes.put((double) order.getDeliveryDate().toEpochSecond(ZoneOffset.of("+10:00")), actualTime);
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
            throughputData.put((double) order.getOrderDate().toEpochSecond(ZoneOffset.of("+10:00")), throughput);
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
                totalCost += product.getTotal();
            }
        }

        return totalCost;
    }

    /**
     * Calculates the expected cycle time for a given work order based on its products.
     *
     * @param order the work order for which to calculate expected cycle time.
     * @return the expected cycle time in hours.
     */
    private double calculateExpectedCycleTime(WorkOrder order) {
        List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(order.getWorkOrderId());
        double totalExpectedTime = 0;

        for (WorkOrderProduct product : products) {
            int quantity = product.getQuantity();
            totalExpectedTime += (quantity * 10.0) / 60.0; // 10 minutes per part converted to hours
        }

        return totalExpectedTime; // Total expected time in hours
    }

    /**
     * Calculates the actual cycle time for a given work order.
     *
     * @param order the work order for which to calculate actual cycle time.
     * @return the actual cycle time in hours, or 0.0 if not completed.
     */
    private double calculateActualCycleTime(WorkOrder order) {
        if (!"Completed".equals(order.getStatus())) {
            return 0.0; // Not completed orders have no actual cycle time
        }

        LocalDateTime completionTime = order.getDeliveryDate();
        LocalDateTime orderDate = order.getOrderDate();
        Duration duration = Duration.between(orderDate, completionTime);
        return duration.toHours();
    }

    /**
     * Determines whether a work order is on schedule based on its delivery date and status.
     *
     * @param order the work order to check.
     * @return true if the order is on schedule, false otherwise.
     */
    private boolean isOnSchedule(WorkOrder order) {
        return order.getDeliveryDate().isAfter(LocalDateTime.now()) && order.getStatus().equals("In Progress");
    }

    /**
     * Returns the number of parts used for a given work order product.
     *
     * @param product the work order product to check.
     * @return the quantity of parts used.
     */
    private int getPartsUsed(WorkOrderProduct product) {
        return product.getQuantity();
    }

    /**
     * Estimates the production for a given work order based on its products.
     *
     * @param order the work order for which to estimate production.
     * @return the estimated production rate in hours.
     */
    private double estimateProduction(WorkOrder order) {
        List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(order.getWorkOrderId());
        double totalProduced = 0;

        for (WorkOrderProduct product : products) {
            int quantity = product.getQuantity();
            totalProduced += quantity; // Add to the total produced
        }

        return totalProduced / 10; // Return an estimated rate based on some logic
    }

    /**
     * Calculates the throughput for a given work order.
     *
     * @param order the work order for which to calculate throughput.
     * @return the throughput rate.
     */
    private double calculateThroughput(WorkOrder order) {
        List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(order.getWorkOrderId());
        int totalQuantity = 0;

        for (WorkOrderProduct product : products) {
            totalQuantity += product.getQuantity(); // Sum the quantities
        }

        return totalQuantity; // Return total throughput
    }
}
