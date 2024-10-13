package com.example.protrack.report;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code GenerateReportController} class handles the generation of reports
 * based on selected date ranges and displays the report output.
 */
public class GenerateReportController {

    @FXML
    private DatePicker startDatePicker; // Date picker for the start date

    @FXML
    private DatePicker endDatePicker; // Date picker for the end date

    @FXML
    private TextArea reportOutput; // Text area to display the report

    @FXML
    private Button closePopupButton; // Button to close the popup

    private OrgReport orgReport; // Instance of OrgReport for generating reports

    /**
     * Setter method to allow external initialisation of OrgReport.
     *
     * @param orgReport the OrgReport instance to set
     */
    public void setOrgReport(OrgReport orgReport) {
        this.orgReport = orgReport;
    }

    /**
     * Handles the report generation when the generate button is clicked.
     */
    @FXML
    private void handleGenerateReport() {
        LocalDate startDate = startDatePicker.getValue(); // Get start date from picker
        LocalDate endDate = endDatePicker.getValue(); // Get end date from picker

        if (startDate != null && endDate != null) {
            generateReport(startDate, endDate);
        } else {
            reportOutput.setText("Please select both start and end dates."); // Error message for date selection
        }
    }

    /**
     * Generates the report based on the selected date range.
     *
     * @param startDate the start date for the report
     * @param endDate   the end date for the report
     */
    private void generateReport(LocalDate startDate, LocalDate endDate) {
        if (orgReport == null) {
            reportOutput.setText("Report data is not available."); // Error message if orgReport is not initialized
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

        // Append forecast data to the report
        reportBuilder.append("### Forecast Data\n");
        if (forecastData.isEmpty()) {
            reportBuilder.append("No forecast data available.\n");
        } else {
            for (Map.Entry<Double, Double> entry : forecastData.entrySet()) {
                reportBuilder.append("- Day ").append(entry.getKey()).append(": ")
                        .append(String.format("%.2f", entry.getValue())).append(" units forecasted\n");
            }
        }

        // Append expected cycle times to the report
        reportBuilder.append("\n### Expected Cycle Times (in Days)\n");
        if (expectedCycleTimes.isEmpty()) {
            reportBuilder.append("No expected cycle times available.\n");
        } else {
            for (Map.Entry<Double, Double> entry : expectedCycleTimes.entrySet()) {
                reportBuilder.append("- Day ").append(entry.getKey()).append(": ")
                        .append(String.format("%.2f", entry.getValue())).append(" days expected\n");
            }
        }

        // Append actual cycle times to the report
        reportBuilder.append("\n### Actual Cycle Times (in Days)\n");
        if (actualCycleTimes.isEmpty()) {
            reportBuilder.append("No actual cycle times available for the reporting period.\n");
        } else {
            for (Map.Entry<Double, Double> entry : actualCycleTimes.entrySet()) {
                reportBuilder.append("- Day ").append(entry.getKey()).append(": ")
                        .append(String.format("%.2f", entry.getValue())).append(" days actual\n");
            }
        }

        // Append throughput data to the report
        reportBuilder.append("\n### Throughput Data\n");
        if (throughputData.isEmpty()) {
            reportBuilder.append("No throughput data available.\n");
        } else {
            for (Map.Entry<Double, Double> entry : throughputData.entrySet()) {
                reportBuilder.append("- Day ").append(entry.getKey()).append(": ")
                        .append(String.format("%.2f", entry.getValue())).append(" units processed\n");
            }
        }

        // Displays the report in the TextArea
        reportOutput.setText(reportBuilder.toString());
    }

    /**
     * Closes the report generation popup.
     */
    @FXML
    private void closePopup() {
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close(); // Close the popup window
    }
}
