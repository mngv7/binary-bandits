package com.example.protrack.applicationpages;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.example.protrack.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import com.example.protrack.utility.LoggedInUserSingleton;

import com.example.protrack.users.AbstractUser;
import com.example.protrack.users.UsersDAO;
import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EmployeesController {

    @FXML
    public VBox employeeNames;

    @FXML
    public Button newUserButton;

    public void initialize() {
        Integer loggedInId = LoggedInUserSingleton.getInstance().getEmployeeId();
        UsersDAO usersDAO = new UsersDAO();

        newUserButton.setDisable(!usersDAO.getUserById(loggedInId).getAccessLevel().equals("HIGH"));

        loadAllUsers();
    }

    public void loadAllUsers() {
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

    private static final String TITLE = "Create User";
    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;

    public void openCreateNewUserPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/create-user-view.fxml"));
            Parent addPartsRoot = fxmlLoader.load();

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(TITLE);

            Scene scene = new Scene(addPartsRoot, WIDTH, HEIGHT);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("stylesheet.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            popupStage.setScene(scene);
            popupStage.setY(150);
            popupStage.setX(390);
            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
