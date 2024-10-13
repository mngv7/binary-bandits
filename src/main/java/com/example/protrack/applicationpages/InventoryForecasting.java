package com.example.protrack.applicationpages;

import java.util.HashMap;

public class InventoryForecasting {

    private final int safetyStock = 500;

    public void calculateAverageDailyUsage()
    {
        DashboardController dashboardController = new DashboardController();

        HashMap<Integer, Integer> sumOfParts = dashboardController.getSumOfParts();
        HashMap<Integer, Integer> dailyAveragePartUsage = new HashMap<>(); // PartID, Daily usage

        for (HashMap.Entry<Integer, Integer> entry : sumOfParts.entrySet()) {
            int partsId = entry.getKey();
            int monthlyUsage = entry.getValue();
            int dailyUsage = monthlyUsage/30;
            dailyAveragePartUsage.put(partsId, dailyUsage);
        }
    }

    HashMap<Integer, Integer> reorderPoints = new HashMap<>();

    public void calculateReorderPoint() {
        // For each Part in parts
            // Get lead time
            // Get daily usage
            // Get safety stock
            // Calculate reorder point and put into HashMap.
    }
}
