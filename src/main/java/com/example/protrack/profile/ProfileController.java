package com.example.protrack.profile;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
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
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ProfileController {

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

    public void initialize() {
        Integer employeeId = LoggedInUserSingleton.getInstance().getEmployeeId();
        UsersDAO usersDAO = new UsersDAO();

        AbstractUser selectedUser = usersDAO.getUserById(employeeId);

        String firstName = selectedUser.getFirstName();
        String lastName = selectedUser.getLastName();

        setProfileData(selectedUser, firstName, lastName);

        List<ProductionUser> productionUsers = usersDAO.getProductionUsers();
        List<Customer> customers = new CustomerDAOImplementation().getAllCustomers();

        WorkOrdersDAO workOrdersDAO = new WorkOrdersDAOImplementation(productionUsers, customers);
        WorkOrderProductsDAO workOrderProductsDAO = new WorkOrderProductsDAOImplementation();

        userReport = new UserReport(workOrdersDAO, workOrderProductsDAO, employeeId);
        populateUserStatistics();
    }

    private void setProfileData(AbstractUser selectedUser, String firstName, String lastName) {
        String initials = firstName.charAt(0) + String.valueOf(lastName.charAt(0));
        initialsIcon.setText(initials);

        employeeRole.setText(selectedUser.getAccessLevel());
        fullName.setText(firstName + " " + lastName);
        emailAddress.setText(selectedUser.getEmail());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(selectedUser.getDob());
        dob.setText(formattedDate);
        phoneNo.setText(selectedUser.getPhoneNo());
        gender.setText(selectedUser.getGender());
        employeeIdLabel.setText(String.valueOf(selectedUser.getEmployeeId()));
    }

    private void populateUserStatistics() {
        int totalWorkOrders = userReport.calculateTotalOrders();
        totalWorkOrdersLabel.setText(String.valueOf(totalWorkOrders));

        int totalProductsProduced = userReport.calculateTotalProductsProduced();
        totalProductsProducedLabel.setText(String.valueOf(totalProductsProduced));

        int totalPartsUsed = userReport.calculateTotalPartsUsed();
        totalPartsUsedLabel.setText(String.valueOf(totalPartsUsed));

        double onScheduleRate = userReport.calculateOnScheduleRate();
        onScheduleRateLabel.setText(String.format("%.2f%%", onScheduleRate));

        double totalProductionCost = userReport.calculateTotalProductionCost();
        totalProductionCostLabel.setText(String.format("$%.2f", totalProductionCost));

        // Format orders by status with each status on a new line
        Map<String, Integer> ordersByStatus = userReport.calculateTotalOrdersByStatus();
        StringBuilder statusText = new StringBuilder();
        for (Map.Entry<String, Integer> entry : ordersByStatus.entrySet()) {
            statusText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        ordersByStatusLabel.setText(statusText.toString().trim()); // Remove trailing newline
    }
}
