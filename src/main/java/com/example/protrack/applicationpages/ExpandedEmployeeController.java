package com.example.protrack.applicationpages;

import com.example.protrack.employees.SelectedEmployeeSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ExpandedEmployeeController {

    @FXML
    public Button removeEmployeeButton;

    @FXML
    private Label employeeNameTitle;

    public void initialize() {
        String firstName = SelectedEmployeeSingleton.getInstance().getEmployeeFirstName();
        String lastname = SelectedEmployeeSingleton.getInstance().getEmployeeLastName();
        employeeNameTitle.setText(firstName + " " + lastname);
    }

    // Method to go back to the Employees view
    public void backToEmployees() {

    }
}
