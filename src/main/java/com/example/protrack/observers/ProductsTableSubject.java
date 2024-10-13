package com.example.protrack.observers;

import com.example.protrack.products.Product;
import com.example.protrack.products.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * The {@code ProductsTableSubject} class implements the {@link Subject} interface,
 * managing a list of products and providing observer functionality for components
 * that need to respond to changes in the product data.
 * It maintains a collection of observers that are notified when product data is
 * synchronized from the database.
 */
public class ProductsTableSubject implements Subject<Product> {

    private final List<Observer> observers = new ArrayList<>(); // To hold multiple observers
    private final ObservableList<Product> products = FXCollections.observableArrayList();

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
     * Notifies all registered observers about changes in the product data.
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * Retrieves the product data managed by this subject as an {@link ObservableList}.
     *
     * @return an {@code ObservableList<Product>} containing the current products
     */
    @Override
    public ObservableList<Product> getData() {
        return products; // Return the current list of products
    }

    /**
     * Synchronizes the product data with the database. This method retrieves
     * the latest product data from the database and updates the internal list
     * if there are any changes. It also notifies registered observers of any updates.
     */
    @Override
    public void syncDataFromDB() {
        List<Product> dbProducts = new ProductDAO().getAllProducts();

        // Check if there are changes
        if (!new HashSet<>(products).containsAll(dbProducts) || !new HashSet<>(dbProducts).containsAll(products)) { // Check for content changes
            products.setAll(dbProducts); // Update the ObservableList
            notifyObservers(); // Notify observers of the update
        }
    }
}
