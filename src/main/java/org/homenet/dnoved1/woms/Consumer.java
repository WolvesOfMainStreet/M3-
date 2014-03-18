package org.homenet.dnoved1.woms;

/**
 * Taken from Java 8. <a href=http://download.java.net/jdk8/docs/api/java/util/function/Consumer.html>Link</a>. <br/>
 * <br/>
 * Represents an operation that accepts a single input argument and returns no
 * result. Unlike most other functional interfaces, Consumer is expected to
 * operate via side-effects. <br/>
 * <br/>
 * This is a functional interface whose functional method is
 * {@link #accept(Object)}.
 *
 * @param <T>
 */
public interface Consumer<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t);
}
