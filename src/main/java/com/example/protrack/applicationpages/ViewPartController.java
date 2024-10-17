package com.example.protrack.applicationpages;

import com.example.protrack.Main;
import com.example.protrack.database.WorkstationPartDBTable;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.requests.Requests;
import com.example.protrack.requests.RequestsDAO;
import com.example.protrack.warehouseutil.LocationsAndContentsDAO;
import com.example.protrack.warehouseutil.RealWarehouse;
import com.example.protrack.warehouseutil.RealWorkstation;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Objects;

public class ViewPartController {
    @FXML
    private Button closePopupButton;

    @FXML
    private TableView<RequestWithPartName> partRequestsTable;
    @FXML
    private TableColumn<RequestWithPartName, Integer> colLocationID;
    @FXML
    private TableColumn<RequestWithPartName, Integer> colPartRequestsPartID;
    @FXML
    private TableColumn<RequestWithPartName, String> colPartRequestsPartName;
    @FXML
    private TableColumn<RequestWithPartName, Integer> colPartRequestsPartQuantity;
    @FXML
    private TableColumn<RequestWithPartName, Void> acceptColumn;
    @FXML
    private TableColumn<RequestWithPartName, Void> rejectColumn;

    private ObservableList<RequestWithPartName> partRequestsList;

    public void initialize() {
        // Set up the TableView columns with the corresponding property values
        colPartRequestsPartID.setCellValueFactory(new PropertyValueFactory<>("partId"));

        colPartRequestsPartName.setCellValueFactory(new PropertyValueFactory<>("partName"));

        colPartRequestsPartQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        // Initialize the ObservableList and set it to the TableView
        partRequestsList = FXCollections.observableArrayList();
        partRequestsTable.setItems(partRequestsList);
        partRequestsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colLocationID.setCellValueFactory(new PropertyValueFactory<>("locationId")); // Bind the location ID column

        // Accept column
        acceptColumn.setCellFactory(column -> new TableCell<RequestWithPartName, Void>() {
            private final Button acceptButton = new Button("✔");

            {
                acceptButton.setOnAction(e -> {
                    RequestWithPartName request = getTableView().getItems().get(getIndex());
                    handleAccept(request);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : acceptButton);
            }
        });

        // Reject column
        rejectColumn.setCellFactory(column -> new TableCell<RequestWithPartName, Void>() {
            private final Button rejectButton = new Button("✖");

            {
                rejectButton.setOnAction(e -> {
                    RequestWithPartName request = getTableView().getItems().get(getIndex());
                    handleReject(request);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : rejectButton);
            }
        });

        // Load and display the initial list of requests
        refreshTable();
    }

    public void refreshTable() {
        partRequestsList.clear();
        RequestsDAO requestsDAO = new RequestsDAO();
        PartsDAO partsDAO = new PartsDAO(); // Create an instance of PartsDAO

        List<Requests> fetchedRequests = requestsDAO.getAllRequests();

        for (Requests request : fetchedRequests) {
            // Fetch part name using partId
            String partName = partsDAO.getPartById(request.getPartId()).getName();
            // Add the request to the observable list along with part name
            partRequestsList.add(new RequestWithPartName(request, partName));
        }
    }

    // Inner class to wrap Requests with part name
    public static class RequestWithPartName extends Requests {
        private final String partName;

        public RequestWithPartName(Requests request, String partName) {
            super(request.getLocationId(), request.getPartId(), request.getRequestId(), request.getQuantity());
            this.partName = partName;
        }

        public String getPartName() {
            return partName;
        }

        public int getRequestID() {
            return getRequestId();
        }
    }

    public void handleReject(Requests request) {
        RequestsDAO requestsDAO = new RequestsDAO();
        requestsDAO.deleteRequestById(request.getRequestId());
    }

    public void handleAccept(Requests request) {
        RequestsDAO requestsDAO = new RequestsDAO();
        requestsDAO.deleteRequestById(request.getRequestId());
        // full functionality could be implemented here
    }

    public void onClosePopupButton() {
        // Get the current stage (popup window)
        Stage stage = (Stage) closePopupButton.getScene().getWindow();
        stage.close();
    }
}
