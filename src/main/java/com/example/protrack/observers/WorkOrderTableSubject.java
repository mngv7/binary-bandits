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

/**
 * The {@code WorkOrderTableSubject} class implements the {@link Subject} interface,
 * managing a list of work orders and providing observer functionality for components
 * that need to respond to changes in the work order data.
 * It maintains a collection of observers that are notified when work order data is
 * synchronized from the database.
 */
public class WorkOrderTableSubject implements Subject<WorkOrder> {

    private final List<Observer> observers = new ArrayList<>(); // To hold multiple observers
    private final ObservableList<WorkOrder> workOrders = FXCollections.observableArrayList();

    /**
     * Registers an observer to the subject.
     *
     * @param observer the observer to be registered
     */
    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Deregisters an observer from the subject.
     *
     * @param observer the observer to be deregistered
     */
    @Override
    public void deregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers about changes in the work order data.
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * Retrieves the work order data managed by this subject as an {@link ObservableList}.
     *
     * @return an {@code ObservableList<WorkOrder>} containing the current work orders
     */
    @Override
    public ObservableList<WorkOrder> getData() {
        return workOrders; // Return the current list of work orders
    }

    /**
     * Synchronizes the work order data with the database. This method retrieves
     * the latest work order data from the database and updates the internal list
     * if there are any changes. It also notifies registered observers of any updates.
     */
    @Override
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
