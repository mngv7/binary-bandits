package com.example.protrack.applicationpages;

import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.supplier.SupplierDAOImplementation;

import java.util.HashMap;
import java.util.List;

public class InventoryForecasting {

    HashMap<Integer, Integer> dailyAveragePartUsage = new HashMap<>(); // PartID, Daily usage

    private void calculateAverageDailyUsage()
    {
        DashboardController dashboardController = new DashboardController();

        HashMap<Integer, Integer> sumOfParts = dashboardController.getSumOfParts();

        for (HashMap.Entry<Integer, Integer> entry : sumOfParts.entrySet()) {
            int partsId = entry.getKey();
            int monthlyUsage = entry.getValue();
            int dailyUsage = monthlyUsage/30;
            dailyAveragePartUsage.put(partsId, dailyUsage);
        }
    }

    private HashMap<Integer, Integer> reorderPoints = new HashMap<>();

    private void calculateReorderPoint() {
        PartsDAO partsDAO = new PartsDAO();
        List<Parts> parts = partsDAO.getAllParts();
        SupplierDAOImplementation supplierDAO = new SupplierDAOImplementation();

        for (Parts part : parts) {
            int partId = part.getPartsId();
            int supplierId = part.getSupplierId();
            double leadTime = supplierDAO.getSupplier(supplierId).getLeadTime();
            int dailyAverageUsage = dailyAveragePartUsage.get(partId);
            int safetyStock = 500;
            int reorderPoint = (int) Math.round((leadTime * dailyAverageUsage) + safetyStock);
            reorderPoints.put(partId, reorderPoint);
        }
    }

    public HashMap<Integer, Integer> getReorderPoints() {
        calculateAverageDailyUsage();
        calculateReorderPoint();
        return reorderPoints;
    }
}
