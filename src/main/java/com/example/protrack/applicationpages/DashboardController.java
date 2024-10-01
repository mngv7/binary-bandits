package com.example.protrack.applicationpages;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAO;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.products.BillOfMaterialsDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import com.example.protrack.workorderproducts.WorkOrderProductsDAOImplementation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import java.util.Map;

import java.io.IOException;
import java.time.Month;
import java.util.HashMap;
import java.util.List;


public class DashboardController {
    @FXML
    public ComboBox<String> monthComboBox;

    @FXML
    public VBox partsAndQuantity;

    HashMap<Integer, Integer> sumOfParts = new HashMap<>();

    public void initialize() {
        initializeHashMap();
    }

    private void initializeHashMap() {
        PartsDAO partsDAO = new PartsDAO();

        for (Parts part : partsDAO.getAllParts()) {
            sumOfParts.put(part.getPartsId(), 0);
        }
    }

    public void loadMonthlyReport() {
        if (monthComboBox.getValue() != null) {
            populateHashMap((Month.valueOf(monthComboBox.getValue().toUpperCase())).getValue());
            displayParts();
        }
    }

    public void downloadPDF() {
        if (monthComboBox.getValue() != null) {
            populateHashMap((Month.valueOf(monthComboBox.getValue().toUpperCase())).getValue());
            displayParts();
            createPDF(sumOfParts); // Pass the sumOfParts HashMap to createPDF
        }
    }

    private void populateHashMap(Integer month) {
        List<ProductionUser> productionUsers = new UsersDAO().getProductionUsers();
        List<Customer> customers = new CustomerDAO().getAllCustomers();

        // Initialize DAOs
        WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);
        WorkOrderProductsDAOImplementation productOrders = new WorkOrderProductsDAOImplementation();
        BillOfMaterialsDAO bomDAO = new BillOfMaterialsDAO();

        // Loop through all work orders for the given month
        for (WorkOrder workOrder : workOrdersDAO.getWorkOrderByMonth(month)) {
            // Loop through all products in the current work order
            for (WorkOrderProduct productOrder : productOrders.getWorkOrderProductsByWorkOrderId(workOrder.getWorkOrderId())) {
                int productId = productOrder.getProductId();
                int quantity = productOrder.getQuantity();

                HashMap<Integer, Integer> productIdParts = bomDAO.getPartsAndAmountsForProduct(productId);

                // Multiply each part's required amount by the product order's quantity
                for (HashMap.Entry<Integer, Integer> entry : productIdParts.entrySet()) {
                    int partsId = entry.getKey();
                    int requiredAmount = entry.getValue();
                    int totalRequiredAmount = requiredAmount * quantity;

                    // Add the total amount to the sum HashMap, updating it if the part already exists
                    sumOfParts.put(partsId, sumOfParts.getOrDefault(partsId, 0) + totalRequiredAmount);
                }
            }
        }
    }

    public void displayParts() {
        PartsDAO partsDAO = new PartsDAO();
        partsAndQuantity.getChildren().clear();

        // Loop through the sumOfParts HashMap
        for (HashMap.Entry<Integer, Integer> entry : sumOfParts.entrySet()) {
            int partsId = entry.getKey();
            int totalRequiredAmount = entry.getValue();

            // Only print if the value is not zero
            if (totalRequiredAmount > 0) {
                Label label = new Label();
                label.setText(partsDAO.getPartById(partsId).getName() + " " + totalRequiredAmount);
                partsAndQuantity.getChildren().add(label);
            }
        }
        initializeHashMap();
    }

    public static void createPDF(HashMap<Integer, Integer> sumOfParts) {
        String userHome = System.getProperty("user.home");
        String downloadsPath = userHome + "/Downloads/InvoiceFromProTrack.pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("Invoice from ProTrack");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Part Name          Quantity");
                contentStream.endText();

                contentStream.setFont(PDType1Font.HELVETICA, 12);
                float yPosition = 680; // Starting position for the parts list

                PartsDAO partsDAO = new PartsDAO();

                for (Map.Entry<Integer, Integer> entry : sumOfParts.entrySet()) {
                    int partsId = entry.getKey();
                    int totalRequiredAmount = entry.getValue();

                    if (totalRequiredAmount > 0) {
                        String partName = partsDAO.getPartById(partsId).getName();

                        // Debugging: Print part info
                        System.out.println("Part: " + partName + ", Quantity: " + totalRequiredAmount);

                        String line = String.format("%-20s %d", partName, totalRequiredAmount);
                        contentStream.beginText();
                        contentStream.newLineAtOffset(100, yPosition);
                        contentStream.showText(line);
                        contentStream.endText();
                        yPosition -= 20; // Move down for the next line
                    }
                }
            }

            document.save(downloadsPath);
            System.out.println("Invoice PDF created: " + downloadsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
