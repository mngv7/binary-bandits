package com.example.protrack.observers;

import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PartsTableSubject implements Subject<Parts> {
    private final List<Observer> observers = new ArrayList<>(); // To hold multiple observers
    private final ObservableList<Parts> parts = FXCollections.observableArrayList();

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

    public ObservableList<Parts> getData() {
        return parts;
    }

    public void syncDataFromDB() {
        List<Parts> dbParts = new PartsDAO().getAllParts();

        // Check if there are changes
        if (!new HashSet<>(parts).containsAll(dbParts) || !new HashSet<>(dbParts).containsAll(parts)) { // Check for content changes
            parts.setAll(dbParts); // Update the ObservableList
            notifyObservers(); // Notify observers of the update
        }
    }
}
