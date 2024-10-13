package com.example.protrack.observers;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * The {@code CustomersTableSubject} class implements the {@link Subject} interface,
 * managing a list of customers and providing observer functionality for components
 * that need to respond to changes in the customer data.
 * It maintains a collection of observers that are notified when customer data is
 * synchronized from the database.
 */
public class CustomersTableSubject implements Subject<Customer> {

    private final List<Observer> observers = new ArrayList<>(); // To hold multiple observers
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

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
     * Notifies all registered observers about changes in the customer data.
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * Retrieves the customer data managed by this subject as an {@link ObservableList}.
     *
     * @return an {@code ObservableList<Customer>} containing the current customers
     */
    @Override
    public ObservableList<Customer> getData() {
        return customers;
    }

    /**
     * Synchronizes the customer data with the database. This method retrieves
     * the latest customer data from the database and updates the internal list
     * if there are any changes. It also notifies registered observers of any updates.
     */
    @Override
    public void syncDataFromDB() {
        List<Customer> dbCustomers = new CustomerDAOImplementation().getAllCustomers();
        System.out.println(dbCustomers);

        // Check if there are changes
        if (!new HashSet<>(customers).containsAll(dbCustomers) || !new HashSet<>(dbCustomers).containsAll(customers)) { // Check for content changes
            customers.setAll(dbCustomers); // Update the ObservableList
            notifyObservers(); // Notify observers of the update
        }
    }
}
