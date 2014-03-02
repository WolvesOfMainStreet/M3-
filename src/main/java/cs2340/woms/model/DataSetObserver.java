package cs2340.woms.model;

import java.util.Collection;

/**
 * An observer on a dataset.
 */
public interface DataSetObserver<T> {

    /**
     * Should get called whenever the observing dataset changes.
     *
     * @param dataset the new dataset.
     */
    void update(Collection<T> dataset);
}
