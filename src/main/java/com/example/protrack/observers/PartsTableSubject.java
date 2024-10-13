package com.example.protrack.observers;

import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * The {@code PartsTableSubject} class implements the {@link Subject} interface,
 * managing a list of parts and providing observer functionality for components
 * that need to respond to changes in the parts data.
 * It maintains a collection of observers that are notified when parts data is
 * synchronized from the database.
 */
public class PartsTableSubject implements Subject<Parts> {

    private final List<Observer> observers = new ArrayList<>(); // To hold multiple observers
    private final ObservableList<Parts> parts = FXCollections.observableArrayList();

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
     * Notifies all registered observers about changes in the parts data.
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * Retrieves the parts data managed by this subject as an {@link ObservableList}.
     *
     * @return an {@code ObservableList<Parts>} containing the current parts
     */
    @Override
    public ObservableList<Parts> getData() {
        return parts;
    }

    /**
     * Synchronizes the parts data with the database. This method retrieves
     * the latest parts data from the database and updates the internal list
     * if there are any changes. It also notifies registered observers of any updates.
     */
    @Override
    public void syncDataFromDB() {
        List<Parts> dbParts = new PartsDAO().getAllParts();

        // Check if there are changes
        if (!new HashSet<>(parts).containsAll(dbParts) || !new HashSet<>(dbParts).containsAll(parts)) { // Check for content changes
            parts.setAll(dbParts); // Update the ObservableList
            notifyObservers(); // Notify observers of the update
        }
    }
}
