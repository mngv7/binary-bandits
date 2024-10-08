package com.example.protrack.workorderobserver;

import com.example.protrack.parts.Parts;
import com.example.protrack.parts.PartsDAO;
import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PartsTableSubject implements Subject {
    private List<Observer> observers = new ArrayList<>(); // To hold multiple observers
    private ObservableList<Parts> parts = FXCollections.observableArrayList();

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

    public void getPartsFromDB() {
        List<Parts> dbParts = new PartsDAO().getAllParts();

        // Check if there are changes
        if (!new HashSet<>(parts).containsAll(dbParts) || !new HashSet<>(dbParts).containsAll(parts)) { // Check for content changes
            parts.setAll(dbParts); // Update the ObservableList
            notifyObservers(); // Notify observers of the update
        }
    }
}
