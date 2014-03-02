package cs2340.woms.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A base class for observable datasets.
 */
public abstract class ObservableDataSet<T> {

    private List<DataSetObserver<T>> observers = new ArrayList<DataSetObserver<T>>();

    /**
     * Adds a new observer to be notified when {@link #notifyObservers()} is
     * called.
     *
     * @param observer the observer to add.
     */
    public void addObserver(DataSetObserver<T> observer) {
        observers.add(observer);
    }

    /**
     * Removes the given observer, preventing it from being updated when
     * {@link #notifyObservers()} is called.
     *
     * @param observer the observer to remove.
     */
    public void removeObserver(DataSetObserver<T> observer) {
        observers.remove(observer);
    }

    /**
     * Clears all observers from this dataset.
     */
    public void clearObservers() {
        observers.clear();
    }

    /**
     * Should be called whenever the dataset this represents changes. All
     * registered observers will be notified through their
     * {@link DataSetObserver#update(Collection)} method.
     */
    public void notifyObservers() {
        for (DataSetObserver<T> observer: observers) {
            observer.update(getDataSet());
        }
    }

    /**
     * Get the backing dataset.
     *
     * @return the backing dataset.
     */
    protected abstract Collection<T> getDataSet();
}
