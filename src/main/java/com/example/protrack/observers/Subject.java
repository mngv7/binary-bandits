package com.example.protrack.observers;

import javafx.collections.ObservableList;

/**
 * The {@code Subject} interface defines a contract for subject objects
 * that maintain a list of observers and notify them of any changes.
 * It allows observers to register and deregister themselves,
 * providing mechanisms for notifying all registered observers.
 *
 * @param <T> the type of data that the subject manages and notifies its observers about
 */
public interface Subject<T> {

    /**
     * Registers an observer to the subject.
     *
     * @param observer the observer to be registered
     */
    void registerObserver(Observer observer);

    /**
     * Deregisters an observer from the subject.
     *
     * @param observer the observer to be deregistered
     */
    void deregisterObserver(Observer observer);

    /**
     * Notifies all registered observers about changes in the subject's state.
     */
    void notifyObservers();

    /**
     * Retrieves the data managed by the subject as an ObservableList.
     *
     * @return an {@code ObservableList} containing the subject's data
     */
    ObservableList<T> getData();

    /**
     * Synchronizes the subject's data with the database.
     */
    void syncDataFromDB();
}
