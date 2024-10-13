package com.example.protrack.observers;

import com.example.protrack.supplier.Supplier;
import com.example.protrack.supplier.SupplierDAOImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * The {@code SuppliersTableSubject} class implements the {@link Subject} interface,
 * managing a list of suppliers and providing observer functionality for components
 * that need to respond to changes in the supplier data.
 * It maintains a collection of observers that are notified when supplier data is
 * synchronized from the database.
 */
public class SuppliersTableSubject implements Subject<Supplier> {

    private final List<Observer> observers = new ArrayList<>(); // To hold multiple observers
    private final ObservableList<Supplier> suppliers = FXCollections.observableArrayList();

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
     * Notifies all registered observers about changes in the supplier data.
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * Retrieves the supplier data managed by this subject as an {@link ObservableList}.
     *
     * @return an {@code ObservableList<Supplier>} containing the current suppliers
     */
    @Override
    public ObservableList<Supplier> getData() {
        return suppliers; // Return the current list of suppliers
    }

    /**
     * Synchronizes the supplier data with the database. This method retrieves
     * the latest supplier data from the database and updates the internal list
     * if there are any changes. It also notifies registered observers of any updates.
     */
    @Override
    public void syncDataFromDB() {
        List<Supplier> dbSuppliers = new SupplierDAOImplementation().getAllSuppliers();

        // Check if there are changes
        if (!new HashSet<>(suppliers).containsAll(dbSuppliers) || !new HashSet<>(dbSuppliers).containsAll(suppliers)) { // Check for content changes
            suppliers.setAll(dbSuppliers); // Update the ObservableList
            notifyObservers(); // Notify observers of the update
        }
    }
}
