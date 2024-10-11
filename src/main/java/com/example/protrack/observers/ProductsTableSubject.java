package com.example.protrack.observers;

import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ProductsTableSubject implements Subject<Product> {
    private final List<Observer> observers = new ArrayList<>(); // To hold multiple observers
    private final ObservableList<Product> products = FXCollections.observableArrayList();

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

    public ObservableList<Product> getData() {
        return products; // Return the current list of suppliers
    }

    public void syncDataFromDB() {
        List<Product> dbProducts = new ProductDAO().getAllProducts();

        // Check if there are changes
        if (!new HashSet<>(products).containsAll(dbProducts) || !new HashSet<>(dbProducts).containsAll(products)) { // Check for content changes
            products.setAll(dbProducts); // Update the ObservableList
            notifyObservers(); // Notify observers of the update
        }
    }
}
