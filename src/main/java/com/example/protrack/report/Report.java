package com.example.protrack.report;

import java.util.Map;

public interface Report {

    // Maps each workOrder to its forecasted completion time and actual completion time
    // production & expected production             line chart
    Map<Double, Double> forecastWorkOrderChartValues();

    // to be used for cumulative area chart or burndown, expected cycle time
    Map<Double, Double> calculateOrdersExpectedCycleTimeChartValues();

    Map<Double, Double> calculateOrdersActualCycleTimeChartValues();

    // Throughput line chart, completed production over time
    Map<Double, Double> calculateThroughputChartValues();

    // Calculate on-schedule rate instead of perfect rate
    Double calculateOnScheduleRate();

    Map<String, Integer> calculateTotalOrdersByStatus();

    Integer calculateTotalOrders();

    Integer calculateTotalProductsProduced();

    Integer calculateTotalPartsUsed();

    Double calculateTotalProductionCost();
}
