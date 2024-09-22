package com.example.protrack.applicationpages;

import com.example.protrack.employees.SelectedEmployeeSingleton;
import com.example.protrack.users.AbstractUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.utility.LoggedInUserSingleton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Objects;

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

    private Integer employeeId;

    public void initialize() {
        Integer loggedInId = LoggedInUserSingleton.getInstance().getEmployeeId();
        UsersDAO usersDAO = new UsersDAO();

        removeEmployeeButton.setDisable(!usersDAO.getUserById(loggedInId).getAccessLevel().equals("HIGH"));

        String firstName = SelectedEmployeeSingleton.getInstance().getEmployeeFirstName();
        String lastname = SelectedEmployeeSingleton.getInstance().getEmployeeLastName();

        employeeId = usersDAO.getEmployeeIdByFullName(firstName + " " + lastname);

        employeeNameTitle.setText(firstName + " " + lastname);

        AbstractUser selectedUser = usersDAO.getUserById(employeeId);

        String accessLevel = selectedUser.getAccessLevel();
        String role = switch (accessLevel) {
            case "HIGH" -> "Manager";
            case "MEDIUM" -> "Stock Controller";
            case "LOW" -> "Production Worker";
            default -> "Unknown Role"; // Handle unexpected access levels
        };


        String initials = selectedUser.getFirstName().charAt(0) + selectedUser.getLastName().substring(0,1);
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

    public void onRemoveEmployeePress() throws SQLException {

        Integer loggedInId = LoggedInUserSingleton.getInstance().getEmployeeId();

        if (!Objects.equals(employeeId, loggedInId)) {
            UsersDAO usersDAO = new UsersDAO();

            usersDAO.deleteUserById(employeeId);
            backToEmployees();
        } else {
            System.out.println("You can't do that silly!");
        }
    }
}
