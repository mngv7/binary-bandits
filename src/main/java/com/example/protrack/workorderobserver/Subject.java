package com.example.protrack.workorderobserver;

import com.example.protrack.workorder.WorkOrder;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public interface Subject {
    void registerObserver(Observer observer);
    void deregisterObserver(Observer observer);
    void notifyObservers();
}
