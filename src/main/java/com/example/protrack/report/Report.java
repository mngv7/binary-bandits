package com.example.protrack.report;

import java.util.Map;

/**
 * The Report interface defines methods for generating various reports
 * related to work orders and production metrics.
 */
public interface Report {

    /**
     * Maps each work order to its forecasted completion time and actual completion time.
     * This data can be used to create a line chart comparing production against expected production.
     *
     * @return a map where keys represent normalized time values, and values represent forecasted and
     * actual completion times.
     */
    Map<Double, Double> forecastWorkOrderChartValues();

    /**
     * Calculates the expected cycle times for work orders.
     * This data can be used for cumulative area charts or burndown charts.
     *
     * @return a map where keys represent normalized time values, and values represent expected cycle
     * times for work orders.
     */
    Map<Double, Double> calculateOrdersExpectedCycleTimeChartValues();

    /**
     * Calculates the actual cycle times for work orders.
     *
     * @return a map where keys represent normalized time values, and values represent
     *         actual cycle times for work orders.
     */
    Map<Double, Double> calculateOrdersActualCycleTimeChartValues();

    /**
     * Calculates the throughput of completed production over time.
     * This data can be represented in a line chart.
     *
     * @return a map where keys represent normalized time values, and values represent
     *         throughput values for work orders.
     */
    Map<Double, Double> calculateThroughputChartValues();

    /**
     * Calculates the on-schedule rate of work orders, indicating the percentage
     * of orders that were completed on or before their due dates.
     *
     * @return the on-schedule rate as a percentage of completed work orders.
     */
    Double calculateOnScheduleRate();

    /**
     * Calculates the total number of work orders by their status.
     *
     * @return a map where keys represent order statuses and values represent the count of work orders for each status.
     */
    Map<String, Integer> calculateTotalOrdersByStatus();

    /**
     * Calculates the total number of work orders.
     *
     * @return the total number of work orders.
     */
    Integer calculateTotalOrders();

    /**
     * Calculates the total number of products produced across all work orders.
     *
     * @return the total number of products produced.
     */
    Integer calculateTotalProductsProduced();

    /**
     * Calculates the total number of parts used across all work orders.
     *
     * @return the total number of parts used.
     */
    Integer calculateTotalPartsUsed();

    /**
     * Calculates the total production cost for all work orders.
     *
     * @return the total production cost as a double value.
     */
    Double calculateTotalProductionCost();
}
