package com.example.protrack.workorderobserver;

import com.example.protrack.customer.Customer;
import com.example.protrack.customer.CustomerDAOImplementation;
import com.example.protrack.supplier.Supplier;
import com.example.protrack.supplier.SupplierDAOImplementation;
import com.example.protrack.users.ProductionUser;
import com.example.protrack.users.UsersDAO;
import com.example.protrack.workorder.WorkOrder;
import com.example.protrack.workorder.WorkOrdersDAOImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SuppliersTableSubject implements Subject {
    private List<Observer> observers = new ArrayList<>(); // To hold multiple observers
    private ObservableList<Supplier> suppliers = FXCollections.observableArrayList();

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

    public ObservableList<Supplier> getSuppliers() {
        return suppliers; // Return the current list of suppliers
    }

    public void getSuppliersFromDB() {
        List<Supplier> dbSuppliers = new SupplierDAOImplementation().getAllSuppliers();

        // Check if there are changes
        if (!new HashSet<>(suppliers).containsAll(dbSuppliers) || !new HashSet<>(dbSuppliers).containsAll(suppliers)) { // Check for content changes
            suppliers.setAll(dbSuppliers); // Update the ObservableList
            notifyObservers(); // Notify observers of the update
        }
    }
}
