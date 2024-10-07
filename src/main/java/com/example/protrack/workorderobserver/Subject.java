package com.example.protrack.workorderobserver;

public interface Subject {
    void addObserver(Object object);
    void removeObserver(Object object);
    void notifyObservers();
}
