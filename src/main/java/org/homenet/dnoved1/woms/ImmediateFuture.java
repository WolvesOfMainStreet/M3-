package org.homenet.dnoved1.woms;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * A future who's result has already been computed.
 *
 * @param <V> the value that this future should return.
 */
public class ImmediateFuture<V> implements Future<V> {

    /**The result to return.*/
    private V result;

    /**
     * Creates a new immediate future who's result is the given result.
     *
     * @param result the already computed result.
     */
    public ImmediateFuture(V result) {
        this.result = result;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        // Task is already completed.
        return false;
    }

    @Override
    public V get() {
        return result;
    }

    @Override
    public V get(long timeout, TimeUnit unit) {
        return result;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return true;
    }
}
