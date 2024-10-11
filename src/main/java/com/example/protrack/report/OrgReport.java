package com.example.protrack.report;

import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAO;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import com.example.protrack.workorderproducts.WorkOrderProductsDAO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgReport implements Report {

    private final WorkOrdersDAO workOrdersDAO;
    private final WorkOrderProductsDAO workOrderProductsDAO;

    public OrgReport(WorkOrdersDAO workOrdersDAO, WorkOrderProductsDAO workOrderProductsDAO) {
        this.workOrdersDAO = workOrdersDAO;
        this.workOrderProductsDAO = workOrderProductsDAO;
    }

    private double normalizeToDays(LocalDateTime orderDate, LocalDateTime deliveryDate) {
        return (double) Duration.between(orderDate, deliveryDate).toDays();
    }

    @Override
    public Map<Double, Double> forecastWorkOrderChartValues() {
        Map<Double, Double> forecastData = new HashMap<>();
        List<WorkOrder> workOrders = workOrdersDAO.getAllWorkOrders();
        System.out.println(workOrders);

        // Populate forecastData with normalized values
        for (WorkOrder order : workOrders) {
            double forecastedDate = estimateProduction(order);
            double normalizedOrderDate = normalizeToDays(order.getOrderDate(), order.getDeliveryDate());
            if (forecastData.containsKey(normalizedOrderDate)) {
                forecastData.compute(normalizedOrderDate, (k, value) -> forecastedDate + value);
            } else {
                forecastData.put(normalizedOrderDate, forecastedDate);
            }

            System.out.println(forecastData);
        }
        return forecastData;
    }

    @Override
    public Map<Double, Double> calculateOrdersExpectedCycleTimeChartValues() {
        Map<Double, Double> expectedCycleTimes = new HashMap<>();
        List<WorkOrder> workOrders = workOrdersDAO.getAllWorkOrders();

        // Populate expectedCycleTimes with normalized values
        for (WorkOrder order : workOrders) {
            double expectedTime = calculateExpectedCycleTime(order);
            double normalizedOrderDate = normalizeToDays(order.getOrderDate(), order.getDeliveryDate());
            expectedCycleTimes.put(normalizedOrderDate, expectedTime);
        }
        return expectedCycleTimes;
    }

    @Override
    public Map<Double, Double> calculateOrdersActualCycleTimeChartValues() {
        Map<Double, Double> actualCycleTimes = new HashMap<>();
        List<WorkOrder> workOrders = workOrdersDAO.getAllWorkOrders();

        // Populate actualCycleTimes with normalized values
        for (WorkOrder order : workOrders) {
            double actualTime = calculateActualCycleTime(order);
            if (order.getStatus().equals("Completed")) {
                double normalizedDeliveryDate = normalizeToDays(order.getDeliveryDate(), order.getOrderDate());
                actualCycleTimes.put(normalizedDeliveryDate, actualTime);
            }
        }
        return actualCycleTimes;
    }

    @Override
    public Map<Double, Double> calculateThroughputChartValues() {
        Map<Double, Double> throughputData = new HashMap<>();
        List<WorkOrder> workOrders = workOrdersDAO.getAllWorkOrders();

        // Populate throughputData with normalized values
        for (WorkOrder order : workOrders) {
            double throughput = calculateThroughput(order);
            double normalizedOrderDate = normalizeToDays(order.getOrderDate(), order.getDeliveryDate());
            throughputData.put(normalizedOrderDate, throughput);
        }
        return throughputData;
    }

    @Override
    public Double calculateOnScheduleRate() {
        List<WorkOrder> allWorkOrders = workOrdersDAO.getAllWorkOrders();
        int onScheduleOrders = 0;
        int totalOrders = allWorkOrders.size();

        for (WorkOrder order : allWorkOrders) {
            if (isOnSchedule(order)) {
                onScheduleOrders++;
            }
        }

        return (totalOrders > 0) ? (double) onScheduleOrders / totalOrders * 100 : 0.0;
    }

    @Override
    public Map<String, Integer> calculateTotalOrdersByStatus() {
        List<WorkOrder> allWorkOrders = workOrdersDAO.getAllWorkOrders();
        Map<String, Integer> orderStatusCount = new HashMap<>();

        for (WorkOrder order : allWorkOrders) {
            String status = order.getStatus();
            orderStatusCount.put(status, orderStatusCount.getOrDefault(status, 0) + 1);
        }

        return orderStatusCount;
    }

    @Override
    public Integer calculateTotalOrders() {
        List<WorkOrder> allWorkOrders = workOrdersDAO.getAllWorkOrders();
        return allWorkOrders.size();
    }

    @Override
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

    @Override
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

    @Override
    public Double calculateTotalProductionCost() {
        List<WorkOrderProduct> allWorkOrderProducts = workOrderProductsDAO.getAllWorkOrderProducts();
        double totalCost = 0.0;

        for (WorkOrderProduct product : allWorkOrderProducts) {
            totalCost += product.getTotal(); // Assuming total is already calculated in WorkOrderProduct
        }

        return totalCost;
    }

    private double calculateExpectedCycleTime(WorkOrder order) {
        List<WorkOrderProduct> products = workOrderProductsDAO.getWorkOrderProductsByWorkOrderId(order.getWorkOrderId());
        double totalExpectedTime = 0;

        for (WorkOrderProduct product : products) {
            int quantity = product.getQuantity();
            // Assuming expected time for each product is calculated based on its parts
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
