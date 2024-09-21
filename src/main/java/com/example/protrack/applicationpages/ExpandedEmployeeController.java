package com.example.protrack.applicationpages;

import com.example.protrack.employees.SelectedEmployeeSingleton;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.utility.LoggedInUserSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ExpandedEmployeeController {

    @FXML
    public Button removeEmployeeButton;

    @FXML
    public Label employeeRole;

    @FXML
    private Label employeeNameTitle;

    public void initialize() {
        Integer loggedInId = LoggedInUserSingleton.getInstance().getEmployeeId();
        UsersDAO usersDAO = new UsersDAO();

        removeEmployeeButton.setDisable(!usersDAO.getUserById(loggedInId).getAccessLevel().equals("HIGH"));

        String firstName = SelectedEmployeeSingleton.getInstance().getEmployeeFirstName();
        String lastname = SelectedEmployeeSingleton.getInstance().getEmployeeLastName();

        Integer employeeId = usersDAO.getEmployeeIdByFullName(firstName + " " + lastname);

        employeeNameTitle.setText(firstName + " " + lastname);

        String accessLevel = usersDAO.getUserById(employeeId).getAccessLevel();
        String role = switch (accessLevel) {
            case "HIGH" -> "Manager";
            case "MEDIUM" -> "Stock Controller";
            case "LOW" -> "Production Worker";
            default -> "Unknown Role"; // Handle unexpected access levels
        };

        employeeRole.setText(role);
    }

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void backToEmployees() {
        if (mainController != null) {
            mainController.employees();
        }
    }
}
