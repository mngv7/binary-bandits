package com.example.protrack.applicationpages;
import java.util.List;  // Ensure this import is correct
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


import com.example.protrack.users.AbstractUser;
import com.example.protrack.users.UsersDAO;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.awt.*;

public class EmployeesController {

    @FXML
    public VBox employeeNames;

    public void initialize() {
        UsersDAO usersDAO = new UsersDAO();
        List<AbstractUser> allUsers = usersDAO.getAllUsers();

        for (AbstractUser user : allUsers) {
            VBox newRow = new VBox();

            String employeeTitle = switch (user.getAccessLevel()) {
                case "HIGH" -> "Manager";
                case "MEDIUM" -> "Stock Controller";
                case "LOW" -> "Production Worker";
                default -> "Unknown";
            };

            Label employeeNameLabel = new Label(user.getFirstName());
            Label employeeTitleLabel = new Label(employeeTitle);
            Label spacing = new Label(" ");
            newRow.getChildren().addAll(employeeNameLabel, employeeTitleLabel, spacing);

            employeeNames.getChildren().add(newRow);
        }
    }
}
