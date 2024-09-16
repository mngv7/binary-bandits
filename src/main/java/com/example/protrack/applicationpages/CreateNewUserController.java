package com.example.protrack.applicationpages;
import com.example.protrack.users.ManagerialUser;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.users.WarehouseUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import com.example.protrack.Main;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

public class CreateNewUserController {

    @FXML
    public Button closePopupButton;

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField lastNameField;

    @FXML
    public DatePicker dobPicker;

    @FXML
    public TextField emailField;

    @FXML
    public TextField phoneNumberField;

    @FXML
    public ComboBox genderComboBox;

    @FXML
    public ComboBox accessLevelComboBox;

    @FXML
    public Button addUserButton;

    public void onClosePopupButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText("Cancel User Creation");
        alert.setContentText("Are you sure you want to cancel?");
        alert.setGraphic(null);

        DialogPane dialogPane = alert.getDialogPane();
        String stylesheet = Objects.requireNonNull(Main.class.getResource("cancelAlert.css")).toExternalForm();
        dialogPane.getStyleClass().add("cancelDialog");
        dialogPane.getStylesheets().add(stylesheet);

        ButtonType confirmBtn = new ButtonType("Confirm", ButtonBar.ButtonData.YES);
        ButtonType backBtn = new ButtonType("Back", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(confirmBtn, backBtn);
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        Node confirmButton = dialogPane.lookupButton(confirmBtn);
        ButtonBar.setButtonData(confirmButton, ButtonBar.ButtonData.LEFT);
        confirmButton.setId("confirmBtn");
        Node backButton = dialogPane.lookupButton(backBtn);
        ButtonBar.setButtonData(backButton, ButtonBar.ButtonData.RIGHT);
        backButton.setId("backBtn");
        alert.showAndWait();
        if (alert.getResult().getButtonData() == ButtonBar.ButtonData.YES) {
            alert.close();
            stage.close();
        } else if (alert.getResult().getButtonData() == ButtonBar.ButtonData.NO) {
            alert.close();
        }
    }

    public void createNewUser() throws SQLException {
        UsersDAO usersDAO = new UsersDAO();

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneNumberField.getText();
        String gender = genderComboBox.getValue().toString();
        String password = "default";
        LocalDate localDate = dobPicker.getValue();
        Date sqlDate = Date.valueOf(localDate);
        Integer employeeId = usersDAO.getMaxEmployeeId() + 1;


        switch (accessLevelComboBox.getValue().toString()) {
            case "HIGH":
                usersDAO.newUser(new ManagerialUser(employeeId, firstName, lastName, sqlDate, email, phone, gender, password));
                break;
            case "MEDIUM":
                usersDAO.newUser(new WarehouseUser(employeeId, firstName, lastName, sqlDate, email, phone, gender, password));
                break;
            case "LOW":
                usersDAO.newUser(new ProductionUser(employeeId, firstName, lastName, sqlDate, email, phone, gender, password));
                break;
            default:
                System.out.println("Unknown access level.");
                break;
        }

        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }
}
