package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.customer.EditCustomerController;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.products.BillOfMaterialsDAO;
import com.example.protrack.report.GenerateReportController;
import com.example.protrack.report.OrgReport;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAO;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import com.example.protrack.workorderproducts.WorkOrderProductsDAOImplementation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

public class DashboardController {
    @FXML
    public ComboBox<String> monthComboBox;

    @FXML
    public Label generateReport;

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

    @FXML
    public HBox graphContainer; // HBox to hold the graphs

    @FXML
    public GridPane reorderPointGrid;

    @FXML
    public VBox partAndReorderGroup;

    @FXML
    public VBox PartId;

    @FXML
    public VBox reorderPoint;

    // <PartID, Usage>
    private final HashMap<Integer, Integer> sumOfParts = new HashMap<>();
    OrgReport orgReport;
    List<Customer> customers;
    List<ProductionUser> productionUsers;

    @FXML
    private LineChart<String, Number> lineChart;

    public HashMap<Integer, Integer> getSumOfParts() {
        return sumOfParts;
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

    public void initialize() {
        CustomerDAOImplementation customerDAO = new CustomerDAOImplementation();
        customers = customerDAO.getAllCustomers();

        UsersDAO usersDAO = new UsersDAO();
        productionUsers = usersDAO.getProductionUsers();

        setLastThreeMonths();
        initializeHashMap();
        loadMonthlyReport();
        displayGraphs();
        updatePartUsageLineChart(sortMonthlyUsage(loadPartUsageStatistics()));
    }

    private HashMap<String, Integer> sortMonthlyUsage(HashMap<String, Integer> monthlyTotalUsage) {
        // Create a list from the entry set of the HashMap
        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(monthlyTotalUsage.entrySet());

        Map<String, Integer> monthOrder = new HashMap<>();
        monthOrder.put("January", 1);
        monthOrder.put("February", 2);
        monthOrder.put("March", 3);
        monthOrder.put("April", 4);
        monthOrder.put("May", 5);
        monthOrder.put("June", 6);
        monthOrder.put("July", 7);
        monthOrder.put("August", 8);
        monthOrder.put("September", 9);
        monthOrder.put("October", 10);
        monthOrder.put("November", 11);
        monthOrder.put("December", 12);

        // Sort the entry list by month order
        entryList.sort(Comparator.comparingInt(entry -> monthOrder.get(entry.getKey())));

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private HashMap<String, Integer> loadPartUsageStatistics() {
        // Get the current month
        YearMonth currentMonth = YearMonth.now();

        // Create a map to store total part usage for each month
        HashMap<String, Integer> monthlyTotalUsage = new HashMap<>();

        // Loop through the last 6 months
        for (int i = 0; i < 6; i++) {
            YearMonth month = currentMonth.minusMonths(i);
            String monthName = month.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            int monthValue = month.getMonthValue();

            // Reset the sumOfParts for each month
            initializeHashMap();

            populateHashMap(monthValue);

            // Calculate the total usage for the current month
            int totalUsageForMonth = sumOfParts.values().stream().mapToInt(Integer::intValue).sum();

            // Store the total usage for the current month
            monthlyTotalUsage.put(monthName, totalUsageForMonth);
        }

        return monthlyTotalUsage;
    }

    private void updatePartUsageLineChart(HashMap<String, Integer> monthlyTotalUsage) {
        lineChart.getData().clear();

        // Create a new series for the line chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Populate the series with the monthly data
        for (Map.Entry<String, Integer> entry : monthlyTotalUsage.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Add the series to the line chart
        lineChart.getData().add(series);
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

    public void displayGraphs() {
        WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);
        WorkOrderProductsDAOImplementation workOrderProductsDAO = new WorkOrderProductsDAOImplementation();

        orgReport = new OrgReport(workOrdersDAO, workOrderProductsDAO);

        graphContainer.getChildren().clear(); // Clear previous graphs

        // Create Work Order Forecasting Line Chart
        ScatterChart<Number, Number> forecastingChart = createWorkOrderForecastingChart();

        // Create Pie Chart for On Schedule vs Behind Schedule
        PieChart schedulePieChart = createSchedulePieChart();

        // Create Bar Chart for Total Parts Used, Total Products Produced, Total Cost
        BarChart<String, Number> metricsBarChart = createMetricsBarChart();

        // Add charts to the graph container
        graphContainer.getChildren().addAll(forecastingChart, schedulePieChart, metricsBarChart);
    }

    private ScatterChart<Number, Number> createWorkOrderForecastingChart() {

        Map<Double, Double> forecastData = orgReport.forecastWorkOrderChartValues();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Work Orders");

        NumberAxis xAxis = new NumberAxis();
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(30);
        xAxis.setLabel("Expected Completion (days)");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);

        double maxValue = forecastData.values().stream()
                .max(Double::compareTo)
                .orElse(30.0); // Defaults to 30.0 if forecastData is empty

        double roundedMaxValue = Math.ceil((maxValue + 1) / 5) * 5;
        yAxis.setUpperBound(roundedMaxValue);

        yAxis.setTickUnit(10);
        yAxis.setLabel("Expected Production Time (hours)");

        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Work Order Forecasting");
        scatterChart.setPrefWidth(600);

        for (Map.Entry<Double, Double> entry : forecastData.entrySet()) {
            if (entry.getKey() >= 0 && entry.getKey() <= 30) {

                // Create a Circle with size based on the value
                Circle circle = new Circle(Math.max(Math.min(entry.getValue(), 50) / 2, 5));
                circle.setFill(getColorBasedOnCircleSizeAndX(entry.getKey(), entry.getValue()));

                // Create a data point with the custom Circle node
                XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(entry.getKey(), entry.getValue());
                series.getData().add(dataPoint);
                dataPoint.setNode(circle); // Use the custom node
            }
        }

        scatterChart.getData().add(series);

        return scatterChart;
    }

    private Color getColorBasedOnCircleSizeAndX(double xValue, double circleSize) {
        // Clamp both values to the range [0, 30], size
        xValue = Math.max(0, Math.min(xValue, 30));
        circleSize = Math.max(0, Math.min(circleSize, 25));

        // Normalize both values to the range [0, 1]
        double normalizedX = 1 - (xValue / 30.0); // Closer to 0 means more intense color
        double normalizedSize = 1 - (1 - (circleSize / 25.0)); // Larger size means more intense color

        double combinedValue = ((normalizedX * 1.4) + (normalizedSize / 1.4)) / 2; // Weighted in favour of expected completion date as is more critical

        // Calculate the RGB components based on the combined normalized value
        int red = (int) (255 * combinedValue); // Increase red as the combined value increases
        int green = (int) (255 * (1 - combinedValue)); // Increase green as the combined value decreases
        int blue = 0; // Keep blue at 0

        return Color.rgb(red, green, blue); // Create the color
    }

    private PieChart createSchedulePieChart() {
        // Implementation of pie chart creation
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Order Status Distribution");
        pieChart.minWidth(300);

        Map<String, Integer> orderStatusCounts = orgReport.calculateTotalOrdersByStatus();
        for (Map.Entry<String, Integer> entry : orderStatusCounts.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        return pieChart;
    }

    private BarChart<String, Number> createMetricsBarChart() {
        // Implementation of metrics bar chart creation
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Inventory Usage");

        // Prepare bar chart data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Metrics");

        // Add data for Total Parts Used, Total Products Produced, and Total Orders
        series.getData().add(new XYChart.Data<>("Total Parts Used", orgReport.calculateTotalPartsUsed()));
        series.getData().add(new XYChart.Data<>("Total Products Produced", orgReport.calculateTotalProductsProduced()));

        // Add the series to the bar chart
        barChart.getData().add(series);

        return barChart;
    }

    public void generateReportPopup() {
        try {
            // Load the FXML file for the edit customer dialog
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/generate_report.fxml"));
            Parent root = fxmlLoader.load();

            GenerateReportController controller = fxmlLoader.getController();

            WorkOrdersDAOImplementation workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);
            WorkOrderProductsDAOImplementation workOrderProductsDAO = new WorkOrderProductsDAOImplementation();

            orgReport = new OrgReport(workOrdersDAO, workOrderProductsDAO);
            controller.setOrgReport(orgReport);

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Generate Report");
            popupStage.setScene(new Scene(root));

            Bounds rootBounds = generateReport.getScene().getRoot().getLayoutBounds();
            popupStage.setY(rootBounds.getCenterY() - 100);
            popupStage.setX(rootBounds.getCenterX());

            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
