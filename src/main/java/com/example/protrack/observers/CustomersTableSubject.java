package com.example.protrack.observers;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CustomersTableSubject implements Subject<Customer> {
    private List<Observer> observers = new ArrayList<>(); // To hold multiple observers
    private ObservableList<Customer> customers = FXCollections.observableArrayList();

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

    public ObservableList<Customer> getData() {
        return customers;
    }

    public void syncDataFromDB() {
        List<Customer> dbCustomers = new CustomerDAOImplementation().getAllCustomers();

        // Check if there are changes
        if (!new HashSet<>(customers).containsAll(dbCustomers) || !new HashSet<>(dbCustomers).containsAll(customers)) { // Check for content changes
            customers.setAll(dbCustomers); // Update the ObservableList
            notifyObservers(); // Notify observers of the update
        }
    }
}
