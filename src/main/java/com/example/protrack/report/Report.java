package com.example.protrack.report;

import com.example.protrack.workorder.WorkOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Report {

    // Maps each workOrder to its forecasted completion time and actual completion time
    // production & expected production             line chart
    Map<Long, Double> forecastWorkOrderChartValues();

    // to be used for cumulative area chart or burndown, expected cycle time
    Map<Long, Double> calculateOrdersExpectedCycleTimeChartValues();

    Map<Long, Double> calculateOrdersActualCycleTimeChartValues();

    // Throughput line chart, completed production over time
    Map<Long, Double> calculateThroughputChartValues();

    // All below can be a bar chart
    Double calculateDefectRate();

    Double calculatePerfectRate();

    Map<String, Integer> calculateTotalOrdersByStatus();

    Integer calculateTotalOrders();

    Integer calculateTotalProductsProduced();

    Integer calculateTotalPartsUsed();

    Double calculateTotalProductionTime();

    Double calculateTotalProductionCost();
}
