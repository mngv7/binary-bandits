package com.example.protrack.applicationpages;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductsController {

    private static final String TITLE = "Create Product";
    private static final int WIDTH = 900;
    private static final int HEIGHT = 360;

    public void openCreateProductPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/protrack/create-product-view.fxml"));
            Parent createProductRoot = fxmlLoader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle(TITLE);

            Scene scene = new Scene(createProductRoot, WIDTH, HEIGHT);
            popupStage.setScene(scene);

            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable(ActionEvent actionEvent) {
    }
}
