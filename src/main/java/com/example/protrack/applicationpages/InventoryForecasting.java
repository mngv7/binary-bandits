package com.example.protrack.applicationpages;

import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.supplier.SupplierDAOImplementation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

/**
 * This class is responsible for forecasting inventory and calculating reorder points for parts
 * based on daily average usage and supplier lead times.
 */
public class InventoryForecasting {

    /** Stores the daily average usage of parts, mapped by part ID. */
    HashMap<Integer, Integer> dailyAveragePartUsage = new HashMap<>(); // PartID, Daily usage

    /**
     * Calculates the average daily usage for each part based on the monthly usage data
     * retrieved from the dashboard and updates the dailyAveragePartUsage map.
     */
    private void calculateAverageDailyUsage() {
        int currentMonth = LocalDate.now().getMonthValue();

        DashboardController dashboardController = new DashboardController();
        dashboardController.populateHashMap(currentMonth);
        HashMap<Integer, Integer> sumOfParts = dashboardController.getSumOfParts();

        for (HashMap.Entry<Integer, Integer> entry : sumOfParts.entrySet()) {
            int partsId = entry.getKey();
            int monthlyUsage = entry.getValue();
            int dailyUsage = monthlyUsage / 30;
            dailyAveragePartUsage.put(partsId, dailyUsage);
        }
    }

    /** Stores the reorder points for parts, mapped by part ID. */
    private final HashMap<Integer, Integer> reorderPoints = new HashMap<>();

    /**
     * Calculates the reorder point for each part based on the supplier's lead time, daily average usage, and safety stock.
     * The reorder points are stored in the reorderPoints map.
     */
    private void calculateReorderPoint() {
        PartsDAO partsDAO = new PartsDAO();
        List<Parts> parts = partsDAO.getAllParts();
        SupplierDAOImplementation supplierDAO = new SupplierDAOImplementation();

        for (Parts part : parts) {
            int partId = part.getPartsId();
            int safetyStock = 500;
            int reorderPoint;

            if (dailyAveragePartUsage.containsKey(partId)) {
                int supplierId = part.getSupplierId();
                reorderPoint = 0;

                if (supplierDAO.getSupplier(supplierId) != null) {
                    double leadTime = supplierDAO.getSupplier(supplierId).getLeadTime();
                    int dailyAverageUsage = dailyAveragePartUsage.get(partId);
                    reorderPoint = (int) Math.round((leadTime * dailyAverageUsage) + safetyStock);
                }

            } else {
                reorderPoint = safetyStock;
            }

            reorderPoints.put(partId, reorderPoint);
        }
    }

    /**
     * Returns the calculated reorder points for all parts.
     * This method invokes the calculation of average daily usage and reorder points before returning the data.
     *
     * @return a HashMap where the key is the part ID, and the value is the reorder point for that part.
     */
    public HashMap<Integer, Integer> getReorderPoints() {
        calculateAverageDailyUsage();
        calculateReorderPoint();
        return reorderPoints;
    }
}
