package com.example.protrack.applicationpages;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.products.BillOfMaterialsDAO;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import com.example.protrack.workorderproducts.WorkOrderProductsDAOImplementation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public class DashboardController {
    @FXML
    public ComboBox<String> monthComboBox;

    @FXML
    public VBox partsAndQuantity;

    @FXML
    public GridPane inventoryUsageGrid;

    @FXML
    public VBox partAndAmountGroup;

    @FXML
    public VBox partName;

    @FXML
    public VBox partUsage;

    HashMap<Integer, Integer> sumOfParts = new HashMap<>();

    public void initialize() {
        setLastThreeMonths();
        initializeHashMap();
        loadMonthlyReport();
    }

    private void setLastThreeMonths() {
        // Get the current month
        YearMonth currentMonth = YearMonth.now();

        // List to store the last 3 months
        List<String> lastThreeMonths = new ArrayList<>();

        // Add current month and the previous 2 months
        for (int i = 0; i < 6; i++) {
            YearMonth month = currentMonth.minusMonths(i);
            String monthName = month.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            lastThreeMonths.add(monthName); // Add the month name
        }

        monthComboBox.getItems().clear();

        // Add the last 3 months to the ComboBox
        monthComboBox.setItems(FXCollections.observableArrayList(lastThreeMonths));

        // Set the default selected item to the current month
        String currentMonthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        monthComboBox.setValue(currentMonthName);  // Set the current month as default
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
            createPDF(); // Pass the sumOfParts HashMap to createPDF
        }
    }

    private void populateHashMap(Integer month) {
        List<ProductionUser> productionUsers = new UsersDAO().getProductionUsers();
        List<Customer> customers = new CustomerDAOImplementation().getAllCustomers();

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

        inventoryUsageGrid.getChildren().clear();

        Label partNameLabel = new Label("Part Name");
        partNameLabel.getStyleClass().add("subheading2"); // Add style class
        inventoryUsageGrid.add(partNameLabel, 0, 0);

        Label quantityUsedLabel = new Label("Quantity Used");
        quantityUsedLabel.getStyleClass().add("subheading2"); // Add style class
        inventoryUsageGrid.add(quantityUsedLabel, 1, 0);

        Label costLabel = new Label("Cost");
        costLabel.getStyleClass().add("subheading2"); // Add style class
        inventoryUsageGrid.add(costLabel, 2, 0);

        int columnIndex = 0;
        int rowIndex = 1;

        PartsDAO partsDAO = new PartsDAO();

        for (HashMap.Entry<Integer, Integer> entry : sumOfParts.entrySet()) {
            int partsId = entry.getKey();
            int totalAmount = entry.getValue();
            double totalCost = partsDAO.getPartById(partsId).getCost() * totalAmount;
            BigDecimal roundedTotalCost = new BigDecimal(totalCost).setScale(2, RoundingMode.HALF_UP);
            totalCost = roundedTotalCost.doubleValue();

            if (totalAmount > 0) {
                inventoryUsageGrid.add(new Label(partsDAO.getPartById(partsId).getName()), columnIndex, rowIndex);
                inventoryUsageGrid.add(new Label(Integer.toString(totalAmount)), columnIndex + 1, rowIndex);
                inventoryUsageGrid.add(new Label("$" + totalCost), columnIndex + 2, rowIndex);

                rowIndex++;
                if (rowIndex > 6) { // Number of rows before changing columns.
                    rowIndex = 1;
                    columnIndex += 3; // Skip to the next set of columns for icons and labels
                }
            }
        }
        initializeHashMap();
    }

    public static void createPDF() {
        String userHome = System.getProperty("user.home");
        String downloadsPath = userHome + "/Downloads/PartsForecast.pdf";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Set the font and size for the heading
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 750); // Position the text
                contentStream.showText("Parts Forecast"); // Set the text to display
                contentStream.endText();
            }

            // Save the document to the specified path
            document.save(downloadsPath);
            System.out.println("PDF created: " + downloadsPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
