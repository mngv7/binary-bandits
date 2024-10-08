package com.example.protrack.applicationpages;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.inventoryusage.InventoryUsage;
import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.products.BillOfMaterialsDAO;
import com.example.protrack.report.OrgReport;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import com.example.protrack.workorderproducts.WorkOrderProduct;
import com.example.protrack.workorderproducts.WorkOrderProductsDAOImplementation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

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
    private LineChart<String, Number> lineChart;


    OrgReport orgReport;

    List<Customer> customers;
    List<ProductionUser> productionUsers;

    HashMap<Integer, Integer> sumOfParts = new HashMap<>();

    public void initialize() {
        CustomerDAOImplementation customerDAO = new CustomerDAOImplementation();
        customers = customerDAO.getAllCustomers();

        UsersDAO usersDAO = new UsersDAO();
        productionUsers = usersDAO.getProductionUsers();

        InventoryUsage inventoryUsage = new InventoryUsage();

        setLastThreeMonths();
        sumOfParts = inventoryUsage.initializeHashMap();
        loadMonthlyReport();
        displayGraphs();
        updatePartUsageLineChart(inventoryUsage.sortMonthlyUsage(inventoryUsage.loadPartUsageStatistics()));
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

    public void loadMonthlyReport() {
        if (monthComboBox.getValue() != null) {
            InventoryUsage inventoryUsage = new InventoryUsage();
            sumOfParts = inventoryUsage.populateHashMap((Month.valueOf(monthComboBox.getValue().toUpperCase())).getValue());
            displayParts();
        }
    }

    public void downloadPDF() {
        if (monthComboBox.getValue() != null) {
            InventoryUsage inventoryUsage = new InventoryUsage();
            sumOfParts = inventoryUsage.populateHashMap((Month.valueOf(monthComboBox.getValue().toUpperCase())).getValue());
            displayParts();
            createPDF(); // Pass the sumOfParts HashMap to createPDF
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
        InventoryUsage inventoryUsage = new InventoryUsage();
        inventoryUsage.initializeHashMap();
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
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLowerBound(0);
        xAxis.setTickUnit(1);
        xAxis.setLabel("Expected Completion (days)");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLowerBound(0);
        yAxis.setTickUnit(5);
        yAxis.setLabel("Expected Production Time (hours)");

        ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
        scatterChart.setTitle("Work Order Forecasting");

        Map<Double, Double> forecastData = orgReport.forecastWorkOrderChartValues();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Work Orders: (Colour changes according to no. of products and estimated completion time)");

        for (Map.Entry<Double, Double> entry : forecastData.entrySet()) {
            if (entry.getKey() >= 0 && entry.getKey() <= 30) {
                // Debugging output
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());

                // Create a Circle with size based on the value
                Circle circle = new Circle(entry.getValue() / 2); // Minimum size to avoid very small circles
                circle.setFill(getColorBasedOnCircleSizeAndX(entry.getKey(), entry.getValue()));

                // Create a data point with the custom Circle node
                XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(entry.getKey(), entry.getValue());
                dataPoint.setNode(circle); // Use the custom node

                series.getData().add(dataPoint);
            }
        }

        scatterChart.getData().add(series);
        return scatterChart;
    }

    private Color getColorBasedOnCircleSizeAndX(double xValue, double circleSize) {
        // Clamp both values to the range [0, 30]
        xValue = Math.max(0, Math.min(xValue, 30));
        circleSize = Math.max(0, Math.min(circleSize, 30));

        // Normalize both values to the range [0, 1]
        double normalizedX = 1 - (xValue / 30.0); // Closer to 0 means more intense color
        double normalizedSize = 1 - (circleSize / 30.0); // Larger size means more intense color

        // Combine the normalized values (weighted if necessary)
        double combinedValue = (normalizedX + normalizedSize) / 2; // Average for a balanced effect

        // Calculate the RGB components based on the combined normalized value
        int red = (int) (255 * (1 - combinedValue)); // Increase red as the combined value decreases
        int green = (int) (255 * combinedValue); // Increase green as the combined value increases
        int blue = 0; // Keep blue at 0

        return Color.rgb(red, green, blue); // Create the color
    }




    private PieChart createSchedulePieChart() {
        // Implementation of pie chart creation
        PieChart pieChart = new PieChart();

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
        barChart.setTitle("Production Metrics");

        // Prepare bar chart data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Metrics");

        // Add data for Total Parts Used, Total Products Produced, and Total Orders
        series.getData().add(new XYChart.Data<>("Total Parts Used", orgReport.calculateTotalPartsUsed()));
        series.getData().add(new XYChart.Data<>("Total Products Produced", orgReport.calculateTotalProductsProduced()));
        series.getData().add(new XYChart.Data<>("Total Orders", orgReport.calculateTotalOrders()));

        // Add the series to the bar chart
        barChart.getData().add(series);

        // Color customization for the bars
        for (XYChart.Data<String, Number> data : series.getData()) {
            if (data.getYValue().doubleValue() > 0) {
                // Customize colors based on the metric's name
                if (data.getXValue().equals("Total Parts Used")) {
                    data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            newNode.setStyle("-fx-bar-fill: blue;"); // Set color for Total Parts Used
                        }
                    });
                } else if (data.getXValue().equals("Total Products Produced")) {
                    data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            newNode.setStyle("-fx-bar-fill: green;"); // Set color for Total Products Produced
                        }
                    });
                } else if (data.getXValue().equals("Total Orders")) {
                    data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            newNode.setStyle("-fx-bar-fill: red;"); // Set color for Total Orders
                        }
                    });
                }
            }
        }

        return barChart;
    }

}
