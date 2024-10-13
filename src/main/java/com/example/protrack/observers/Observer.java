package com.example.protrack.observers;

/**
 * The {@code Observer} interface defines a contract for observer objects
 * that need to be notified of changes in the subject they are observing.
 * Implementing classes must provide their own implementation of the {@link #update()} method
 */
public interface Observer {

    /**
     * This method is called to notify the observer of a change in the subject.
     */
    void update();
}