package com.example.protrack.observers;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WorkOrderTableSubject implements Subject<WorkOrder> {

    private List<Observer> observers = new ArrayList<>(); // To hold multiple observers
    private ObservableList<WorkOrder> workOrders = FXCollections.observableArrayList();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void deregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public ObservableList<WorkOrder> getData() {
        return workOrders; // Return the current list of work orders
    }

    public void syncDataFromDB() {
        List<Customer> customers = new CustomerDAOImplementation().getAllCustomers();
        List<ProductionUser> productionUsers = new UsersDAO().getProductionUsers();
        List<WorkOrder> dbWorkOrders = new WorkOrdersDAOImplementation(productionUsers, customers).getAllWorkOrders();

        // Check if there are changes
        if (!new HashSet<>(workOrders).containsAll(dbWorkOrders) || !new HashSet<>(dbWorkOrders).containsAll(workOrders)) { // Check for content changes
            workOrders.setAll(dbWorkOrders); // Update the ObservableList
            notifyObservers(); // Notify observers of the update
        }
    }
}
