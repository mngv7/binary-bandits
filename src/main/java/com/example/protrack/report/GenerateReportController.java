package com.example.protrack.report;

import javafx.fxml.FXML;
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

    private OrgReport orgReport; // This should be initialized

    // Setter method to allow external initialization
    public void setOrgReport(OrgReport orgReport) {
        this.orgReport = orgReport;
    }

    @FXML
    public void initialize() {
        // Any necessary initialization can be done here
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

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        // Generate report metrics
        Map<Double, Double> forecastData = orgReport.forecastWorkOrderChartValues();
        Map<Double, Double> expectedCycleTimes = orgReport.calculateOrdersExpectedCycleTimeChartValues();
        Map<Double, Double> actualCycleTimes = orgReport.calculateOrdersActualCycleTimeChartValues();
        Map<Double, Double> throughputData = orgReport.calculateThroughputChartValues();

        // Build the report string
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Report from ").append(startDate).append(" to ").append(endDate).append("\n\n");

        reportBuilder.append("Forecast Data:\n").append(forecastData).append("\n");
        reportBuilder.append("Expected Cycle Times:\n").append(expectedCycleTimes).append("\n");
        reportBuilder.append("Actual Cycle Times:\n").append(actualCycleTimes).append("\n");
        reportBuilder.append("Throughput Data:\n").append(throughputData).append("\n");

        // Display the report in the TextArea
        reportOutput.setText(reportBuilder.toString());
    }

    @FXML
    private void handleCancel() {
        closePopup();
    }

    private void closePopup() {
        Stage stage = (Stage) startDatePicker.getScene().getWindow();
        stage.close();
    }
}
