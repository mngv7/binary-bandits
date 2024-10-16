package com.example.protrack.applicationpages;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.employees.SelectedEmployeeSingleton;
import com.example.protrack.report.UserReport;
import com.example.protrack.users.AbstractUser;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.utility.LoggedInUserSingleton;
import com.example.protrack.workorder.WorkOrdersDAO;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import com.example.protrack.workorderproducts.WorkOrderProductsDAO;
import com.example.protrack.workorderproducts.WorkOrderProductsDAOImplementation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller for the expanded view of an employee's details.
 * This class manages the display of an employee's information and statistics,
 * as well as handling navigation and employee removal functionality.
 */
public class ExpandedEmployeeController {

    @FXML
    public Button removeEmployeeButton;

    @FXML
    public Label employeeRole;

    @FXML
    public Label fullName;

    @FXML
    public Label emailAddress;

    @FXML
    public Label dob;

    @FXML
    public Label phoneNo;

    @FXML
    public Label gender;

    @FXML
    public Label initialsIcon;

    @FXML
    public Label employeeIdLabel;

    @FXML
    private Label employeeNameTitle;

    @FXML
    private Label totalWorkOrdersLabel;

    @FXML
    private Label totalProductsProducedLabel;

    @FXML
    private Label totalPartsUsedLabel;

    @FXML
    private Label onScheduleRateLabel;

    @FXML
    private Label totalProductionCostLabel;

    @FXML
    private Label ordersByStatusLabel;

    private UserReport userReport;

    private Integer employeeId;
    private MainController mainController;

    /**
     * Initializes the controller by fetching the details of the selected employee
     * and populating UI fields with the retrieved information.
     */
    public void initialize() {
        Integer loggedInId = LoggedInUserSingleton.getInstance().getEmployeeId();
        UsersDAO usersDAO = new UsersDAO();

        String firstName = SelectedEmployeeSingleton.getInstance().getEmployeeFirstName();
        String lastname = SelectedEmployeeSingleton.getInstance().getEmployeeLastName();

        employeeId = usersDAO.getEmployeeIdByFullName(firstName + " " + lastname);

        employeeNameTitle.setText(firstName + " " + lastname);

        AbstractUser selectedUser = usersDAO.getUserById(employeeId);

        removeEmployeeButton.setDisable(!usersDAO.getUserById(loggedInId).getAccessLevel().equals("HIGH"));

        String accessLevel = selectedUser.getAccessLevel();
        String role = switch (accessLevel) {
            case "HIGH" -> "Manager";
            case "MEDIUM" -> "Stock Controller";
            case "LOW" -> "Production Worker";
            default -> "Unknown Role"; // Handle unexpected access levels
        };

        String initials = selectedUser.getFirstName().charAt(0) + selectedUser.getLastName().substring(0, 1);
        initialsIcon.setText(initials);

        employeeRole.setText(role);
        fullName.setText(firstName + " " + lastname);
        emailAddress.setText(selectedUser.getEmail());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(selectedUser.getDob());

        dob.setText(formattedDate);
        phoneNo.setText(selectedUser.getPhoneNo());
        gender.setText(selectedUser.getGender());
        employeeIdLabel.setText(String.valueOf(selectedUser.getEmployeeId()));

        List<ProductionUser> productionUsers = usersDAO.getProductionUsers();
        List<Customer> customers = new CustomerDAOImplementation().getAllCustomers();

        WorkOrdersDAO workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);
        WorkOrderProductsDAO workOrderProductsDAO = new WorkOrderProductsDAOImplementation();

        userReport = new UserReport(workOrdersDAO, workOrderProductsDAO, employeeId);

        populateUserStatistics();
    }

    /**
     * Populates the various user Key Performance Indicators (KPIs) for display.
     */
    private void populateUserStatistics() {
        // Fetch and display total work orders
        int totalWorkOrders = userReport.calculateTotalOrders();
        totalWorkOrdersLabel.setText(String.valueOf(totalWorkOrders));

        // Fetch and display total products produced
        int totalProductsProduced = userReport.calculateTotalProductsProduced();
        totalProductsProducedLabel.setText(String.valueOf(totalProductsProduced));

        // Fetch and display total parts used
        int totalPartsUsed = userReport.calculateTotalPartsUsed();
        totalPartsUsedLabel.setText(String.valueOf(totalPartsUsed));

        // Fetch and display on-schedule rate
        double onScheduleRate = userReport.calculateOnScheduleRate();
        onScheduleRateLabel.setText(String.format("%.2f%%", onScheduleRate));

        // Fetch and display total production cost
        double totalProductionCost = userReport.calculateTotalProductionCost();
        totalProductionCostLabel.setText(String.format("$%.2f", totalProductionCost));

        // Fetch and display orders by status with each status on a new line
        Map<String, Integer> ordersByStatus = userReport.calculateTotalOrdersByStatus();
        StringBuilder statusText = new StringBuilder();
        for (Map.Entry<String, Integer> entry : ordersByStatus.entrySet()) {
            statusText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        ordersByStatusLabel.setText(statusText.toString().trim()); // Remove trailing newline
    }

    /**
     * Sets a reference to the main controller for navigation back to the main page.
     *
     * @param mainController the main controller of the application
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Navigates back to the employees page.
     */
    @FXML
    public void backToEmployees() {
        if (mainController != null) {
            mainController.employees();
        }
    }

    /**
     * Handles the removal of an employee by deleting the selected employee from the database.
     */
    public void onRemoveEmployeeButtonPress() {
        Integer loggedInId = LoggedInUserSingleton.getInstance().getEmployeeId();

        if (!Objects.equals(employeeId, loggedInId)) {
            UsersDAO usersDAO = new UsersDAO();

            try {
                usersDAO.deleteUserById(employeeId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            backToEmployees();
        } else {
            System.out.println("You can't do that silly!");
        }
    }
}
