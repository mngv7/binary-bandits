package com.example.protrack.report;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class GenerateReportController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextArea reportOutput; // Text area to display the report

    @FXML
    private Button closePopupButton;

    private OrgReport orgReport;

    // Setter method to allow external initialization
    public void setOrgReport(OrgReport orgReport) {
        this.orgReport = orgReport;
    }

    @FXML
    private void handleGenerateReport() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate != null && endDate != null) {
            generateReport(startDate, endDate);
        } else {
            reportOutput.setText("Please select both start and end dates.");
        }
    }

    private void generateReport(LocalDate startDate, LocalDate endDate) {
        if (orgReport == null) {
            reportOutput.setText("Report data is not available.");
            return;
        }

        // Generate report metrics
        Map<Double, Double> forecastData = orgReport.forecastWorkOrderChartValues();
        Map<Double, Double> expectedCycleTimes = orgReport.calculateOrdersExpectedCycleTimeChartValues();
        Map<Double, Double> actualCycleTimes = orgReport.calculateOrdersActualCycleTimeChartValues();
        Map<Double, Double> throughputData = orgReport.calculateThroughputChartValues();

        // Build the report string
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("**Report Period**: ").append(startDate).append(" to ").append(endDate).append("\n\n");

        reportBuilder.append("### Forecast Data\n");
        if (forecastData.isEmpty()) {
            reportBuilder.append("No forecast data available.\n");
        } else {
            for (Map.Entry<Double, Double> entry : forecastData.entrySet()) {
                reportBuilder.append("- Day ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append(" units forecasted\n");
            }
        }

        reportBuilder.append("\n### Expected Cycle Times (in Days)\n");
        if (expectedCycleTimes.isEmpty()) {
            reportBuilder.append("No expected cycle times available.\n");
        } else {
            for (Map.Entry<Double, Double> entry : expectedCycleTimes.entrySet()) {
                reportBuilder.append("- Day ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append(" days expected\n");
            }
        }

        reportBuilder.append("\n### Actual Cycle Times (in Days)\n");
        if (actualCycleTimes.isEmpty()) {
            reportBuilder.append("No actual cycle times available for the reporting period.\n");
        } else {
            for (Map.Entry<Double, Double> entry : actualCycleTimes.entrySet()) {
                reportBuilder.append("- Day ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append(" days actual\n");
            }
        }

        reportBuilder.append("\n### Throughput Data\n");
        if (throughputData.isEmpty()) {
            reportBuilder.append("No throughput data available.\n");
        } else {
            for (Map.Entry<Double, Double> entry : throughputData.entrySet()) {
                reportBuilder.append("- Day ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append(" units processed\n");
            }
        }

        // Display the report in the TextArea
        reportOutput.setText(reportBuilder.toString());
    }

    @FXML
    private void closePopup() {
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }
}
