package org.homenet.dnoved1.woms;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * An abstract future that simply delegates all calls to another future. Also
 * has support for executing a {@link Consumer} whenever a result is retrieved
 * through any of the get methods.
 *
 * @param <V> the value that this future should return.
 */
public class DelegateFuture<V>  implements Future<V> {

    /**The future to delegate method calls to.*/
    private Future<V> delegate;
    /**The behavior to be executed whenever a result is retrieved.*/
    private Consumer<V> getBehavior;

    /**
     * Creates a new future that delegates all method calls to the given
     * delegate future.
     *
     * @param delegate the future to delegate method calls to.
     */
    public DelegateFuture(Future<V> delegate) {
        this.delegate = delegate;
    }

    /**
     * Creates a new future that delegates all method calls to the given
     * delegate future. Additionally, whenever {@link #get()} or
     * {@link #get(long, TimeUnit)} is called the given consumer will be fed
     * the result.
     *
     * @param delegate the future to delegate method calls to.
     * @param getBehavior the consumer who's accept method will get called
     * whenever a result is retrieved using the get methods.
     */
    public DelegateFuture(Future<V> delegate, Consumer<V> getBehavior) {
        this(delegate);
        this.getBehavior = getBehavior;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return delegate.cancel(mayInterruptIfRunning);
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        V result = delegate.get();

        if (getBehavior != null) {
            getBehavior.accept(result);
        }

        return result;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        V result = delegate.get();

        if (getBehavior != null) {
            getBehavior.accept(result);
        }

        return result;
    }

    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public boolean isDone() {
        return delegate.isDone();
    }
}