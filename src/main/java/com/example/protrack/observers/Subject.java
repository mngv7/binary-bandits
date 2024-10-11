package com.example.protrack.observers;

import javafx.collections.ObservableList;

public interface Subject<T> {
    void registerObserver(Observer observer);

    void deregisterObserver(Observer observer);

    void notifyObservers();

    ObservableList<T> getData();

    void syncDataFromDB();
}
